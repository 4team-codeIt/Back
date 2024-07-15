package com.brick.demo.auth.repository;

import com.brick.demo.auth.jwt.RefreshToken;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class InMemoryRefreshTokenManager implements TokenManager<RefreshToken> {

  private final Map<String, RefreshToken> refreshTokenStore = new HashMap<>();

  @Override
  public Optional<RefreshToken> findByKey(String key) {
    return Optional.ofNullable(refreshTokenStore.get(key));
  }

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    refreshTokenStore.put(refreshToken.getKey(), refreshToken);
    log.info("현재 refreshTokenStore: ");
    refreshTokenStore.forEach((e, rt) ->
        log.info("email: {}, RefreshToken: {}", e, rt.getValue())
    );
    return refreshToken;
  }

  @Override
  public void deleteByKey(String key) {
    refreshTokenStore.remove(key);
    log.info("현재 refreshTokenStore: ");
    refreshTokenStore.forEach((e, rt) ->
        log.info("email: {}, RefreshToken: {}", e, rt.getValue())
    );
  }

}
