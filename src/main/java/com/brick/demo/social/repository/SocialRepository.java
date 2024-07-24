package com.brick.demo.social.repository;

import com.brick.demo.social.entity.Social;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
  @Query("SELECT s FROM Social s WHERE s.gatheringDate > :date")
  List<Social> findAllByGatheringDateAfter(@Param("date") LocalDateTime date);

  @Query("SELECT s FROM Social s WHERE s.gatheringDate <= :date")
  List<Social> findAllByGatheringDateBefore(@Param("date") LocalDateTime date);

  @Query("SELECT s FROM Social s ORDER BY s.gatheringDate DESC")
  List<Social> findAllOrderByGatheringDateDesc();

  @Query("SELECT s FROM Social s LEFT JOIN s.participants p GROUP BY s ORDER BY COUNT(p) DESC")
  List<Social> findAllOrderByPopularity();
}
