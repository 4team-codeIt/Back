package com.brick.demo.social.controller;


import com.brick.demo.social.dto.QnaPageResponse;
import com.brick.demo.social.dto.QnaPatchRequest;
import com.brick.demo.social.dto.QnaRequest;
import com.brick.demo.social.dto.QnaResponse;
import com.brick.demo.social.service.QnaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socials/{socialId}/qnas")
public class QnaController implements QnaControllerDocs {

	private static final Logger log = LoggerFactory.getLogger(QnaController.class);
	private final QnaService qnaService;

	@Autowired
	QnaController(QnaService qnaService) {
		this.qnaService = qnaService;
	}

//	@GetMapping // qna created_at이 커서 (최신순) - 커서기반
//	public PaginationDateResponse getQnasBySocialIdOrderByLocalDate(@PathVariable Long socialId,
//			@RequestParam(value = "cursor", required = false) LocalDateTime cursor,
//			@RequestParam(value = "limit") int limit) {
//		return qnaService.getQnasBySocialIdByCreatedAt(socialId, cursor, limit);
//	}

	@GetMapping//오프셋 기반 페이지네이션
	public QnaPageResponse getQnasBySocialIdOrderByLocalDate(
			@PathVariable Long socialId,
			@ParameterObject Pageable pageable) {
		return qnaService.getQnasBySocialIdByCreatedAt(socialId, pageable);
	}

	@PostMapping
	public QnaResponse create(@PathVariable Long socialId, @Valid @RequestBody QnaRequest dto) {
		log.info("디티오 : {}", dto);
		return qnaService.create(socialId, dto);
	}

	@PatchMapping("/{qnaId}")
	public QnaResponse update(@PathVariable Long socialId, @PathVariable Long qnaId,
			@Valid @RequestBody
			QnaPatchRequest dto) {
		return qnaService.update(qnaId, dto);
	}

	@DeleteMapping("/{qnaId}")
	public ResponseEntity<Void> delete(@PathVariable Long socialId, @PathVariable Long qnaId) {
		qnaService.delete(qnaId);
		return ResponseEntity.noContent().build();
	}
}