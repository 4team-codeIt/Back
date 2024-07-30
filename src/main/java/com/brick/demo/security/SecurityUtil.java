package com.brick.demo.security;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

	public static Account getCurrentAccount(AccountRepository accountRepository) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			throw new CustomException(ErrorDetails.E001);
		}
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		String name = userDetails.getName();
		return accountRepository.findByName(name)
				.orElseThrow(() -> new CustomException(ErrorDetails.E001));
	}
}
