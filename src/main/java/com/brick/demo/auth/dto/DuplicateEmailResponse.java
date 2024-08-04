package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DuplicateEmailResponse {

	@NotEmpty
	private final Boolean duplicateEmail;

	@JsonCreator
	public DuplicateEmailResponse(Boolean duplicateEmail) {
		this.duplicateEmail = duplicateEmail;
	}
}
