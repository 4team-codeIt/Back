package com.brick.demo.auth.service;

import static com.brick.demo.security.JwtRequestFilter.BEARER_PREFIX;

import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.DuplicateEmailResponseDto;
import com.brick.demo.auth.dto.DuplicateNameRequestDto;
import com.brick.demo.auth.dto.DuplicateNameResponseDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.dto.SigninRequestDto;
import com.brick.demo.auth.dto.SigninResponseDto;
import com.brick.demo.auth.dto.TokenDto;
import com.brick.demo.auth.dto.UserPatchRequestDto;
import com.brick.demo.auth.dto.UserResponseDto;
import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.jwt.AccessToken;
import com.brick.demo.auth.jwt.RefreshToken;
import com.brick.demo.auth.jwt.TokenProvider;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.auth.repository.TokenManager;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {

	private final AccountManager accountManager;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final TokenProvider tokenProvider;
	private final TokenManager<RefreshToken> refreshTokenManager;
	private final TokenManager<AccessToken> accessTokenTokenManager;
	private final SecurityContextLogoutHandler logoutHandler;

	@Autowired
	public AuthService(AccountManager accountManager, PasswordEncoder passwordEncoder,
			AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider,
			TokenManager<RefreshToken> refreshTokenManager,
			TokenManager<AccessToken> accessTokenTokenManager,
			SecurityContextLogoutHandler logoutHandler) {
		this.accountManager = accountManager;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.tokenProvider = tokenProvider;
		this.refreshTokenManager = refreshTokenManager;
		this.accessTokenTokenManager = accessTokenTokenManager;
		this.logoutHandler = logoutHandler;
	}

	@Transactional(readOnly = true)
	public Optional<UserResponseDto> getAccountDetail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		final String writerName = userDetails.getName();
		final Optional<Account> accountOptional = accountManager.getAccountByName(writerName);
		if (accountOptional.isEmpty()) {
			throw new CustomException(ErrorDetails.E001);
		}
		final Account account = accountOptional.get();
		return Optional.of(new UserResponseDto(account));
	}

	@Transactional(readOnly = true)
	public DuplicateEmailResponseDto isDuplicatedEmail(DuplicateEmailRequestDto dto) {
		Optional<Account> account = accountManager.getAccountByEmail(dto.getEmail());
		if (account.isEmpty()) {
			return new DuplicateEmailResponseDto(false);
		}
		return new DuplicateEmailResponseDto(true);
	}


	@Transactional(readOnly = true)
	public DuplicateNameResponseDto isDuplicatedName(DuplicateNameRequestDto dto) {
		Optional<Account> account = accountManager.getAccountByName(dto.getName());
		if (account.isEmpty()) {
			return new DuplicateNameResponseDto(false);
		}
		return new DuplicateNameResponseDto(true);
	}

	@Transactional
	public void createAccount(SignUpRequestDto dto) {
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		Account account = dto.toEntity(passwordEncoder);
		accountManager.save(account);
	}

	@Transactional
	public UserResponseDto updateAccount(UserPatchRequestDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		final Optional<Account> accountOptional = accountManager.getAccountByEmail(email);
		if (accountOptional.isEmpty()) {
			throw new CustomException(ErrorDetails.E001);
		}
		final Account account = accountOptional.get();

		final LocalDate birthday =
				(dto.getBirthday() != null) ? dto.getBirthday() : account.getBirthday();
		final String introduce =
				(dto.getIntroduce() != null) ? dto.getIntroduce() : account.getIntroduce();
		final String profileImageUrl = (dto.getProfileImageUrl() != null) ? dto.getProfileImageUrl()
				: account.getProfileImageUrl();
		account.update(birthday, introduce, profileImageUrl);
		accountManager.save(account);
		return new UserResponseDto(account);
	}

	@Transactional
	public void deleteAccount(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		final Optional<Account> accountOptional = accountManager.getAccountByEmail(email);
		if (accountOptional.isEmpty()) {
			throw new CustomException(ErrorDetails.E001);
		}
		final Account account = accountOptional.get();
		account.softDelete();
		accountManager.save(account);

		deleteTokens(request, response); //로그아웃
	}

	public SigninResponseDto signin(SigninRequestDto dto) throws CustomException {
		UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();

		try {
			Authentication authentication = authenticationManagerBuilder.getObject()
					.authenticate(
							authenticationToken); //authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 실행됨.

			//이미 로그인 했었는지 검사(재로그인, 토큰 재발급 방지)
			Optional<AccessToken> existingAccessToken = accessTokenTokenManager.findByKey(dto.getEmail());
			if (existingAccessToken.isPresent()) {
				throw new CustomException(ErrorDetails.E004);
			}

			//탈퇴한 회원인지 검사
			final Optional<Account> accountOptional = accountManager.getAccountByEmail(dto.getEmail());
			if (accountOptional.isPresent() && accountOptional.get().getDeletedAt() != null) {
				throw new CustomException(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다");
			}

			TokenDto tokenDto = tokenProvider.generateToken(authentication);
			AccessToken accessToken = new AccessToken(authentication.getName(),
					tokenDto.getAccessToken());
			accessTokenTokenManager.save(accessToken);
			RefreshToken refreshToken = new RefreshToken(authentication.getName(),
					tokenDto.getRefreshToken());
			refreshTokenManager.save(refreshToken);

			return new SigninResponseDto(tokenDto.getGrantType(), tokenDto.getAccessToken(),
					tokenDto.getAccessTokenExpiresIn());
		} catch (AuthenticationException e) {
			throw new CustomException(ErrorDetails.E002);
		}
	}

	public ResponseEntity<Void> signout(HttpServletRequest request, HttpServletResponse response) {
		deleteTokens(request, response);
		return ResponseEntity.ok().build();
	}


	private void deleteTokens(HttpServletRequest request, HttpServletResponse response) {
		//TODO: access token, refresh token에 expired time을 추가해서 만료하는 방식으로 수정해야함
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();

		//이미 로그아웃 했었는지 검사(재로그인, 토큰 재발급 방지)
		Optional<AccessToken> existingAccessToken = accessTokenTokenManager.findByKey(email);
		if (existingAccessToken.isEmpty()) {
			throw new CustomException(ErrorDetails.E005);
		}

		accessTokenTokenManager.deleteByKey(email);
		refreshTokenManager.deleteByKey(email);
		logoutHandler.logout(request, response, authentication);
	}
}
