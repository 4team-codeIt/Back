package com.brick.demo.auth.service;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final AccountManager accountManager;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return accountManager.getAccountByEmail(username)
        .map(this::createUserDetails)
        .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
  }

  // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
  private UserDetails createUserDetails(Account account) {
    Collection emptyAuthorities = Collections.emptyList();

    return new User(
        String.valueOf(account.getEmail()),
        account.getPassword(),
        emptyAuthorities
    );
  }
}
