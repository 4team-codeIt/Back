package com.brick.demo.social.repository;

import com.brick.demo.social.entity.Social;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {
  // GET /socials

  Page<Social> findAllByNameContainingIgnoreCaseOrderByCreatedAtDesc(
      final String name, final Pageable pageable);

  Page<Social> findAllByCanceledTrueOrderByCreatedAtDesc(final Pageable pageable);

  @Query(
      "SELECT s FROM Social s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY SIZE(s.participants) DESC")
  Page<Social> findAllByNameContainingIgnoreCaseOrderByPopularityDesc(
      @Param("name") final String name, final Pageable pageable);

  @Query(
      "SELECT s FROM Social s "
          + "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) "
          + "AND s.gatheringDate > :date AND SIZE(s.participants) < s.maxCount AND s.canceled IS NULL "
          + "ORDER BY s.createdAt DESC")
  Page<Social> findAllInProgress(
      @Param("name") final String name,
      @Param("date") final LocalDateTime date,
      final Pageable pageable);

  @Query(
      "SELECT s FROM Social s "
          + "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) "
          + "AND (s.gatheringDate <= :date OR SIZE(s.participants) >= s.maxCount) "
          + "ORDER BY s.createdAt DESC")
  Page<Social> findAllCompleted(
      @Param("name") final String name,
      @Param("date") final LocalDateTime date,
      final Pageable pageable);

  default Page<Social> findAllCanceled(final Pageable pageable) {
    return findAllByCanceledTrueOrderByCreatedAtDesc(pageable);
  }

  default Page<Social> findAllOrderByPopularity(final String name, final Pageable pageable) {
    return findAllByNameContainingIgnoreCaseOrderByPopularityDesc(name, pageable);
  }

  default Page<Social> findAllDefault(final String name, final Pageable pageable) {
    return findAllByNameContainingIgnoreCaseOrderByCreatedAtDesc(name, pageable);
  }

  // GET /socials with id list

  Page<Social> findAllByIdInOrderByCreatedAtDesc(final List<Long> ids, final Pageable pageable);

  @Query("SELECT s FROM Social s WHERE s.id IN :ids ORDER BY SIZE(s.participants) DESC")
  Page<Social> findAllByIdInOrderByPopularityDesc(final List<Long> ids, final Pageable pageable);

  @Query(
      "SELECT s FROM Social s "
          + "WHERE s.id IN :ids "
          + "AND s.gatheringDate > :date AND SIZE(s.participants) < s.maxCount AND s.canceled IS NULL "
          + "ORDER BY s.createdAt DESC")
  Page<Social> findPartInProgress(
      @Param("ids") final List<Long> ids,
      @Param("date") final LocalDateTime date,
      final Pageable pageable);

  @Query(
      "SELECT s FROM Social s "
          + "WHERE s.id IN :ids "
          + "AND (s.gatheringDate <= :date OR SIZE(s.participants) >= s.maxCount) "
          + "ORDER BY s.createdAt DESC")
  Page<Social> findPartCompleted(
      final List<Long> ids, final LocalDateTime date, final Pageable pageable);

  default Page<Social> findPartOrderByPopularity(final List<Long> ids, final Pageable pageable) {
    return findAllByIdInOrderByPopularityDesc(ids, pageable);
  }

  default Page<Social> findPartDefault(final List<Long> ids, final Pageable pageable) {
    return findAllByIdInOrderByCreatedAtDesc(ids, pageable);
  }

  // GET /socials/me

  Page<Social> findAllByOwnerEntityIdOrderByCreatedAtDesc(
      final Long ownerEntityId, final Pageable pageable);

  Page<Social> findAllByOwnerEntityIdAndGatheringDateAfterOrderByCreatedAtDesc(
      final Long ownerEntityId, final LocalDateTime date, final Pageable pageable);

  Page<Social> findAllByOwnerEntityIdAndGatheringDateBeforeOrderByCreatedAtDesc(
      final Long ownerEntityId, final LocalDateTime date, final Pageable pageable);

  @Query(
      "SELECT s FROM Social s WHERE s.owner.entityId = :ownerEntityId ORDER BY SIZE(s.participants) DESC")
  Page<Social> findAllByOwnerEntityIdOrderByPopularityDesc(
      final Long ownerEntityId, final Pageable pageable);

  default Page<Social> findMineInProgress(
      final Long ownerEntityId, final LocalDateTime date, final Pageable pageable) {
    return findAllByOwnerEntityIdAndGatheringDateAfterOrderByCreatedAtDesc(
        ownerEntityId, date, pageable);
  }

  default Page<Social> findMineCompleted(
      final Long ownerEntityId, final LocalDateTime date, final Pageable pageable) {
    return findAllByOwnerEntityIdAndGatheringDateBeforeOrderByCreatedAtDesc(
        ownerEntityId, date, pageable);
  }

  default Page<Social> findMineOrderByPopularity(
      final Long ownerEntityId, final Pageable pageable) {
    return findAllByOwnerEntityIdOrderByPopularityDesc(ownerEntityId, pageable);
  }

  default Page<Social> findMineDefault(final Long ownerEntityId, final Pageable pageable) {
    return findAllByOwnerEntityIdOrderByCreatedAtDesc(ownerEntityId, pageable);
  }
}
