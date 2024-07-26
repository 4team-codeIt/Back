package com.brick.demo.social.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.social.dto.ParticipantCount;
import com.brick.demo.social.dto.Place;
import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.entity.Social;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@DataJpaTest
@SpringJUnitConfig
class SocialRepositoryTest {

  @Autowired private AccountRepository accountRepository;

  @Autowired private SocialRepository socialRepository;

  @BeforeEach
  void 테스트_데이터_삽입() {
    Account account =
        accountRepository.save(
            Account.builder().name("tester").email("test@example.com").password("passwd").build());

    LocalDateTime now = LocalDateTime.now();
    Place place = new Place("서울시 강남구 테헤란로 123", "456동 789호", 37.5665, 126.978);
    ParticipantCount participantCount = new ParticipantCount(2, 5);

    SocialCreateRequest dto1 =
        new SocialCreateRequest("오늘 모임", "오늘 진행하는 모임입니다.", now, participantCount, place);
    Social social1 = Social.save(account, dto1);

    SocialCreateRequest dto2 =
        new SocialCreateRequest(
            "한달 후 모임", "한달 후에 진행할 모임입니다.", now.plusMonths(1), participantCount, place);
    Social social2 = Social.save(account, dto2);

    SocialCreateRequest dto3 =
        new SocialCreateRequest(
            "한달 전 모임", "한달 전에 진행했던 모임입니다.", now.minusMonths(1), participantCount, place);
    Social social3 = Social.save(account, dto3);

    socialRepository.save(social1);
    socialRepository.save(social2);
    socialRepository.save(social3);
  }

  @Test
  void 모임_일자_내림차순으로_모임을_조회한다() {
    List<Social> socials = socialRepository.findAllByOrderByGatheringDateDesc();

    assertThat(socials).hasSize(3);
    assertThat(socials.get(0).getName()).isEqualTo("한달 후 모임");
    assertThat(socials.get(2).getName()).isEqualTo("한달 전 모임");
  }

  @Test
  void 내일_기준_이전의_모임을_조회한다() {
    LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
    List<Social> socials =
        socialRepository.findAllByGatheringDateBeforeOrderByGatheringDateDesc(tomorrow);

    assertThat(socials).hasSize(2);
    assertThat(socials.get(0).getName()).isEqualTo("오늘 모임");
    assertThat(socials.get(1).getName()).isEqualTo("한달 전 모임");
  }

  @Test
  void 어제_기준_이후의_모임을_조회한다() {
    LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
    List<Social> socials =
        socialRepository.findAllByGatheringDateAfterOrderByGatheringDateDesc(yesterday);

    assertThat(socials).hasSize(2);
    assertThat(socials.get(0).getName()).isEqualTo("한달 후 모임");
    assertThat(socials.get(1).getName()).isEqualTo("오늘 모임");
  }
}
