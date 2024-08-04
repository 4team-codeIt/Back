package com.brick.demo.social.dto;

import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.Social;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public record QnaPageResponse(
		@Schema(description = "Qna 전체 개수", requiredMode = RequiredMode.REQUIRED)
		Long totalElement,

		@Schema(description = "Qna 전체 페이지", requiredMode = RequiredMode.REQUIRED)
		Integer totalPages,

		@Schema(description = "Qna 현재 페이지", requiredMode = RequiredMode.REQUIRED)
		Integer currentPage,

		@Schema(description = "Qna 목록", requiredMode = RequiredMode.REQUIRED)
		@Valid List<QnaResponseDto> socials
) {

	public static QnaPageResponse from(Page<Qna> page) {
		return new QnaPageResponse(
				page.getTotalElements(),
				page.getTotalPages(),
				page.getNumber(),
				page.stream().map(qna -> new QnaResponseDto(qna, qna.getCommentCount()))
						.collect(Collectors.toList()));
	}
}
