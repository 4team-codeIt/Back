package com.brick.demo.social.dto;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.Social;
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
	@Size(max = 600, message = "내용은 600자를 넘을 수 없습니다")
	private String content;

	public Qna toEntity(Account writer, Social social) {
		return Qna.builder()
				.writer(writer)
				.social(social)
				.title(this.title)
				.content(this.content)
				.build();
	}
}
