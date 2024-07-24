package com.brick.demo.social.repository;

import com.brick.demo.social.entity.Social;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
  List<Social> findAllByOrderByGatheringDateDesc();

  List<Social> findAllByGatheringDateAfter(LocalDateTime date);

  List<Social> findByGatheringDateBefore(LocalDateTime date);
}
