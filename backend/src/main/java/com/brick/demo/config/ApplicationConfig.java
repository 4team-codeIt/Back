package com.brick.demo.config;


import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.auth.repository.JpaAccountManager;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan("auth.entity")
@EnableTransactionManagement
public class ApplicationConfig {

  @Bean
  public AccountManager accountManager() {
    return new JpaAccountManager();
  }
}
