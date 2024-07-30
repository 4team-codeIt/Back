package com.brick.demo.social.service;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.security.CustomUserDetails;
import com.brick.demo.social.dto.PaginationIdResponse;
import com.brick.demo.social.dto.PaginationDateResponse;
import com.brick.demo.social.dto.QnaPatchRequestDto;
import com.brick.demo.social.dto.QnaRequestDto;
import com.brick.demo.social.dto.QnaResponseDto;
import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.repository.QnaCommentRepository;
import com.brick.demo.social.repository.QnaRepository;
import com.brick.demo.social.repository.SocialRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
	private SocialRepository socialRepository;

	@Autowired
	QnaService(AccountManager accountManager, QnaRepository qnaRepository,
			QnaCommentRepository qnaCommentRepository, SocialRepository socialRepository) {
		this.accountManager = accountManager;
		this.qnaRepository = qnaRepository;
		this.qnaCommentRepository = qnaCommentRepository;
		this.socialRepository = socialRepository;
	}

	public PaginationIdResponse getQnasBySocialId(Long socialId, Long cursor, int limit) {
		PageRequest pageable = PageRequest.of(0, limit);
		List<Qna> qnas = qnaRepository.findBySocialIdWithCursor(socialId, cursor, pageable);
		List<QnaResponseDto> qnaResponseDtos = qnas.stream()
				.map(qna -> new QnaResponseDto(qna, qna.getCommentCount()))
				.toList();
		Long nextCursor = qnas.size() == limit ? qnas.get(qnas.size() - 1).getId() : null;
		return new PaginationIdResponse(nextCursor, qnaResponseDtos);
	}

	public PaginationDateResponse getQnasBySocialIdByUpdatedAt(Long socialId, LocalDateTime cursor,
			int limit) {
		PageRequest pageable = PageRequest.of(0, limit);
		List<Qna> qnas = qnaRepository.findBySocialIdWithDateCursor(socialId, cursor, pageable);
		List<QnaResponseDto> qnaResponseDtos = qnas.stream()
				.map(qna -> new QnaResponseDto(qna, qna.getCommentCount()))
				.collect(Collectors.toList());
		LocalDateTime nextCursor =
				qnas.size() == limit ? qnas.get(qnas.size() - 1).getUpdatedAt() : null;
		return new PaginationDateResponse(nextCursor, qnaResponseDtos);
	}

	@Transactional
	public QnaResponseDto create(Long socialId, @Valid QnaRequestDto dto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		final String writerName = userDetails.getName();
		final Optional<Account> accountOptional = accountManager.getAccountByName(writerName);
		if (accountOptional.isEmpty()) {
			throw new CustomException(ErrorDetails.E001);
		}
		final Account account = accountOptional.get();
		final Social social = socialRepository.findById(socialId)
				.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
		Qna qna = dto.toEntity(account, social);
		qna = qnaRepository.save(qna);

		return QnaResponseDto.builder().id(String.valueOf(qna.getId())).title(qna.getTitle())
				.content(qna.getContent()).writerName(writerName).commentCount(0)
				.createdAt(qna.getCreatedAt())
				.updatedAt(qna.getUpdatedAt())
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
		if (qna.getDeletedAt() != null && qna.getDeletedAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 Qna 입니다");
		}
		qna.softDelete();
		qnaRepository.save(qna);
	}
}