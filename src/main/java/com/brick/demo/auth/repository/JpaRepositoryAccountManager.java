package com.brick.demo.auth.repository;

import com.brick.demo.auth.entity.Account;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

// 원래 JpaRepository 상속한 repository 인터페이스 만들면 이부분 안해도 됨.
// 근데 AuthService가 accountManger 인터페이스 사용해서 코드를 작성해서,
// AuthService 수정 안 하려고 일부러 만듬.
@Repository
public class JpaRepositoryAccountManager extends AbstractAccountManager{

  private final AccountRepository accountRepository;

  @Autowired
  public JpaRepositoryAccountManager(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Optional<Account> getAccountByEmail(String email) {
    return accountRepository.findByEmail(email);
  }

  @Override
  @Transactional
  public Account save(Account account) {
    return accountRepository.save(account);
  }

  @Override
  @Transactional
  public void update(Account account) {
    accountRepository.save(account);
  }
}
