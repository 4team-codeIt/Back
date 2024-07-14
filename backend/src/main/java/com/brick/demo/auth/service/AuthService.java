package com.brick.demo.auth.service;

import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private AccountManager accountManager;

  @Autowired
  public AuthService(AccountManager accountManager) {
    this.accountManager = accountManager;
  }

  @Transactional(readOnly = true)
  public Account findAccountByEmail(String email) throws CustomException {
    Account account = accountManager.getAccountByEmail(email);
    if (account == null) {
      throw new CustomException(ErrorDetails.USER_NOT_FOUND_BY_EMAIL);
    }
    return account;
  }

  @Transactional(readOnly = true)
  public boolean isExistedEmail(DuplicateEmailRequestDto dto) {
    return accountManager.getAccountByEmail(dto.getEmail()) != null;
  }

  public void createAccount(SignUpRequestDto dto) {
    accountManager.save(dto.toEntity());
  }
}
