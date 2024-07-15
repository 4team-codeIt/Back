package com.brick.demo.auth.service;

import static com.brick.demo.auth.jwt.JwtRequestFilter.BEARER_PREFIX;

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
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final TokenManager accessTokenTokenManager;

  @Autowired
  public AuthService(AccountManager accountManager, PasswordEncoder passwordEncoder,
      AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider,
      TokenManager<RefreshToken> refreshTokenManager,
      @Qualifier("accessTokenTokenManager") TokenManager accessTokenTokenManager) {
    this.accountManager = accountManager;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.tokenProvider = tokenProvider;
    this.refreshTokenManager = refreshTokenManager;
    this.accessTokenTokenManager = accessTokenTokenManager;
  }

  @Transactional(readOnly = true)
  public Optional<UserResponseDto> getAccountDetail(String authorizationHeader) {
    if (authorizationHeader.startsWith(BEARER_PREFIX)) {
      String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
      Authentication authentication = tokenProvider.getAuthentication(accessToken);
      String email = authentication.getName();
      Optional<Account> account = accountManager.getAccountByEmail(email);
      if (account != null) {
        return Optional.of(new UserResponseDto(account.get().getEmail(), account.get().getName()));
      }
      throw new CustomException(ErrorDetails.E001);
    }
    throw new CustomException(ErrorDetails.E003);
  }

  @Transactional(readOnly = true)
  public DuplicateEmailResponseDto isDuplicatedEmail(DuplicateEmailRequestDto dto) {
    boolean isDuplicate = accountManager.getAccountByEmail(dto.getEmail()) != null;
    DuplicateEmailResponseDto response = new DuplicateEmailResponseDto(isDuplicate);
    return response;
  }

  public void createAccount(SignUpRequestDto dto) {
    String encodedPassword = passwordEncoder.encode(dto.getPassword());
    Account account = dto.toEntity(passwordEncoder);
    accountManager.save(account);
  }

  public SigninResponseDto signin(SigninRequestDto dto) {
    UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();

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
  }

  public ResponseEntity<Void> signout(String authorizationHeader) {
    //TODO: access token, refresh token에 expired time을 추가해서 만료하는 방식으로 수정해야함
    if (authorizationHeader.startsWith(BEARER_PREFIX)) {
      String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());
      Authentication authentication = tokenProvider.getAuthentication(accessToken);
      String email = authentication.getName();

      //이미 로그아웃 했었는지 검사(재로그인, 토큰 재발급 방지)
      Optional<AccessToken> existingAccessToken = accessTokenTokenManager.findByKey(email);
      if (existingAccessToken.isEmpty()) {
        throw new CustomException(ErrorDetails.E005);
      }

      accessTokenTokenManager.deleteByKey(email);
      refreshTokenManager.deleteByKey(email);
      return ResponseEntity.ok().build();
    }
    throw new CustomException(ErrorDetails.E003);

  }
}
