package com.brick.demo.social.service;

import static com.brick.demo.security.SecurityUtil.getCurrentAccount;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.social.dto.request.SocialCreateRequest;
import com.brick.demo.social.dto.request.SocialQuery;
import com.brick.demo.social.dto.request.SocialUpdateRequest;
import com.brick.demo.social.dto.response.SocialCreateResponse;
import com.brick.demo.social.dto.response.SocialDetailResponse;
import com.brick.demo.social.dto.response.SocialResponse;
import com.brick.demo.social.dto.response.SocialResponses;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialDetail;
import com.brick.demo.social.entity.SocialParticipant;
import com.brick.demo.social.repository.SocialDetailRepository;
import com.brick.demo.social.repository.SocialParticipantRepository;
import com.brick.demo.social.repository.SocialRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  public SocialResponses getSocials(final SocialQuery query) {
    Pageable pageable = PageRequest.of(query.offset(), query.limit());

    Page<Social> pages =
        query.ids() == null
            ? findAllSocials(
                query.filterBy(), query.orderBy(), query.name(), query.tags(), pageable)
            : findAllSocialsByIdList(
                query.filterBy(),
                query.orderBy(),
                query.name(),
                query.tags(),
                query.ids(),
                pageable);

    return SocialResponses.from(pages);
  }

  @Transactional
  public SocialResponses getMySocials(final SocialQuery query) {
    Pageable pageable = PageRequest.of(query.offset(), query.limit());

    return SocialResponses.from(findAllMySocials(query.filterBy(), query.orderBy(), pageable));
  }

  @Transactional
  public SocialResponse getSocialById(Long id) throws CustomException {
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

    return SocialResponse.from(social);
  }

  @Transactional
  public SocialCreateResponse createSocial(final SocialCreateRequest dto) {
    Account account = getCurrentAccount(accountRepository);
    Social social = socialRepository.save(new Social(account, dto));
    SocialDetail detail = socialDetailRepository.save(new SocialDetail(social, dto));

    socialParticipantRepository.save(new SocialParticipant(social, account));

    return new SocialCreateResponse(social.getId(), "모임을 성공적으로 생성하였습니다.");
  }

  @Transactional
  public void updateSocial(final Long id, final SocialUpdateRequest dto) {
    Account account = getCurrentAccount(accountRepository);
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
    if (!account.getName().equals(social.getOwner().getName())) {
      throw new CustomException(ErrorDetails.SOCIAL_FORBIDDEN);
    }
    SocialDetail detail = socialDetailRepository.findBySocialId(id).get();

    social.update(dto);
    detail.update(dto);
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
  public SocialDetailResponse getDetailBySocialId(final Long id) {
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
    SocialDetail detail =
        socialDetailRepository
            .findBySocialId(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

    return SocialDetailResponse.from(social, detail);
  }

  private Page<Social> findAllSocials(
      final String filterBy,
      final String orderBy,
      final String name,
      final List<String> tags,
      Pageable pageable) {
    LocalDateTime now = LocalDateTime.now();
    String filter = (filterBy == null) ? "" : filterBy;
    String order = (orderBy == null) ? "" : orderBy;
    String nameKeyword = parseKeyword(name);

    switch (filter) {
      case "open":
        return socialRepository.findAllInProgress(nameKeyword, now, pageable);
      case "close":
        return socialRepository.findAllCompleted(nameKeyword, now, pageable);
      case "cancel":
        return socialRepository.findAllCanceled(pageable);
      default:
        if (order.equals("popularity")) {
          return socialRepository.findAllOrderByPopularity(nameKeyword, pageable);
        }
        return socialRepository.findAllDefault(nameKeyword, pageable);
    }
  }

  private Page<Social> findAllSocialsByIdList(
      final String filterBy,
      final String orderBy,
      final String name,
      final List<String> tags,
      final List<Long> ids,
      Pageable pageable) {
    LocalDateTime now = LocalDateTime.now();
    String filter = (filterBy == null) ? "" : filterBy;
    String order = (orderBy == null) ? "" : orderBy;
    String nameKeyword = parseKeyword(name);

    switch (filter) {
      case "open":
        return socialRepository.findPartInProgress(ids, now, pageable);
      case "close":
        return socialRepository.findPartCompleted(ids, now, pageable);
      default:
        if (order.equals("popularity")) {
          return socialRepository.findPartOrderByPopularity(ids, pageable);
        }
        return socialRepository.findPartDefault(ids, pageable);
    }
  }

  private Page<Social> findAllMySocials(
      final String filterBy, final String orderBy, Pageable pageable) {
    Account account = getCurrentAccount(accountRepository);
    Long ownerEntityId = account.getEntityId();

    LocalDateTime now = LocalDateTime.now();
    String filter = (filterBy == null) ? "" : filterBy;
    String order = (orderBy == null) ? "" : orderBy;

    switch (filter) {
      case "open":
        return socialRepository.findMineInProgress(ownerEntityId, now, pageable);
      case "close":
        return socialRepository.findMineCompleted(ownerEntityId, now, pageable);
      default:
        if (order.equals("popularity")) {
          return socialRepository.findMineOrderByPopularity(ownerEntityId, pageable);
        }
        return socialRepository.findMineDefault(ownerEntityId, pageable);
    }
  }

  private String parseKeyword(final String keyword) {
    return (keyword == null || keyword.isEmpty()) ? "" : keyword;
  }
}
