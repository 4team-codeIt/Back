package com.brick.demo.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@PropertySource("classpath:application-private.yml")
public class CorsConfig {

  private final String frontDevIp;
  private final String frontProdIp;
  private final String frontLocalIp;

  public CorsConfig(
      @Value("${front-dev-ip}") String frontDevIp,
      @Value("${front-prod-ip}") String frontProdIp,
      @Value("${front-local-ip}") String frontLocalIp) {
    this.frontDevIp = frontDevIp;
    this.frontProdIp = frontProdIp;
    this.frontLocalIp = frontLocalIp;
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);

    List<String> allowedOrigins = Arrays.asList(frontDevIp, frontProdIp, frontLocalIp);
    allowedOrigins.forEach(config::addAllowedOrigin);

    config.addAllowedHeader("*");
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));

    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
