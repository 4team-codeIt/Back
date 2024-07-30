package com.brick.demo.social.service;

import static com.brick.demo.security.SecurityUtil.getCurrentAccount;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.security.CustomUserDetails;
import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialDetailResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialUpdateRequest;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialDetail;
import com.brick.demo.social.entity.SocialParticipant;
import com.brick.demo.social.repository.SocialDetailRepository;
import com.brick.demo.social.repository.SocialParticipantRepository;
import com.brick.demo.social.repository.SocialRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialService {

	private static final Logger log = LoggerFactory.getLogger(SocialService.class);
	private final SocialRepository socialRepository;
	private final SocialDetailRepository socialDetailRepository;
	private final SocialParticipantRepository socialParticipantRepository;
	private final AccountRepository accountRepository;

	@Transactional
	public List<SocialResponse> selectSocials(final String filterBy, final String orderBy) {
		return findAllSocials(filterBy, orderBy).stream()
				.map(SocialResponse::fromEntity)
				.collect(Collectors.toList());
	}

	@Transactional
	public SocialResponse selectSocialById(Long id) throws CustomException {
		Social social =
				socialRepository
						.findById(id)
						.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

		return SocialResponse.fromEntity(social);
	}

	@Transactional
	public SocialCreateResponse createSocial(final SocialCreateRequest dto) {
		Account account = getCurrentAccount(accountRepository);
		Social social = socialRepository.save(new Social(account, dto));
		SocialDetail detail = socialDetailRepository.save(new SocialDetail(social, dto));

		socialParticipantRepository.save(new SocialParticipant(social, account));
		social.updateDetail(detail);

		return new SocialCreateResponse(social.getId(), "모임을 성공적으로 생성하였습니다.");
	}

	@Transactional
	public void updateSocial(final Long id, final SocialUpdateRequest dto) {
		Account account = getCurrentAccount(accountRepository);
		Social social =
				socialRepository
						.findById(id)
						.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
		log.info("어스  이름:", account.getName());
		log.info("소셜 작성자 이름:", social.getName());
		if (!account.getName().equals(social.getOwner().getName())) {
			throw new CustomException(ErrorDetails.SOCIAL_FORBIDDEN);
		}

		social.update(dto);
	}

	@Transactional
	public void cancelSocial(Long id) {
		Account account = getCurrentAccount(accountRepository);
		Social social =
				socialRepository
						.findById(id)
						.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

		if (!account.getName().equals(social.getOwner().getName())) {
			throw new CustomException(ErrorDetails.SOCIAL_FORBIDDEN);
		}

		social.cancel();
	}

	@Transactional
	public SocialDetailResponse selectDetailBySocialId(final Long id) {
		Social social =
				socialRepository
						.findById(id)
						.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
		SocialDetail detail =
				socialDetailRepository
						.findBySocialId(id)
						.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

		return SocialDetailResponse.fromEntities(social, detail);
	}

	private List<Social> findAllSocials(final String filterBy, final String orderBy) {
		LocalDateTime now = LocalDateTime.now();
		String filter = (filterBy == null) ? "" : filterBy;
		String order = (orderBy == null) ? "" : orderBy;

		switch (filter) {
			case "open":
				return socialRepository.findAllByGatheringDateAfterOrderByGatheringDateDesc(now);
			case "close":
				return socialRepository.findAllByGatheringDateBeforeOrderByGatheringDateDesc(now);
			case "cancel":
				return socialRepository.findAllByCanceledTrueOrderByCreatedAtDesc();
			case "host":
				return socialRepository.findAllOrderByPopularityDesc();
			default:
				if (order.equals("popularity")) {
					return socialRepository.findAllOrderByPopularityDesc();
				}
				return socialRepository.findAllByOrderByCreatedAtDesc();
		}
	}
}
