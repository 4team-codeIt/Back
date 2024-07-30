package com.brick.demo.social.controller;


import com.brick.demo.social.dto.PaginationDateResponse;
import com.brick.demo.social.dto.PaginationIdResponse;
import com.brick.demo.social.dto.QnaPatchRequestDto;
import com.brick.demo.social.dto.QnaRequestDto;
import com.brick.demo.social.dto.QnaResponseDto;
import com.brick.demo.social.service.QnaService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	@GetMapping // qna created_at이 커서 (최신순)
	public PaginationDateResponse getQnasBySocialIdOrderByLocalDate(@PathVariable Long socialId,
			@RequestParam(value = "cursor", required = false) LocalDateTime cursor,
			@RequestParam(value = "limit") int limit) {
		return qnaService.getQnasBySocialIdByCreatedAt(socialId, cursor, limit);
	}

	@PostMapping
	public QnaResponseDto create(@PathVariable Long socialId, @Valid @RequestBody QnaRequestDto dto) {
		log.info("디티오 : {}", dto);
		return qnaService.create(socialId, dto);
	}

	@PatchMapping("/{qnaId}")
	public QnaResponseDto update(@PathVariable Long qnaId,
			@Valid @RequestBody
			QnaPatchRequestDto dto) {
		return qnaService.update(qnaId, dto);
	}

	@DeleteMapping("/{qnaId}")
	public ResponseEntity<Void> delete(@PathVariable Long qnaId) {
		qnaService.delete(qnaId);
		return ResponseEntity.noContent().build();
	}
}