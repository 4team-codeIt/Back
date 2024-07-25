package com.brick.demo.auth.dto;

import com.brick.demo.auth.entity.Account;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

	private final String email;
	private final String name;
	private final LocalDate birthday;
	private final String introduce;
	private final String profileImageUrl;

	public UserResponseDto(Account account) {
		this.email = account.getEmail();
		this.name = account.getName();
		this.birthday = account.getBirthday();
		this.introduce = account.getIntroduce();
		this.profileImageUrl = account.getProfileImageUrl();
	}

}
