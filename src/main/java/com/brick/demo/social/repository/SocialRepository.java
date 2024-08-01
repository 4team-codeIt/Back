package com.brick.demo.social.repository;

import com.brick.demo.social.entity.Social;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
  List<Social> findAllByOrderByCreatedAtDesc();

  List<Social> findAllByOrderByGatheringDateDesc();

  List<Social> findAllByGatheringDateBeforeOrderByGatheringDateDesc(final LocalDateTime date);

  List<Social> findAllByGatheringDateBeforeOrderByCreatedAtDesc(final LocalDateTime date);

  List<Social> findAllByGatheringDateAfterOrderByGatheringDateDesc(final LocalDateTime date);

  List<Social> findAllByGatheringDateAfterOrderByCreatedAtDesc(final LocalDateTime date);

  List<Social> findAllByCanceledTrueOrderByCreatedAtDesc();

  List<Social> findAllByOwnerEntityIdOrderByCreatedAtDesc(final Long ownerEntityId);

  @Query("SELECT s FROM Social s ORDER BY SIZE(s.participants) DESC")
  List<Social> findAllByOrderByPopularityDesc();

  List<Social> findAllByIdInOrderByCreatedAtDesc(final List<Long> ids);

  List<Social> findAllByIdInOrderByGatheringDateDesc(final List<Long> ids);

  List<Social> findAllByIdInAndGatheringDateBeforeOrderByGatheringDateDesc(
      final List<Long> ids, final LocalDateTime date);

  List<Social> findAllByIdInAndGatheringDateBeforeOrderByCreatedAtDesc(
      final List<Long> ids, final LocalDateTime date);

  List<Social> findAllByIdInAndGatheringDateAfterOrderByGatheringDateDesc(
      final List<Long> ids, final LocalDateTime date);

  List<Social> findAllByIdInAndGatheringDateAfterOrderByCreatedAtDesc(
      final List<Long> ids, final LocalDateTime date);

  @Query("SELECT s FROM Social s WHERE s.id IN :ids ORDER BY SIZE(s.participants) DESC")
  List<Social> findAllByIdInOrderByPopularityDesc(final List<Long> ids);
}
