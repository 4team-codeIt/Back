package com.brick.demo.social.dto;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.social.entity.Qna;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class QnaPatchRequestDto {

	@Size(max = 40, message = "제목은 40자를 넘을 수 없습니다")
	private String title;

	@Size(max = 500, message = "내용은 500자를 넘을 수 없습니다")
	private String content;
}
