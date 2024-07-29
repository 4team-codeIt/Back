package com.brick.demo.config;

import com.brick.demo.auth.jwt.TokenProvider;
import com.brick.demo.security.JwtRequestFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private final CorsFilter corsFilter;
  private final TokenProvider tokenProvider;
  private final List<String> excludeUrlsJwtfilter =
      List.of(
          "/h2-console/**",
          "/swagger-ui/**",
          "/v3/api-docs/**",
          "/auth/signup",
          "/auth/signin",
          "/auth/users/duplicate-email",
          "/auth/users/duplicate-name",
          "/socials",
          "/socials/*/details",
          "/socials/*/qnas",
          "/socials/*/qnas/*",
          "/socials/*/qnas/*/comments",
          "/socials/*/qnas/*/comments/*");
  private final List<String> excludeUrlsCsrf =
      List.of(
          "/auth/users",
          "/socials",
          "/socials/*/qnas",
          "/socials/*/qnas/*",
          "/socials/*/qnas/*/comments",
          "/socials/*/qnas/*/comments/*");

  public WebSecurityConfig(CorsFilter corsFilter, TokenProvider tokenProvider) {
    this.corsFilter = corsFilter;
    this.tokenProvider = tokenProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityContextLogoutHandler securityContextLogoutHandler() {
    return new SecurityContextLogoutHandler();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize -> {
              excludeUrlsJwtfilter.forEach(url -> authorize.requestMatchers(url).permitAll());
              authorize.anyRequest().authenticated();
            })
        .formLogin(form -> form.disable())
        .csrf(
            csrf -> {
              excludeUrlsJwtfilter.forEach(csrf::ignoringRequestMatchers);
              excludeUrlsCsrf.forEach(csrf::ignoringRequestMatchers);
            })
        .headers(
            headers ->
                headers.frameOptions(frameOptions -> frameOptions.sameOrigin()) // 동일 출처에서 프레임을 허용
            )
        .logout((logout) -> logout.logoutUrl("/auth/signout"));
    http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            new JwtRequestFilter(tokenProvider, new AntPathMatcher(), excludeUrlsJwtfilter),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
