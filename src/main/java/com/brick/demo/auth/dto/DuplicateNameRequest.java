package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DuplicateNameRequest {

	@NotEmpty
	private final String name;

	@JsonCreator
	public DuplicateNameRequest(String name) {
		this.name = name;
	}
}
