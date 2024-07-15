package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DuplicateEmailRequestDto {

  @Email
  @NotEmpty
  private final String email;

  @JsonCreator
  public DuplicateEmailRequestDto(String email) {
    this.email = email;
  }
}
