package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DuplicateEmailResponseDto {

  @NotEmpty
  private final Boolean duplicateEmail;

  @JsonCreator
  public DuplicateEmailResponseDto(Boolean duplicateEmail) {
    this.duplicateEmail = duplicateEmail;
  }
}
