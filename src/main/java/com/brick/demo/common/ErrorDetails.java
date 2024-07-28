package com.brick.demo.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorDetails {
  E001("USER_NOT_FOUND", "해당하는 이메일로 가입된 유저를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  E002("INVALID_CREDENTIALS", "잘못된 이메일 또는 비밀번호입니다", HttpStatus.UNAUTHORIZED),
  E003("UNAUTHORIZED", "Authorization 헤더가 필요합니다", HttpStatus.UNAUTHORIZED),
  E004("UNAUTHORIZED", "로그인한 상태에서 로그인 요청을 보낼 수 없습니다", HttpStatus.UNAUTHORIZED),
  E005("UNAUTHORIZED", "로그아웃한 상태에서 로그아웃 요청을 보낼 수 없습니다", HttpStatus.UNAUTHORIZED),

  SOCIAL_NOT_FOUND("SOCIAL_NOT_FOUND", "아이디에 해당하는 모임을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  SOCIAL_FORBIDDEN("SOCIAL_FORBIDDEN", "수정 권한이 없는 모임입니다", HttpStatus.FORBIDDEN),

  SOCIAL_EXCEED_MAX_LIMIT(
      "SOCIAL_EXCEED_MAX_LIMIT", "최대 인원을 초과하여 더 이상 참여할 수 없습니다", HttpStatus.BAD_REQUEST),
  SOCIAL_ALREADY_JOINED("SOCIAL_ALREADY_JOINED", "이미 참여한 모임입니다", HttpStatus.BAD_REQUEST),
  SOCIAL_ALREADY_PASSED("SOCIAL_ALREADY_PASSED", "이미 지난 모임입니다", HttpStatus.BAD_REQUEST),
  SOCIAL_NOT_JOINED("SOCIAL_NOT_JOINED", "참여한 모임이 아닙니다", HttpStatus.BAD_REQUEST),
  SOCIAL_OWNER_LEAVE_FORBIDDEN(
      "SOCIAL_OWNER_LEAVE_FORBIDDEN", "모임의 주최자는 참여를 취소할 수 없습니다", HttpStatus.FORBIDDEN);

  private final String code;
  private final String message;
  private final HttpStatus status;

  ErrorDetails(String code, String message, HttpStatus status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}
