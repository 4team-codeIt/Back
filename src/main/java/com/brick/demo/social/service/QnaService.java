package com.brick.demo.social.service;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.security.CustomUserDetails;
import com.brick.demo.social.dto.QnaPatchRequestDto;
import com.brick.demo.social.dto.QnaRequestDto;
import com.brick.demo.social.dto.QnaResponseDto;
import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.repository.QnaCommentRepository;
import com.brick.demo.social.repository.QnaRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class QnaService {

	private static final Logger log = LoggerFactory.getLogger(QnaService.class);
	private AccountManager accountManager;
	private QnaRepository qnaRepository;
	private QnaCommentRepository qnaCommentRepository;

	@Autowired
	QnaService(AccountManager accountManager, QnaRepository qnaRepository,
			QnaCommentRepository qnaCommentRepository) {
		this.accountManager = accountManager;
		this.qnaRepository = qnaRepository;
		this.qnaCommentRepository = qnaCommentRepository;
	}

	@Transactional
	public QnaResponseDto create(Long socialId, QnaRequestDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		final String writerName = userDetails.getUsername();
		final Optional<Account> accountOptional = accountManager.getAccountByName(writerName);
		if (accountOptional.isEmpty()) {
			throw new CustomException(ErrorDetails.E001);
		}
		final Account account = accountOptional.get();
		log.info("Create Qna : {}", account);
		Qna qna = dto.toEntity(account, socialId);
		qna = qnaRepository.save(qna);

		return QnaResponseDto.builder().id(String.valueOf(qna.getId())).title(qna.getTitle())
				.content(qna.getContent()).writerName(writerName).commentCount(0)
				.createdAt(qna.getCreatedAt())
				.updatedAt(qna.getUpdatedAt()).comments(List.of())
				.build();
	}

	@Transactional
	public QnaResponseDto update(Long qnaId, QnaPatchRequestDto dto) {
		Qna qna = qnaRepository.findById(qnaId)
				.orElseThrow(
						() -> new CustomException(HttpStatus.NOT_FOUND, "해당하는 Qna ID의 Qna를 찾을 수 없습니다"));

		final String title = (dto.getTitle() != null) ? dto.getTitle() : qna.getTitle();
		final String content = (dto.getContent() != null) ? dto.getContent() : qna.getContent();
		qna.update(title, content);
		qnaRepository.save(qna);
		qnaRepository.flush(); //영속성 컨텍스트의 변경사항을 DB에 바로 반영
		LocalDateTime updatedAt = qnaRepository.findById(qnaId).get().getUpdatedAt();
		return new QnaResponseDto(qna, updatedAt, List.of());
	}

	public void delete(Long qnaId) {
		Qna qna = qnaRepository.findById(qnaId)
				.orElseThrow(
						() -> new CustomException(HttpStatus.NOT_FOUND, "해당하는 Qna ID의 Qna를 찾을 수 없습니다"));
		if (qna.getDeletedAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 Qna 입니다");
		}
		qna.softDelete();
		qnaRepository.save(qna);
	}
}