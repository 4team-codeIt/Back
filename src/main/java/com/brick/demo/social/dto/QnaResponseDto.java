package com.brick.demo.social.dto;

import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.QnaComment;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class QnaResponseDto {

	private String id;
	private String title;
	private String content;
	private String writerName;
	private int commentCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public QnaResponseDto(Qna qna, LocalDateTime updatedAt, List<QnaComment> comments) {
		this.id = qna.getId().toString();
		this.title = qna.getTitle();
		this.content = qna.getContent();
		this.writerName = qna.getWriter().getName();
		this.commentCount = comments.size();
		this.createdAt = qna.getCreatedAt();
		this.updatedAt = updatedAt;
	}

	public QnaResponseDto(Qna qna, int commentCount) {
		this.id = qna.getId().toString();
		this.title = qna.getTitle();
		this.content = qna.getContent();
		this.writerName = qna.getWriter().getName();
		this.commentCount = commentCount;
		this.createdAt = qna.getCreatedAt();
		this.updatedAt = qna.getUpdatedAt();
	}
}
