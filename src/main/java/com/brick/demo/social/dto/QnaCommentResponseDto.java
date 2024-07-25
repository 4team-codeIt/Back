package com.brick.demo.social.dto;

import com.brick.demo.social.entity.QnaComment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QnaCommentResponseDto {

	private Long id;
	private String content;
	private String writerName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public QnaCommentResponseDto(QnaComment qnaComment) {
		this.id = qnaComment.getId();
		this.content = qnaComment.getContent();
		this.writerName = qnaComment.getWriter().getName();
		this.createdAt = qnaComment.getCreatedAt();
		this.updatedAt = qnaComment.getUpdatedAt();
	}

	public QnaCommentResponseDto(QnaComment qnaComment, LocalDateTime updatedAt) {
		this.id = qnaComment.getId();
		this.content = qnaComment.getContent();
		this.writerName = qnaComment.getWriter().getName();
		this.createdAt = qnaComment.getCreatedAt();
		this.updatedAt = updatedAt;
	}
}