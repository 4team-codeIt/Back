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
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private final CorsFilter corsFilter;
  private final TokenProvider tokenProvider;
  private final List<String> excludeUrls = List.of("/auth/signup", "/auth/signin", "/auth/users/duplicate-email", "/swagger-ui/**", "/v3/api-docs/**");

  public WebSecurityConfig(CorsFilter corsFilter, TokenProvider tokenProvider) {
    this.corsFilter = corsFilter;
    this.tokenProvider = tokenProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> {
                excludeUrls.forEach(url -> authorize.requestMatchers(url).permitAll());
                authorize.requestMatchers("/h2-console/**").permitAll();
                authorize.anyRequest().authenticated();
            }
        )
        .formLogin(form -> form.disable())
        .csrf(csrf -> {
          excludeUrls.forEach(url -> csrf.ignoringRequestMatchers(url));
          csrf.ignoringRequestMatchers("/h2-console/**");
        })
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions.sameOrigin()) // 동일 출처에서 프레임을 허용
        );

    http
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtRequestFilter(tokenProvider, new AntPathMatcher(), excludeUrls),
        UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}