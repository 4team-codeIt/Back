package com.brick.demo.social.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class QnaCommentResponseDto {

	private Long id;
	private String content;
	private String writerName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}