package com.brick.demo.auth.repository;

import com.brick.demo.auth.entity.Account;

public interface AccountManager {

  public String getImplInfo();

  public Account getAccountByEmail(String email);

  public Account save(Account account);

  public void update(Account account);

}
