package com.brick.demo.social.service;

import static com.brick.demo.security.SecurityUtil.getCurrentAccount;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.social.dto.QnaPageResponse;
import com.brick.demo.social.dto.QnaPatchRequest;
import com.brick.demo.social.dto.QnaRequest;
import com.brick.demo.social.dto.QnaResponse;
import com.brick.demo.social.entity.Qna;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.repository.QnaCommentRepository;
import com.brick.demo.social.repository.QnaRepository;
import com.brick.demo.social.repository.SocialRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaService {

	private static final Logger log = LoggerFactory.getLogger(QnaService.class);
	private final AccountRepository accountRepository;
	private final QnaRepository qnaRepository;
	private final QnaCommentRepository qnaCommentRepository;
	private final SocialRepository socialRepository;

//	public PaginationDateResponse getQnasBySocialIdByCreatedAt(Long socialId, LocalDateTime cursor,
//			int limit) {
//		Map<String, Object> conditions = new HashMap<>();
//		conditions.put("social.id", socialId);
//		List<Qna> qnas = qnaRepository.findByCursorAndOrderByCreatedAtDesc(cursor, limit,
//				conditions);
//		boolean hasNext = qnas.size() > limit;
//		if (hasNext) {
//			qnas = qnas.subList(0, limit); // 필요한 만큼만 반환
//		}
//		List<QnaResponseDto> qnaResponseDtos = qnas.stream()
//				.map(qna -> new QnaResponseDto(qna, qna.getCommentCount()))
//				.collect(Collectors.toList());
//		LocalDateTime nextCursor = hasNext ? qnas.get(limit - 1).getCreatedAt() : null;
//		return new PaginationDateResponse(nextCursor, qnaResponseDtos);
//	}

	public QnaPageResponse getQnasBySocialIdByCreatedAt(Long socialId,
			Pageable pageable) {
		socialRepository.findById(socialId).orElseThrow(
				() -> new CustomException(HttpStatus.NOT_FOUND, "해당하는 Social ID의 Social를 찾을 수 없습니다"));

		Pageable sortedByCreatedAtDesc = PageRequest.of(
				pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<Qna> qnaPage = qnaRepository.findBySocialIdAndDeletedAtIsNull(socialId,
				sortedByCreatedAtDesc);
		return QnaPageResponse.from(qnaPage);
	}


	@Transactional
	public QnaResponse create(Long socialId, @Valid QnaRequest dto) {
		final Account account = getCurrentAccount(accountRepository);
		final Social social = socialRepository.findById(socialId)
				.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
		Qna qna = dto.toEntity(account, social);
		qna = qnaRepository.save(qna);

		return QnaResponse.builder().id(String.valueOf(qna.getId())).title(qna.getTitle())
				.content(qna.getContent()).writerName(account.getName()).commentCount(0)
				.createdAt(qna.getCreatedAt())
				.updatedAt(qna.getUpdatedAt())
				.build();
	}

	@Transactional
	public QnaResponse update(Long qnaId, QnaPatchRequest dto) {
		Qna qna = qnaRepository.findById(qnaId)
				.orElseThrow(
						() -> new CustomException(HttpStatus.NOT_FOUND, "해당하는 Qna ID의 Qna를 찾을 수 없습니다"));

		final String title = (dto.getTitle() != null) ? dto.getTitle() : qna.getTitle();
		final String content = (dto.getContent() != null) ? dto.getContent() : qna.getContent();
		qna.update(title, content);
		qnaRepository.save(qna);
		qnaRepository.flush(); //영속성 컨텍스트의 변경사항을 DB에 바로 반영
		LocalDateTime updatedAt = qnaRepository.findById(qnaId).get().getUpdatedAt();
		return new QnaResponse(qna, updatedAt, List.of());
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