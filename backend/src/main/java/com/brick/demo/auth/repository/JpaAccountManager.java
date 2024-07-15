package com.brick.demo.auth.repository;

import com.brick.demo.auth.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@NoArgsConstructor
public class JpaAccountManager extends AbstractAccountManager {

  private EntityManager entityManager;

  @PersistenceContext
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Optional<Account> getAccountByEmail(String email) {
    try {
      Account account = entityManager.createQuery("SELECT a FROM Account a WHERE a.email = :email",
              Account.class)
          .setParameter("email", email)
          .getSingleResult();
      return Optional.of(account);
    } catch (NoResultException e) {
      return null; // 계정을 찾지 못한 경우 null을 반환
    }
  }

  @Override
  @Transactional
  public Account save(Account account) {
    entityManager.persist(account);
    return account;
  }

  @Override
  @Transactional
  public void update(Account account) {
    entityManager.merge(account);
  }
}
