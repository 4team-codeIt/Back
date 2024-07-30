package com.brick.demo.social.service;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.security.CustomUserDetails;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialParticipant;
import com.brick.demo.social.repository.SocialParticipantRepository;
import com.brick.demo.social.repository.SocialRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialParticipantService {

	private final SocialRepository socialRepository;
	private final SocialParticipantRepository socialParticipantRepository;
	private final AccountRepository accountRepository;

	@Transactional
	public void joinSocial(final Long id) {
		Account account = getAccount();
		Social social =
				socialRepository
						.findById(id)
						.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

		checkJoinCondition(social, account);
		socialParticipantRepository.save(new SocialParticipant(social, account));
	}

	@Transactional
	public void leaveSocial(final Long id) {
		Account account = getAccount();
		Social social =
				socialRepository
						.findById(id)
						.orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

		checkCancelCondition(social, account);
		socialParticipantRepository.deleteAllBySocialIdAndAccountEntityId(
				social.getId(), account.getEntityId());
	}

	private void checkJoinCondition(final Social social, final Account account) {
		checkParticipantLimit(social);
		checkDuplicateParticipation(social, account);
	}

	private void checkCancelCondition(final Social social, final Account account) {
		checkGatheringDate(social);
		checkParticipation(social, account);
		checkRole(social, account);
	}

	private void checkParticipantLimit(final Social social) {
		int maxCount = social.getMaxCount();
		int current = social.getParticipants().size();
		if (current >= maxCount) {
			throw new CustomException(ErrorDetails.SOCIAL_EXCEED_MAX_LIMIT);
		}
	}

	private void checkDuplicateParticipation(final Social social, final Account account) {
		if (isParticipated(social, account)) {
			throw new CustomException(ErrorDetails.SOCIAL_ALREADY_JOINED);
		}
	}

	private void checkGatheringDate(final Social social) {
		LocalDateTime gatheringDate = social.getGatheringDate();
		if (gatheringDate.isBefore(LocalDateTime.now())) {
			throw new CustomException(ErrorDetails.SOCIAL_ALREADY_PASSED);
		}
	}

	private void checkParticipation(final Social social, final Account account) {
		if (!isParticipated(social, account)) {
			throw new CustomException(ErrorDetails.SOCIAL_NOT_JOINED);
		}
	}

	private void checkRole(final Social social, Account account) {
		if (social.getOwner().getEntityId().equals(account.getEntityId())) {
			throw new CustomException(ErrorDetails.SOCIAL_OWNER_LEAVE_FORBIDDEN);
		}
	}

	private boolean isParticipated(final Social social, final Account account) {
		return social.getParticipants().stream()
				.anyMatch(
						participant -> participant.getAccount().getEntityId().equals(account.getEntityId()));
	}

	private Account getAccount() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		String name = userDetails.getName();
		return accountRepository.findByName(name).get();
	}
}
