package com.brick.demo.auth.dto;

import com.brick.demo.auth.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {

	@Email
	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

	@NotEmpty
	private String name;

	@Builder
	public SignUpRequest(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}

	public Account toEntity(PasswordEncoder passwordEncoder) {
		return Account.builder()
				.email(this.email)
				.password(passwordEncoder.encode(this.password))
				.name(this.name)
				.build();
	}
}
