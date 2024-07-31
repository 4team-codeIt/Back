package com.brick.demo.social.repository;

import com.brick.demo.common.repository.CommonPaginationRepository;
import com.brick.demo.social.entity.Qna;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnaRepository extends CommonPaginationRepository<Qna, Long> {

}
