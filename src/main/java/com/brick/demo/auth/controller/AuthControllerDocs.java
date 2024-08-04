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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface AuthControllerDocs {

	@Operation(summary = "사용자 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공")
	@GetMapping("/users")
	Optional<UserResponse> accountDetails();

	@Operation(summary = "사용자 정보 수정", description = "로그인된 사용자의 정보를 수정합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다"),
	})
	@PatchMapping("/users")
	UserResponse updateAccount(@Valid @RequestBody UserPatchRequest dto);

	@Operation(summary = "사용자 삭제", description = "로그인된 사용자의 계정을 삭제합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "사용자 삭제 성공")
	})
	@DeleteMapping("/users")
	ResponseEntity<Void> delete(HttpServletRequest request, HttpServletResponse response);

	@Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "회원 가입 성공"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다")
	})
	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	void createAccount(@Valid @RequestBody SignUpRequest dto);

	@Operation(summary = "로그인", description = "사용자 로그인을 수행합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다"),
			@ApiResponse(responseCode = "400", description = "이미 탈퇴한 회원입니다"),
			@ApiResponse(responseCode = "401", description = "잘못된 이메일 또는 비밀번호입니다"),
			@ApiResponse(responseCode = "401", description = "로그인한 상태에서 로그인 요청을 보낼 수 없습니다"),
			@ApiResponse(responseCode = "401", description = "인증 실패")
	})
	@PostMapping("/signin")
	SigninResponse createAuthenticationToken(@Valid @RequestBody SigninRequest dto);

	@Operation(summary = "로그아웃", description = "로그인된 사용자를 로그아웃합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "로그아웃 성공")
	})
	@GetMapping("/signout")
	ResponseEntity<Void> signout(HttpServletRequest request, HttpServletResponse response);

	@Operation(summary = "이메일 중복 확인", description = "이메일 중복 여부를 확인합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다")
	})
	@PostMapping("/users/duplicate-email")
	DuplicateEmailResponse duplicateEmail(@Valid @RequestBody DuplicateEmailRequest dto);

	@Operation(summary = "이름 중복 확인", description = "이름 중복 여부를 확인합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "이름 중복 확인 성공"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다")
	})
	@PostMapping("/users/duplicate-name")
	DuplicateNameResponse duplicateName(@Valid @RequestBody DuplicateNameRequest dto);
}