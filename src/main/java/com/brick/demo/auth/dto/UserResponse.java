package com.brick.demo.auth.dto;

import com.brick.demo.auth.entity.Account;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserResponse {

	private final String email;
	private final String name;
	private final LocalDate birthday;
	private final String introduce;
	private final String profileImageUrl;

	public UserResponse(Account account) {
		this.email = account.getEmail();
		this.name = account.getName();
		this.birthday = account.getBirthday();
		this.introduce = account.getIntroduce();
		this.profileImageUrl = account.getProfileImageUrl();
	}

}
