package com.brick.demo.auth.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class UserPatchRequest {

	private LocalDate birthday;
	private String introduce;
	private String profileImageUrl;

}
