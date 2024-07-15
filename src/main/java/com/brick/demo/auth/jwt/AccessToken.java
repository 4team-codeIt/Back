package com.brick.demo.auth.jwt;


import lombok.Getter;

@Getter
public class AccessToken {

  private final String key;
  private final String value;

  public AccessToken(String key, String value) {
    this.key = key;
    this.value = value;
  }

}