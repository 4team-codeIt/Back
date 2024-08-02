package com.brick.demo.social.repository;

import com.brick.demo.common.repository.CommonPaginationRepository;
import com.brick.demo.social.entity.Qna;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnaRepository extends CommonPaginationRepository<Qna, Long> {

	// socialId를 기준으로 deleted_at이 null인 Qna 조회
	Page<Qna> findBySocialIdAndDeletedAtIsNull(Long socialId, Pageable pageable);

	// qnaId를 기준으로 deleted_at이 null인 Qna 조회
	Optional<Qna> findByIdAndDeletedAtIsNull(Long qnaId);
}
