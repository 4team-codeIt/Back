package com.brick.demo.auth.repository;

import com.brick.demo.auth.jwt.AccessToken;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class InMemoryAccessTokenManager implements TokenManager<AccessToken> {

  private final Map<String, AccessToken> accessTokenStore = new HashMap<>();

  @Override
  public Optional<AccessToken> findByKey(String key) {
    log.info("현재 accessTokenStore: ");
    accessTokenStore.forEach((e, rt) ->
        log.info("email: {}, AccessToken: {}", e, rt.getValue())
    );
    return Optional.ofNullable(accessTokenStore.get(key));
  }

  @Override
  public AccessToken save(AccessToken accessToken) {
    accessTokenStore.put(accessToken.getKey(), accessToken);
    log.info("현재 accessTokenStore: ");
    accessTokenStore.forEach((e, rt) ->
        log.info("email: {}, AccessToken: {}", e, rt.getValue())
    );
    return accessToken;
  }

  @Override
  public void deleteByKey(String key) {
    accessTokenStore.remove(key);
    log.info("현재 accessTokenStore: ");
    accessTokenStore.forEach((e, rt) ->
        log.info("email: {}, AccessToken: {}", e, rt.getValue())
    );
  }

}
