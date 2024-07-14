package com.brick.demo.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorDetails {
  USER_NOT_FOUND_BY_EMAIL("E001", "해당하는 이메일로 가입된 유저를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  INVALID_CREDENTIALS("E002", "잘못된 이메일 또는 비밀번호입니다", HttpStatus.UNAUTHORIZED);

  private final String code;
  private final String message;
  private final HttpStatus status;

  ErrorDetails(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
