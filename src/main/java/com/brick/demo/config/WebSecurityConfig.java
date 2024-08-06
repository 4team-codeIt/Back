package com.brick.demo.config;

import com.brick.demo.auth.jwt.TokenProvider;
import com.brick.demo.security.JwtRequestFilter;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
	private final List<Map<String, String>> excludeUrlsJwtfilter =
			List.of(
					Map.of("url", "/h2-console/**", "method", "ALL"),
					Map.of("url", "/swagger-ui/**", "method", "ALL"),
					Map.of("url", "/v3/api-docs/**", "method", "ALL"),
					Map.of("url", "/auth/signup", "method", "POST"),
					Map.of("url", "/auth/signin", "method", "POST"),
					Map.of("url", "/auth/users/duplicate-email", "method", "POST"),
					Map.of("url", "/auth/users/duplicate-name", "method", "POST"),
					Map.of("url", "/socials", "method", "GET"),
					Map.of("url", "/socials/*/details", "method", "GET"),
					Map.of("url", "/socials/*/qnas", "method", "GET"),
					Map.of("url", "/socials/*/qnas/*", "method", "GET"),
					Map.of("url", "/socials/*/qnas/*/comments", "method", "GET"),
					Map.of("url", "/socials/*/qnas/*/comments/*", "method", "GET"));

	// POST, PATCH, PUT, DELETE 메서드에서 요구됨. 해당 메서드로 요청 시 여기에 허용 안해주면 jwt 필터까지 가지도 못하고 403 에러 발생
	private final List<String> excludeUrlsCsrf =
			List.of(
					"/auth/users",
					"/auth/users/images",
					"/socials",
					"/socials/*",
					"/socials/*/participants",
					"/socials/images",
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
							// method가 all이면 모든 요청에 대해, 아니면 각 요청에 대해서만 인증 열어둠.
							excludeUrlsJwtfilter.forEach(
									entry -> {
										if ("ALL".equalsIgnoreCase(entry.get("method"))) {
											authorize.requestMatchers(entry.get("url")).permitAll();
										} else {
											authorize
													.requestMatchers(
															HttpMethod.valueOf(entry.get("method")), entry.get("url"))
													.permitAll();
										}
									});
							authorize.anyRequest().permitAll();
						})
				.formLogin(form -> form.disable())
				.csrf(
						csrf -> {
							excludeUrlsJwtfilter.forEach(entry -> csrf.ignoringRequestMatchers(entry.get("url")));
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
