package com.brick.demo.social.controller;

import com.brick.demo.social.dto.QnaCommentPageResponse;
import com.brick.demo.social.dto.QnaCommentPatch;
import com.brick.demo.social.dto.QnaCommentRequest;
import com.brick.demo.social.dto.QnaCommentResponse;
import com.brick.demo.social.service.QnaCommentService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socials/{socialId}/qnas/{qnaId}/comments")
public class QnaCommentController implements QnaCommentControllerDocs {

	private final QnaCommentService qnaCommentService;

	@Autowired
	QnaCommentController(QnaCommentService qnaCommentService) {
		this.qnaCommentService = qnaCommentService;
	}

//	@GetMapping // qna id가 커서 (오래된순) - 커서기반
//	public PaginationIdResponse getCommentsByQnaId(@PathVariable Long socialId,
//			@PathVariable Long qnaId,
//			@RequestParam(value = "cursor", required = false) Long cursor,
//			@RequestParam(value = "limit") int limit) {
//		return qnaCommentService.getCommentsByQnaId(qnaId, cursor, limit);
//	}

	@GetMapping // 오프셋 기반 페이지네이션
	public QnaCommentPageResponse getCommentsByQnaId(@PathVariable Long socialId,
			@PathVariable Long qnaId, @ParameterObject Pageable pageable) {
		return qnaCommentService.getCommentsByQnaId(socialId, qnaId, pageable);
	}

	@PostMapping
	public QnaCommentResponse create(@PathVariable Long socialId, @PathVariable Long qnaId,
			@Valid @RequestBody QnaCommentRequest dto) {
		return qnaCommentService.create(qnaId, dto);
	}

	@PatchMapping("/{commentId}")
	public QnaCommentResponse update(@PathVariable Long socialId, @PathVariable Long qnaId,
			@PathVariable Long commentId,
			@Valid @RequestBody
			QnaCommentPatch dto) {
		return qnaCommentService.update(commentId, dto);
	}

	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> delete(@PathVariable Long socialId, @PathVariable Long qnaId,
			@PathVariable Long commentId) {
		qnaCommentService.delete(commentId);
		return ResponseEntity.noContent().build();
	}
}
