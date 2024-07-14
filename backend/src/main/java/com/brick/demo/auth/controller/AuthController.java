package com.brick.demo.auth.controller;

import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.DuplicateEmailResponseDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.dto.SigninRequestDto;
import com.brick.demo.auth.dto.SigninResponseDto;
import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AuthService authService;


  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @GetMapping(value = "/user")
  public Account accountDetails(HttpServletRequest request) {
    return authService.findAccountByEmail(request);
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public void createAccount(@Valid @RequestBody SignUpRequestDto dto) {
    authService.createAccount(dto);
  }

  @PostMapping("/signin")
  public SigninResponseDto createAuthenticationToken(
      @RequestBody SigninRequestDto dto, HttpServletResponse response) {
    return authService.signin(dto, response);
  }

  @PostMapping("/signout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    return authService.signout(response);
  }

  @PostMapping("/users/duplicate-email")
  public DuplicateEmailResponseDto duplicateEmail(
      @RequestBody DuplicateEmailRequestDto dto) {
    return authService.isDuplicatedEmail(dto);
  }
}

