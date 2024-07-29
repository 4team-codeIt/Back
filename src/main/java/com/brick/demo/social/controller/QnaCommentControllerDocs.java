package com.brick.demo.social.controller;

import com.brick.demo.social.dto.QnaCommentPatchDto;
import com.brick.demo.social.dto.QnaCommentRequestDto;
import com.brick.demo.social.dto.QnaCommentResponseDto;
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

public interface QnaCommentControllerDocs {

	@Operation(summary = "Qna 댓글 생성", description = "특정 Qna에 댓글을 생성합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Qna 댓글 생성 성공"),
			@ApiResponse(responseCode = "404", description = "유저가 로그인되지 않았습니다"),
			@ApiResponse(responseCode = "404", description = "해당하는 Qna ID의 Qna를 찾을 수 없습니다"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다")
	})
	@PostMapping
	QnaCommentResponseDto create(@PathVariable Long qnaId,
			@Valid @RequestBody QnaCommentRequestDto dto);

	@Operation(summary = "Qna 댓글 수정", description = "특정 Qna의 댓글을 수정합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Qna 댓글 수정 성공"),
			@ApiResponse(responseCode = "404", description = "해당하는 Qna 댓글 ID의 댓글을 찾을 수 없습니다"),
			@ApiResponse(responseCode = "400", description = "요청이 유효하지 않습니다")
	})
	@PatchMapping("/{commentId}")
	QnaCommentResponseDto update(@PathVariable Long commentId,
			@Valid @RequestBody QnaCommentPatchDto dto);

	@Operation(summary = "Qna 댓글 삭제", description = "특정 Qna 댓글을 삭제합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Qna 댓글 삭제 성공"),
			@ApiResponse(responseCode = "404", description = "해당하는 Qna 댓글 ID의 댓글을 찾을 수 없습니다"),
			@ApiResponse(responseCode = "400", description = "이미 삭제된 Qna 댓글 입니다")
	})
	@DeleteMapping("/{commentId}")
	ResponseEntity<Void> delete(@PathVariable Long commentId);
}