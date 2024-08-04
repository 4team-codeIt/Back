package com.brick.demo.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

	private String grantType;
	private String accessToken;
	private String refreshToken;
	private Long accessTokenExpiresIn;

}