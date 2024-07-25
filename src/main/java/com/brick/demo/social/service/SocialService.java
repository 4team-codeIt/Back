package com.brick.demo.social.service;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountManager;
import com.brick.demo.common.CustomException;
import com.brick.demo.common.ErrorDetails;
import com.brick.demo.security.CustomUserDetails;
import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialUpdateRequest;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialDetail;
import com.brick.demo.social.repository.SocialDetailRepository;
import com.brick.demo.social.repository.SocialRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
  private final AccountManager accountManager;

  private final Sort sortByDate = Sort.by(Sort.Order.desc("gatheringDate"));

  // TODO 필터링 고도화
  public List<SocialResponse> findAll(final String orderBy) {
    if (orderBy.equals("popularity")) {
      return socialRepository.findAllOrderByPopularity().stream()
          .map(SocialResponse::fromEntity)
          .collect(Collectors.toList());
    }
    return socialRepository.findAll(sortByDate).stream()
        .map(SocialResponse::fromEntity)
        .collect(Collectors.toList());
  }

  public SocialResponse findById(Long id) throws CustomException {
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));

    return SocialResponse.fromEntity(social);
  }

  public SocialCreateResponse save(final SocialCreateRequest dto) {
    Account account = getAccount();
    Social social = socialRepository.save(Social.save(account, dto));
    SocialDetail detail = socialDetailRepository.save(SocialDetail.save(social, dto));
    // TODO 참가자 업데이트
    socialRepository.save(Social.updateDetail(social, detail));

    return new SocialCreateResponse(social.getId(), "모임을 성공적으로 생성하였습니다.");
  }

  public void update(final Long id, final SocialUpdateRequest dto) {
    Account account = getAccount();
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
    if (account.getName() != social.getOwner().getName()) {
      throw new CustomException(ErrorDetails.SOCIAL_NOT_FOUND);
    }
    socialRepository.save(Social.update(social, dto));
  }

  public void delete(Long id) {
    Account account = getAccount();
    Social social =
        socialRepository
            .findById(id)
            .orElseThrow(() -> new CustomException(ErrorDetails.SOCIAL_NOT_FOUND));
    if (account.getName() != social.getOwner().getName()) {
      throw new CustomException(ErrorDetails.SOCIAL_NOT_FOUND);
    }
    socialRepository.save(Social.cancel(social));
  }

  private Account getAccount() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    String name = userDetails.getName();
    return accountManager.getAccountByName(name).get();
  }
}
