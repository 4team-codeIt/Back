package com.brick.demo.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorDetails {
  E001("USER_NOT_FOUND", "해당하는 이메일로 가입된 유저를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  E002("INVALID_CREDENTIALS", "잘못된 이메일 또는 비밀번호입니다", HttpStatus.UNAUTHORIZED),
  E003("UNAUTHORIZED", "Authorization 헤더가 필요합니다", HttpStatus.UNAUTHORIZED),
  E004("UNAUTHORIZED", "로그인한 상태에서 로그인 요청을 보낼 수 없습니다", HttpStatus.UNAUTHORIZED),
  E005("UNAUTHORIZED", "로그아웃한 상태에서 로그아웃 요청을 보낼 수 없습니다", HttpStatus.UNAUTHORIZED);

  private final String code;
  private final String message;
  private final HttpStatus status;

  ErrorDetails(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
