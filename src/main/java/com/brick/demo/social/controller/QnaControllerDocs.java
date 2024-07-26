package com.brick.demo.social.controller;

import com.brick.demo.social.dto.QnaPatchRequestDto;
import com.brick.demo.social.dto.QnaRequestDto;
import com.brick.demo.social.dto.QnaResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface QnaControllerDocs {

	@Operation(summary = "Qna 생성", description = "특정 social에 Qna를 생성합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Qna 생성 성공"),
			@ApiResponse(responseCode = "404", description = "유저가 로그인되지 않았습니다"),
			@ApiResponse(responseCode = "404", description = "아이디에 해당하는 모임을 찾을 수 없습니다"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다")
	})
	@PostMapping
	QnaResponseDto create(@PathVariable Long socialId, @Valid @RequestBody QnaRequestDto dto);

	@Operation(summary = "Qna 수정", description = "특정 social의 Qna를 수정합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Qna 수정 성공"),
			@ApiResponse(responseCode = "404", description = "해당하는 Qna ID의 Qna를 찾을 수 없습니다"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다")
	})
	@PatchMapping("/{qnaId}")
	QnaResponseDto update(@PathVariable Long qnaId, @Valid @RequestBody QnaPatchRequestDto dto);

	@Operation(summary = "Qna 삭제", description = "특정 Qna를 삭제합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "질문 삭제 성공"),
			@ApiResponse(responseCode = "404", description = "해당하는 Qna ID의 Qna를 찾을 수 없습니다"),
			@ApiResponse(responseCode = "400", description = "이미 삭제된 Qna 입니다")
	})
	@DeleteMapping("/{qnaId}")
	ResponseEntity<Void> delete(@PathVariable Long qnaId);
}
