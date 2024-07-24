package com.brick.demo.config;


import com.brick.demo.auth.jwt.AccessToken;
import com.brick.demo.auth.jwt.RefreshToken;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.auth.repository.InMemoryAccessTokenManager;
import com.brick.demo.auth.repository.InMemoryRefreshTokenManager;
import com.brick.demo.auth.repository.JpaAccountManager;
import com.brick.demo.auth.repository.JpaRepositoryAccountManager;
import com.brick.demo.auth.repository.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = "com.brick.demo.auth.entity")
@EnableTransactionManagement
public class ApplicationConfig {

  private final AccountRepository accountRepository;

  @Autowired
  public ApplicationConfig(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Bean
  public AccountManager accountManager() {
    return new JpaRepositoryAccountManager(accountRepository);
//    return new JpaAccountManager();
  }

  @Bean
  public TokenManager<AccessToken> accessTokenTokenManager() {
    return new InMemoryAccessTokenManager();
  }
  
  @Bean
  public TokenManager<RefreshToken> refreshTokenManager() {
    return new InMemoryRefreshTokenManager();
  }
}
