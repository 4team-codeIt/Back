package com.brick.demo.social.service;

import static com.brick.demo.security.SecurityUtil.getCurrentAccount;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.security.CustomUserDetails;
import com.brick.demo.social.dto.PaginationIdResponse;
import com.brick.demo.social.dto.QnaCommentPatchDto;
import com.brick.demo.social.dto.QnaCommentRequestDto;
import com.brick.demo.social.dto.QnaCommentResponseDto;
import com.brick.demo.social.dto.QnaResponseDto;
import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.QnaComment;
import com.brick.demo.social.repository.QnaCommentRepository;
import com.brick.demo.social.repository.QnaRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

	private final QnaCommentRepository qnaCommentRepository;
	private final AccountRepository accountRepository;
	private final QnaRepository qnaRepository;

	@Transactional
	public PaginationIdResponse getCommentsByQnaId(Long qnaId, Long cursor, int limit) {
		PageRequest pageable = PageRequest.of(0, limit);
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("qna.id", qnaId);

		List<QnaComment> comments = qnaCommentRepository.findByCursorAndOrderByIdAsc(cursor, pageable,
				conditions);
		boolean hasNext = comments.size() > limit;
		if (hasNext) {
			comments = comments.subList(0, limit); // 필요한 만큼만 반환
		}
		List<QnaCommentResponseDto> commentResponseDtos = comments.stream()
				.map(QnaCommentResponseDto::new)
				.collect(Collectors.toList());
		Long nextCursor = hasNext ? comments.get(limit - 1).getId() : null;
		return new PaginationIdResponse(nextCursor, commentResponseDtos);
	}

	@Transactional
	public QnaCommentResponseDto create(Long qnaId, QnaCommentRequestDto dto) {
		Account account = getCurrentAccount(accountRepository);
		Qna qna = qnaRepository.findById(qnaId)
				.orElseThrow(
						() -> new CustomException(HttpStatus.NOT_FOUND, "해당하는 Qna ID의 Qna를 찾을 수 없습니다"));
		QnaComment qnaComment = dto.toEntity(qna, account);
		qnaComment = qnaCommentRepository.save(qnaComment);

		return new QnaCommentResponseDto(qnaComment);
	}

	@Transactional
	public QnaCommentResponseDto update(Long commentId, QnaCommentPatchDto dto) {
		QnaComment qnaComment = qnaCommentRepository.findById(commentId)
				.orElseThrow(
						() -> new CustomException(HttpStatus.NOT_FOUND, "해당하는 Qna 댓글 ID의 댓글을 찾을 수 없습니다"));

		final String content = (dto.getContent() != null) ? dto.getContent() : qnaComment.getContent();
		qnaComment.update(content);
		qnaCommentRepository.save(qnaComment);
		qnaCommentRepository.flush(); //영속성 컨텍스트의 변경사항을 DB에 바로 반영
		LocalDateTime updatedAt = qnaCommentRepository.findById(commentId).get().getUpdatedAt();
		return new QnaCommentResponseDto(qnaComment, updatedAt);
	}

	@Transactional
	public void delete(Long commentId) {
		QnaComment qnaComment = qnaCommentRepository.findById(commentId)
				.orElseThrow(
						() -> new CustomException(HttpStatus.NOT_FOUND, "해당하는 Qna 댓글 ID의 댓글을 찾을 수 없습니다"));
		if (qnaComment.getDeletedAt() != null && qnaComment.getDeletedAt()
				.isBefore(LocalDateTime.now())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이미 삭제된 Qna 댓글 입니다");
		}
		qnaComment.softDelete();
		qnaCommentRepository.save(qnaComment);
	}
}
