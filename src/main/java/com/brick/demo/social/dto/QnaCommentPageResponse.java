package com.brick.demo.social.dto;

import com.brick.demo.social.entity.QnaComment;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public record QnaCommentPageResponse(
		@Schema(description = "Qna 댓글 전체 개수", requiredMode = RequiredMode.REQUIRED)
		Long totalElement,

		@Schema(description = "Qna 댓글 전체 페이지", requiredMode = RequiredMode.REQUIRED)
		Integer totalPages,

		@Schema(description = "Qna 댓글 현재 페이지", requiredMode = RequiredMode.REQUIRED)
		Integer currentPage,

		@Schema(description = "Qna 댓글 목록", requiredMode = RequiredMode.REQUIRED)
		@Valid List<QnaCommentResponse> socials
) {

	public static QnaCommentPageResponse from(Page<QnaComment> page) {
		return new QnaCommentPageResponse(
				page.getTotalElements(),
				page.getTotalPages(),
				page.getNumber(),
				page.stream().map(QnaCommentResponse::new)
						.collect(Collectors.toList()));
	}
}
