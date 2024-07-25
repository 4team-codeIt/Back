package com.brick.demo.social.dto;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.social.entity.Qna;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class QnaRequestDto {

	@NotEmpty(message = "제목은 빈 문자열일 수 없습니다")
	@Size(max = 40, message = "제목은 40자를 넘을 수 없습니다")
	private String title;

	@NotEmpty(message = "내용은 빈 문자열일 수 없습니다")
	@Size(max = 500, message = "내용은 500자를 넘을 수 없습니다")
	private String content;

	public Qna toEntity(Account writer, Long socialId) {
		return Qna.builder()
				.writer(writer)
				.socialId(socialId)
				.title(this.title)
				.content(this.content)
				.build();
	}
}
