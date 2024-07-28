package com.brick.demo.fixture;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.entity.OAuthProvider;

public class AccountFixture {
  private static final String NAME = "tester";
  private static final String EMAIL = "test@example.com";
  private static final String PASSWORD = "password";

  public static final Account ACCOUNT = new Account(NAME, EMAIL, PASSWORD, OAuthProvider.GOOGLE);
}
