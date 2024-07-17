package com.brick.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    List<Server> servers = new ArrayList<>();
    servers.add(new Server().url("https://break-brick.kro.kr/"));
    servers.add(new Server().url("http://localhost:8080/"));

    return new OpenAPI()
        .components(new Components())
        .info(apiInfo())
        .servers(servers);
  }

  private Info apiInfo() {
    return new Info()
        .title("Brick API 명세")
        .description("잘못된 부분이나 오류 발생 시 바로 말씀해주세요.") // 문서 설명
        .version("0.0.1");
  }
}
