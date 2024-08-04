package com.brick.demo.social.dto;

import com.brick.demo.social.entity.QnaComment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class QnaCommentResponse {

	private Long id;
	private String content;
	private String writerName;
	private String writerProfileImageUrl;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public QnaCommentResponse(QnaComment qnaComment) {
		this.id = qnaComment.getId();
		this.content = qnaComment.getContent();
		this.writerName = qnaComment.getWriter().getName();
		this.writerProfileImageUrl = qnaComment.getWriter().getProfileImageUrl();
		this.createdAt = qnaComment.getCreatedAt();
		this.updatedAt = qnaComment.getUpdatedAt();
	}

	public QnaCommentResponse(QnaComment qnaComment, LocalDateTime updatedAt) {
		this.id = qnaComment.getId();
		this.content = qnaComment.getContent();
		this.writerName = qnaComment.getWriter().getName();
		this.writerProfileImageUrl = qnaComment.getWriter().getProfileImageUrl();
		this.createdAt = qnaComment.getCreatedAt();
		this.updatedAt = updatedAt;
	}
}