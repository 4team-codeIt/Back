package com.brick.demo.social.repository;

import com.brick.demo.social.entity.SocialParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialParticipantRepository extends JpaRepository<SocialParticipant, Long> {
  void deleteAllBySocialIdAndAccountEntityId(final Long socialId, final Long accountId);
}
