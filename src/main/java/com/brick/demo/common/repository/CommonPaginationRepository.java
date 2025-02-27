package com.brick.demo.common.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonPaginationRepository<T, ID> extends JpaRepository<T, ID> {

	List<T> findByCursorAndOrderByField(ID cursor, int limit, Map<String, Object> conditions,
			String sortBy, boolean asc);

	List<T> findByCursorAndOrderByCreatedAtDesc(LocalDateTime cursor, int limit,
			Map<String, Object> conditions);
}