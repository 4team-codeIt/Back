package com.brick.demo.social.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationIdResponse {

	private Long nextCursor;
	private Object data;

}
