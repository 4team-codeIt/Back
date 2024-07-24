package com.brick.demo.social.dto;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.QnaComment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class QnaCommentRequestDto {

	@NotEmpty(message = "내용은 빈 문자열일 수 없습니다")
	@Size(max = 600, message = "내용은 600자를 넘을 수 없습니다")
	private String content;

	public QnaComment toEntity(Qna qna, Account writer) {
		return QnaComment.builder().qna(qna).content(this.content).writer(writer).build();
	}
}