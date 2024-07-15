package com.brick.demo.auth.jwt;


import lombok.Getter;

@Getter
public class RefreshToken {

  private String key;
  private String value;

  public RefreshToken(String name, String refreshToken) {
    this.key = name;
    this.value = refreshToken;
  }

  public RefreshToken updateValue(String token) {
    this.value = token;
    return this;
  }
}