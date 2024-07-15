package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UserResponseDto {

  private String email;
  private String name;

  @JsonCreator
  public UserResponseDto(String email, String name) {
    this.email = email;
    this.name = name;
  }
}
