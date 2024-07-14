package com.brick.demo.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DuplicateEmailResponseDto {

  @NotEmpty
  private Boolean duplicateEmail;

  public DuplicateEmailResponseDto(Boolean duplicateEmail) {
    this.duplicateEmail = duplicateEmail;
  }
}
