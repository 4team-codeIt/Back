package com.brick.demo.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";
  private final TokenProvider tokenProvider;
  private final AntPathMatcher pathMatcher;
  private final List<String> excludeUrls;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException, IOException {
    String jwt = resolveToken(request);
    String email = null;

//    if (jwt == null) {
//      setErrorResponse(response, ErrorDetails.E003);
//    }

    if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
      Authentication authentication = tokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return excludeUrls.stream().anyMatch(url -> pathMatcher.match(url, request.getServletPath()));
  }

  // Request Header 에서 토큰 정보를 꺼내오기
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }

//  private void setErrorResponse(HttpServletResponse response, ErrorDetails errorDetails) {
//    response.setStatus(errorDetails.getStatus().value());
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    Map<String, String> errorResponse = new HashMap<>();
//    errorResponse.put("code", errorDetails.getCode());
//    errorResponse.put("message", errorDetails.getMessage());
//    ObjectMapper objectMapper = new ObjectMapper();
//    try {
//      String jsonResponse = objectMapper.writeValueAsString(errorResponse);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
}
