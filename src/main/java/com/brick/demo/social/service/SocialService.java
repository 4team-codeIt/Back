package com.brick.demo.social.service;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SocialService {

  private final SocialRepository socialRepository;
  private final SocialDetailRepository socialDetailRepository;
  private final SocialParticipantRepository socialParticipantRepository;
  private final AccountManager accountManager;

  // TODO 필터링 고도화
  public List<SocialResponse> selectSocials(final String orderBy) {
    //    if (orderBy.equals("popularity")) {
    //      return socialRepository.findAll(sortByPopularity).stream()
    //          .map(SocialResponse::fromEntity)
    //          .collect(Collectors.toList());
    //    }
    return socialRepository.findAllByOrderByGatheringDateDesc().stream()
        .map(SocialResponse::fromEntity)
        .collect(Collectors.toList());
  }

  public SocialResponse selectSocialById(Long id) throws CustomException {
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

    return SocialResponse.fromEntity(social);
  }

  public SocialCreateResponse createSocial(final SocialCreateRequest dto) {
    Account account = getAccount();
    Social social = socialRepository.save(Social.save(account, dto));
    SocialDetail detail = socialDetailRepository.save(SocialDetail.save(social, dto));

    socialParticipantRepository.save(new SocialParticipant(social, account));
    socialRepository.save(Social.updateDetail(social, detail));

    return new SocialCreateResponse(social.getId(), "모임을 성공적으로 생성하였습니다.");
  }

  public void updateSocial(final Long id, final SocialUpdateRequest dto) {
    Account account = getAccount();
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

    if (!account.getName().equals(social.getOwner().getName())) {
      throw new CustomException(ErrorDetails.SOCIAL_FORBIDDEN);
    }
    socialRepository.save(Social.update(social, dto));
  }

  public void cancelSocial(Long id) {
    Account account = getAccount();
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

    if (!account.getName().equals(social.getOwner().getName())) {
      throw new CustomException(ErrorDetails.SOCIAL_FORBIDDEN);
    }
    socialRepository.save(Social.cancel(social));
  }

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

  public void joinSocial(final Long id) {
    Account account = getAccount();
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

    checkJoinCondition(social, account);
    socialParticipantRepository.save(new SocialParticipant(social, account));
  }

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
    return accountManager.getAccountByName(name).get();
  }
}
