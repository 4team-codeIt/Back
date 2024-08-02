package com.brick.demo.social.repository;

import com.brick.demo.social.entity.Social;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
  // GET /socials

  Page<Social> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Page<Social> findAllByOrderByGatheringDateDesc(Pageable pageable); // no usage

  Page<Social> findAllByGatheringDateBeforeOrderByCreatedAtDesc(
      final LocalDateTime date, Pageable pageable);

  Page<Social> findAllByGatheringDateBeforeOrderByGatheringDateDesc(
      final LocalDateTime date, Pageable pageable); // no usage

  Page<Social> findAllByGatheringDateAfterOrderByCreatedAtDesc(
      final LocalDateTime date, Pageable pageable);

  Page<Social> findAllByGatheringDateAfterOrderByGatheringDateDesc(
      final LocalDateTime date, Pageable pageable); // no usage

  Page<Social> findAllByCanceledTrueOrderByCreatedAtDesc(Pageable pageable);

  @Query("SELECT s FROM Social s ORDER BY SIZE(s.participants) DESC")
  Page<Social> findAllByOrderByPopularityDesc(Pageable pageable);

  // GET /socials with id list

  Page<Social> findAllByIdInOrderByCreatedAtDesc(final List<Long> ids, Pageable pageable);

  Page<Social> findAllByIdInOrderByGatheringDateDesc(
      final List<Long> ids, Pageable pageable); // no usage

  Page<Social> findAllByIdInAndGatheringDateBeforeOrderByCreatedAtDesc(
      final List<Long> ids, final LocalDateTime date, Pageable pageable);

  Page<Social> findAllByIdInAndGatheringDateBeforeOrderByGatheringDateDesc( // no usage
      final List<Long> ids, final LocalDateTime date, Pageable pageable);

  Page<Social> findAllByIdInAndGatheringDateAfterOrderByCreatedAtDesc(
      final List<Long> ids, final LocalDateTime date, Pageable pageable);

  Page<Social> findAllByIdInAndGatheringDateAfterOrderByGatheringDateDesc( // no usage
      final List<Long> ids, final LocalDateTime date, Pageable pageable);

  @Query("SELECT s FROM Social s WHERE s.id IN :ids ORDER BY SIZE(s.participants) DESC")
  Page<Social> findAllByIdInOrderByPopularityDesc(final List<Long> ids, Pageable pageable);

  // GET /socials/me

  Page<Social> findAllByOwnerEntityIdOrderByCreatedAtDesc(
      final Long ownerEntityId, Pageable pageable);

  Page<Social> findAllByOwnerEntityIdAndGatheringDateAfterOrderByCreatedAtDesc(
      final Long ownerEntityId, final LocalDateTime date, Pageable pageable);

  Page<Social> findAllByOwnerEntityIdAndGatheringDateBeforeOrderByCreatedAtDesc(
      final Long ownerEntityId, final LocalDateTime date, Pageable pageable);

  @Query(
      "SELECT s FROM Social s WHERE s.owner.entityId = :ownerEntityId ORDER BY SIZE(s.participants) DESC")
  Page<Social> findAllByOwnerEntityIdOrderByPopularityDesc(
      final Long ownerEntityId, Pageable pageable);
}
