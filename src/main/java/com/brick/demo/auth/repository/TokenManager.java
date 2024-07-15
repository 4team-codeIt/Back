package com.brick.demo.auth.repository;

import java.util.Optional;

public interface TokenManager<T> {

  Optional<T> findByKey(String key);

  T save(T token);

  void deleteByKey(String key);
}