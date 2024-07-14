package com.brick.demo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuplicateEmailRequestDto {

  @Email
  @NotEmpty
  private String email;

  public DuplicateEmailRequestDto(String email) {
    this.email = email;
  }
}
