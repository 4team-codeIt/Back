package com.brick.demo.social.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationDateResponse {

	private final LocalDateTime nextCursor;
	private final Object data;

}
