package com.brick.demo.social.repository;

import com.brick.demo.social.entity.Qna;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnaRepository extends JpaRepository<Qna, Long> {

	@Query("SELECT q FROM Qna q WHERE q.social.id = :socialId AND (:cursor IS NULL OR q.id > :cursor) ORDER BY q.id ASC")
	List<Qna> findBySocialIdWithCursor(@Param("socialId") Long socialId, @Param("cursor") Long cursor,
			Pageable pageable);

	@Query("SELECT q FROM Qna q WHERE q.social.id = :socialId AND (:cursor IS NULL OR q.createdAt < :cursor) ORDER BY q.createdAt DESC")
	List<Qna> findBySocialIdWithDateCursor(@Param("socialId") Long socialId,
			@Param("cursor") LocalDateTime cursor, Pageable pageable);

}
