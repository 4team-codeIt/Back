package com.brick.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/h2-console/**").permitAll() // H2 콘솔에 대한 접근을 허용
            .requestMatchers("/auth/signup").permitAll()
            .requestMatchers("/auth/signin").permitAll()
            .requestMatchers("/auth/signout").permitAll()
            .requestMatchers("/auth/users/duplicate-email").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/signin")
            .permitAll()
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**") // H2 콘솔 CSRF 무시 설정
            .ignoringRequestMatchers("/auth/signup")
            .ignoringRequestMatchers("/auth/signin")
            .ignoringRequestMatchers("/auth/signout")
            .ignoringRequestMatchers("/auth/users/duplicate-email")

        )
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()) // 동일 출처에서 프레임을 허용
        );

    return http.build();
  }
}
