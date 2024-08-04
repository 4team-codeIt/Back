package com.brick.demo.auth.controller;

import com.brick.demo.auth.dto.DuplicateEmailRequest;
import com.brick.demo.auth.dto.DuplicateEmailResponse;
import com.brick.demo.auth.dto.DuplicateNameRequest;
import com.brick.demo.auth.dto.DuplicateNameResponse;
import com.brick.demo.auth.dto.SignUpRequest;
import com.brick.demo.auth.dto.SigninRequest;
import com.brick.demo.auth.dto.SigninResponse;
import com.brick.demo.auth.dto.UserPatchRequest;
import com.brick.demo.auth.dto.UserResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public Optional<UserResponse> accountDetails() {
		return authService.getAccountDetail();
	}

	@PatchMapping(value = "/users")
	public UserResponse updateAccount(@RequestBody UserPatchRequest dto) {
		return authService.updateAccount(dto);
	}

	@DeleteMapping("/users")
	public ResponseEntity<Void> delete(HttpServletRequest request, HttpServletResponse response) {
		authService.deleteAccount(request, response);
		return ResponseEntity.noContent().build();
	}


	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAccount(@Valid @RequestBody SignUpRequest dto) {
		authService.createAccount(dto);
	}

	@PostMapping("/signin")
	public SigninResponse createAuthenticationToken(
			@RequestBody SigninRequest dto) {
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
	public DuplicateEmailResponse duplicateEmail(
			@RequestBody DuplicateEmailRequest dto) {
		return authService.isDuplicatedEmail(dto);
	}


	@PostMapping("/users/duplicate-name")
	public DuplicateNameResponse duplicateName(
			@RequestBody DuplicateNameRequest dto) {
		return authService.isDuplicatedName(dto);
	}
}

