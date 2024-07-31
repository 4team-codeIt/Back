package com.brick.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationIdResponse {

	private Long nextCursor;
	private Object data;

}
