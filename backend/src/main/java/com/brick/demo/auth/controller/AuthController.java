package com.brick.demo.auth.controller;

import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.DuplicateEmailResponseDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  //TODO: JWT 사용해서 이메일 가져오도록
  @GetMapping(value = "/user/{email}")
  public Account accountDetails(@PathVariable String email) {
    return authService.findAccountByEmail(email);
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public void createAccount(@Valid @RequestBody SignUpRequestDto dto) {
    authService.createAccount(dto);
  }

  @PostMapping("/users/duplicate-email")
  public ResponseEntity<DuplicateEmailResponseDto> duplicateEmail(
      @RequestBody DuplicateEmailRequestDto dto) {
    boolean isDuplicate = authService.isExistedEmail(dto);
    DuplicateEmailResponseDto response = DuplicateEmailResponseDto.builder().
        duplicateEmail(isDuplicate).
        build();
    return ResponseEntity.ok(response);
  }
}

