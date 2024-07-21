package com.brick.demo.auth.service;

import static com.brick.demo.security.JwtRequestFilter.BEARER_PREFIX;

import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.DuplicateEmailResponseDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.dto.SigninRequestDto;
import com.brick.demo.auth.dto.SigninResponseDto;
import com.brick.demo.auth.dto.TokenDto;
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
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
      TokenManager<AccessToken> accessTokenTokenManager, SecurityContextLogoutHandler logoutHandler) {
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
    return Optional.of(new UserResponseDto(userDetails.getEmail(), userDetails.getName()));
  }

  @Transactional(readOnly = true)
  public DuplicateEmailResponseDto isDuplicatedEmail(DuplicateEmailRequestDto dto) {
    Optional<Account> account =  accountManager.getAccountByEmail(dto.getEmail());
    if(account.isEmpty()) {
      return new DuplicateEmailResponseDto(false);
    }
    return new DuplicateEmailResponseDto(true);
  }

  public void createAccount(SignUpRequestDto dto) {
    String encodedPassword = passwordEncoder.encode(dto.getPassword());
    Account account = dto.toEntity(passwordEncoder);
    accountManager.save(account);
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

    return ResponseEntity.ok().build();
  }
}
