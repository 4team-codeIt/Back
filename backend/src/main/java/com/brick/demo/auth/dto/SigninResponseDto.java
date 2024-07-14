package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class SigninResponseDto {

  private final String token;

  @JsonCreator
  public SigninResponseDto(String token) {
    this.token = token;
  }
}
