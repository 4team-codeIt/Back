package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DuplicateNameRequestDto {

  @Email
  @NotEmpty
  private final String name;

  @JsonCreator
  public DuplicateNameRequestDto(String name) {
    this.name = name;
  }
}
