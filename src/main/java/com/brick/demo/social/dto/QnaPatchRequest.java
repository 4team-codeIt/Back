package com.brick.demo.social.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class QnaPatchRequest {

	@Size(max = 40, message = "제목은 40자를 넘을 수 없습니다")
	private String title;

	@Size(max = 600, message = "내용은 600자를 넘을 수 없습니다")
	private String content;
}
