package com.brick.demo.social.repository;

import com.brick.demo.social.entity.SocialDetail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialDetailRepository extends JpaRepository<SocialDetail, Long> {
  Optional<SocialDetail> findBySocialId(final Long id);
}
