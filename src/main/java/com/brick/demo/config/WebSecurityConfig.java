package com.brick.demo.config;

import com.brick.demo.auth.jwt.JwtRequestFilter;
import com.brick.demo.auth.jwt.TokenProvider;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private final TokenProvider tokenProvider;
  private final List<String> excludeUrls = List.of("/auth/signup", "/auth/signin", "/swagger-ui/**", "/v3/api-docs/**");

  public WebSecurityConfig(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

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
            .requestMatchers("/auth/users/duplicate-email").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/v3/api-docs/**").permitAll()
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
            .ignoringRequestMatchers("/auth/users/duplicate-email")
            .ignoringRequestMatchers("/swagger-ui/**")
            .ignoringRequestMatchers("/v3/api-docs/**")

        )
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()) // 동일 출처에서 프레임을 허용
        );

    http.addFilterBefore(new JwtRequestFilter(tokenProvider, new AntPathMatcher(), excludeUrls),
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
