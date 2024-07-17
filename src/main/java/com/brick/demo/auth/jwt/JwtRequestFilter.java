package com.brick.demo.auth.jwt;

import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";
  private final TokenProvider tokenProvider;
  private final AntPathMatcher pathMatcher;
  private final List<String> excludeUrls;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return excludeUrls.stream().anyMatch(url -> pathMatcher.match(url, request.getServletPath()));
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException, IOException {
    try {
      String jwt = resolveToken(request);
      String email = null;

    if (jwt == null) {
      throw new CustomException(ErrorDetails.E003);
    }

      if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }

      chain.doFilter(request, response);
    } catch (CustomException ex) {
      setErrorResponse(response, ex);
    }
  }


  // Request Header 에서 토큰 정보를 꺼내오기
  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(BEARER_PREFIX.length());
    }
    return null;
  }


  private void setErrorResponse(HttpServletResponse response, CustomException ex) throws IOException {
    response.setStatus(ex.getHttpStatus().value());
    response.setContentType("application/json; charset=UTF-8");
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("code", ex.getCode());
    errorResponse.put("message", ex.getMessage().toString());
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writeValueAsString(errorResponse);
    response.getWriter().write(jsonResponse);
  }
}
