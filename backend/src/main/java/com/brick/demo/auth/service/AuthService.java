package com.brick.demo.auth.service;

import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.DuplicateEmailResponseDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.dto.SigninRequestDto;
import com.brick.demo.auth.dto.SigninResponseDto;
import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements UserDetailsService {

  private static final String COOKIE_NAME = "email";
  private final AccountManager accountManager;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthService(AccountManager accountManager, PasswordEncoder passwordEncoder) {
    this.accountManager = accountManager;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(readOnly = true)
  public Account findAccountByEmail(HttpServletRequest request) throws CustomException {
    String email = extractEmailFromCookie(request);
    Account account = accountManager.getAccountByEmail(email);
    if (account == null) {
      throw new CustomException(ErrorDetails.USER_NOT_FOUND_BY_EMAIL);
    }
    return account;
  }

  @Transactional(readOnly = true)
  public DuplicateEmailResponseDto isDuplicatedEmail(DuplicateEmailRequestDto dto) {
    boolean isDuplicate = accountManager.getAccountByEmail(dto.getEmail()) != null;
    DuplicateEmailResponseDto response = new DuplicateEmailResponseDto(isDuplicate);
    return response;
  }

  public void createAccount(SignUpRequestDto dto) {
    String encodedPassword = passwordEncoder.encode(dto.getPassword());
    Account account = Account.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .password(encodedPassword)
        .build();
    accountManager.save(account);
  }

  public SigninResponseDto signin(SigninRequestDto dto, HttpServletResponse response) {
    authenticate(dto);

    // 이메일을 쿠키에 저장
    Cookie cookie = new Cookie(COOKIE_NAME, dto.getEmail());
    cookie.setHttpOnly(true);
    cookie.setPath("/"); // 애플리케이션 전체에서 유효
    cookie.setMaxAge(10 * 60 * 60); // 쿠키의 유효 기간 설정 (초 단위)
    response.addCookie(cookie);

    return new SigninResponseDto(dto.getEmail());
  }

  public ResponseEntity<Void> signout(HttpServletResponse response) {
    // 이메일 쿠키를 무효화
    Cookie cookie = new Cookie(COOKIE_NAME, null);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0); // 쿠키를 즉시 삭제

    response.addCookie(cookie);

    return ResponseEntity.ok().build();
  }

  @Override
  public UserDetails loadUserByUsername(String email) {
    return loadUserByEmail(email);
  }

  public UserDetails loadUserByEmail(String email) {
    Account account = accountManager.getAccountByEmail(email);
    if (account == null) {
      throw new CustomException(ErrorDetails.INVALID_CREDENTIALS);
    }

    return User.builder()
        .username(account.getEmail())
        .password(account.getPassword())
        .build();
  }

  private void authenticate(SigninRequestDto dto) {
    UserDetails userDetails = loadUserByEmail(dto.getEmail());
    if (!passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
      throw new CustomException(ErrorDetails.INVALID_CREDENTIALS);
    }
  }

  private String extractEmailFromCookie(HttpServletRequest request) throws CustomException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (COOKIE_NAME.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    throw new CustomException(ErrorDetails.INVALID_CREDENTIALS);
  }
}
