package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DuplicateNameResponseDto {

  @NotEmpty
  private final Boolean duplicateName;

  @JsonCreator
  public DuplicateNameResponseDto(Boolean duplicateName) {
    this.duplicateName = duplicateName;
  }
}
