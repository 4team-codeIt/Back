package com.brick.demo.social.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.social.dto.ParticipantCount;
import com.brick.demo.social.dto.Place;
import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialDetail;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@DataJpaTest
@SpringJUnitConfig
class SocialDetailRepositoryTest {

  @Autowired private AccountRepository accountRepository;

  @Autowired private SocialRepository socialRepository;

  @Autowired private SocialDetailRepository socialDetailRepository;

  @Test
  void 모임_아이디에_해당하는_모임_상세_정보를_조회한다() {
    Account account =
        accountRepository.save(
            Account.builder().name("tester").email("test@example.com").password("passwd").build());

    Place place = new Place("서울시 강남구 테헤란로 123", "456동 789호", 37.5665, 126.978);
    ParticipantCount participantCount = new ParticipantCount(2, 5);
    SocialCreateRequest dto =
        new SocialCreateRequest(
            "오늘 모임", "오늘 진행하는 모임입니다.", LocalDateTime.now(), participantCount, place);
    Social social = Social.save(account, dto);
    socialRepository.save(social);

    socialDetailRepository.save(SocialDetail.save(social, dto));
    SocialDetail detail = socialDetailRepository.findBySocialId(social.getId()).get();

    assertThat(detail.getGeoLocation()).isEqualTo("37.5665 126.978");
  }
}
