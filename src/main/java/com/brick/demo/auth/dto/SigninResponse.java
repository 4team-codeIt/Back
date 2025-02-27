package com.brick.demo.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class SigninResponse {

	private final String grantType;
	private final String accessToken;
	private final Long accessTokenExpiresIn;

	@JsonCreator
	public SigninResponse(String grantType, String accessToken, Long accessTokenExpiresIn) {
		this.grantType = grantType;
		this.accessToken = accessToken;
		this.accessTokenExpiresIn = accessTokenExpiresIn;
	}
}
