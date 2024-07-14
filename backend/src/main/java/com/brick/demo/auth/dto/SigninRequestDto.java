package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class SigninRequestDto {

  @NotEmpty(message = "Email is required")
  private final String email;

  @NotEmpty(message = "Password is required")
  private final String password;


  @JsonCreator
  public SigninRequestDto(@JsonProperty("email") String email,
      @JsonProperty("password") String password) {
    this.email = email;
    this.password = password;
  }
}
