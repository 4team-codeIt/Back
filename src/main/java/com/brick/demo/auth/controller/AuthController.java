package com.brick.demo.auth.controller;

import com.brick.demo.auth.dto.DuplicateEmailRequestDto;
import com.brick.demo.auth.dto.DuplicateEmailResponseDto;
import com.brick.demo.auth.dto.DuplicateNameRequestDto;
import com.brick.demo.auth.dto.DuplicateNameResponseDto;
import com.brick.demo.auth.dto.SignUpRequestDto;
import com.brick.demo.auth.dto.SigninRequestDto;
import com.brick.demo.auth.dto.SigninResponseDto;
import com.brick.demo.auth.dto.UserPatchRequestDto;
import com.brick.demo.auth.dto.UserResponseDto;
import com.brick.demo.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthControllerDocs {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final AuthService authService;


	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping(value = "/users")
	public Optional<UserResponseDto> accountDetails() {
		return authService.getAccountDetail();
	}

	@PatchMapping(value = "/users")
	public UserResponseDto updateAccount(@RequestBody UserPatchRequestDto dto) {
		return authService.updateAccount(dto);
	}

	@DeleteMapping("/users")
	public ResponseEntity<Void> delete(HttpServletRequest request, HttpServletResponse response) {
		authService.deleteAccount(request, response);
		return ResponseEntity.noContent().build();
	}


	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAccount(@Valid @RequestBody SignUpRequestDto dto) {
		authService.createAccount(dto);
	}

	@PostMapping("/signin")
	public SigninResponseDto createAuthenticationToken(
			@RequestBody SigninRequestDto dto) {
		return authService.signin(dto);
	}

	@GetMapping("/signout")
	public ResponseEntity<Void> signout(
			HttpServletRequest request, HttpServletResponse response) {
		return authService.signout(request, response);
	}

//  @PostMapping("/reissue")
//  public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
//    return ResponseEntity.ok(authService.reissue(tokenRequestDto));
//  }

	@PostMapping("/users/duplicate-email")
	public DuplicateEmailResponseDto duplicateEmail(
			@RequestBody DuplicateEmailRequestDto dto) {
		return authService.isDuplicatedEmail(dto);
	}


	@PostMapping("/users/duplicate-name")
	public DuplicateNameResponseDto duplicateName(
			@RequestBody DuplicateNameRequestDto dto) {
		return authService.isDuplicatedName(dto);
	}
}

