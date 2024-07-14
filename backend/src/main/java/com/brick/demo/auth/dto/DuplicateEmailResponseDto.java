package com.brick.demo.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DuplicateEmailResponseDto {

  @NotEmpty
  private Boolean duplicateEmail;

  @Builder
  DuplicateEmailResponseDto(boolean duplicateEmail) {
    this.duplicateEmail = duplicateEmail;
  }
}
