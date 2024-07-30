package com.brick.demo.common.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class CommonPaginationRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements
		CommonPaginationRepository<T, ID> {

	private final EntityManager entityManager;

	public CommonPaginationRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	private String buildConditionString(Map<String, Object> conditions) {
		StringJoiner conditionString = new StringJoiner(" AND ");
		conditions.forEach((key, value) -> {
			String paramName = key.replace(".", "_"); // 점을 언더스코어로 변경
			conditionString.add("e." + key + " = :" + paramName); // : 는 named parameter
		});
		return conditionString.toString();
	}

	private void setQueryParameters(TypedQuery<T> query, Map<String, Object> conditions,
			Object cursor, String cursorName) {
		if (cursor != null) {
			query.setParameter(cursorName, cursor);
		}
		conditions.forEach((key, value) -> {
			String paramName = key.replace(".", "_");
			query.setParameter(paramName, value);
		});
	}

	private List<T> executeQuery(String jpql, Map<String, Object> conditions, Object cursor,
			String cursorName, Pageable pageable) {
		TypedQuery<T> query = entityManager.createQuery(jpql, getDomainClass())
				.setMaxResults(pageable.getPageSize() + 1); // 하나 더 읽기

		setQueryParameters(query, conditions, cursor, cursorName);

		return query.getResultList();
	}

	@Override
	//sortBy는 컬럼을 넣어야함.
	public List<T> findByCursorAndOrderByField(ID cursor, Pageable pageable,
			Map<String, Object> conditions, String sortBy, boolean asc) {
		String conditionString =
				CollectionUtils.isEmpty(conditions) ? "" : " AND " + buildConditionString(conditions);
		String cursorCondition = cursor == null ? "" : " AND e.id > :cursor";
		String orderBy = sortBy + (asc ? " ASC" : " DESC");

		String jpql =
				"SELECT e FROM " + getDomainClass().getSimpleName() + " e WHERE (1=1) " + conditionString
						+ cursorCondition + " ORDER BY e." + orderBy;

		return executeQuery(jpql, conditions, cursor, "cursor", pageable);
	}

	@Override
	public List<T> findByCursorAndOrderByCreatedAtDesc(LocalDateTime cursor, Pageable pageable,
			Map<String, Object> conditions) {
		String conditionString =
				CollectionUtils.isEmpty(conditions) ? "" : " AND " + buildConditionString(conditions);
		String cursorCondition = cursor == null ? "" : " AND e.createdAt < :cursor";
		String jpql =
				"SELECT e FROM " + getDomainClass().getSimpleName() + " e WHERE (1=1) " + conditionString
						+ cursorCondition + " ORDER BY e.createdAt DESC";

		return executeQuery(jpql, conditions, cursor, "cursor", pageable);
	}
}
