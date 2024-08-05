// package com.brick.demo.social.repository;
//
// import static com.brick.demo.fixture.AccountFixture.ACCOUNT;
// import static com.brick.demo.fixture.SocialFixture.FUTURE_SOCIAL_CREATE_REQUEST;
// import static com.brick.demo.fixture.SocialFixture.PAST_SOCIAL_CREATE_REQUEST;
// import static com.brick.demo.fixture.SocialFixture.TODAY_SOCIAL_CREATE_REQUEST;
// import static com.brick.demo.fixture.SocialFixture.TOMORROW;
// import static com.brick.demo.fixture.SocialFixture.YESTERDAY;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.junit.jupiter.api.Assertions.assertAll;
//
// import com.brick.demo.auth.entity.Account;
// import com.brick.demo.auth.repository.AccountRepository;
// import com.brick.demo.social.entity.Social;
// import java.util.List;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
// @DataJpaTest
// @SpringJUnitConfig
// class SocialRepositoryTest {
//
//  @Autowired private AccountRepository accountRepository;
//
//  @Autowired private SocialRepository socialRepository;
//
//  private Account account;
//
//  @BeforeEach
//  void 테스트_데이터_삽입() {
//    account = accountRepository.save(ACCOUNT);
//
//    socialRepository.save(new Social(account, TODAY_SOCIAL_CREATE_REQUEST));
//    socialRepository.save(new Social(account, FUTURE_SOCIAL_CREATE_REQUEST));
//    socialRepository.save(new Social(account, PAST_SOCIAL_CREATE_REQUEST));
//  }
//
//  @Test
//  void 모임_생성일자_내림차순으로_모임을_조회한다() {
//    List<Social> socials = socialRepository.findAllByOrderByCreatedAtDesc();
//
//    assertAll(
//        () -> assertThat(socials).hasSize(3),
//        () -> assertThat(socials.get(0).getName()).isEqualTo(PAST_SOCIAL_CREATE_REQUEST.name()),
//        () -> assertThat(socials.get(2).getName()).isEqualTo(TODAY_SOCIAL_CREATE_REQUEST.name()));
//  }
//
//  @Test
//  void 모임_일자_내림차순으로_모임을_조회한다() {
//    List<Social> socials = socialRepository.findAllByOrderByGatheringDateDesc();
//
//    assertAll(
//        () -> assertThat(socials).hasSize(3),
//        () -> assertThat(socials.get(0).getName()).isEqualTo(FUTURE_SOCIAL_CREATE_REQUEST.name()),
//        () -> assertThat(socials.get(2).getName()).isEqualTo(PAST_SOCIAL_CREATE_REQUEST.name()));
//  }
//
//  @Test
//  void 내일_기준_이전의_모임을_조회한다() {
//    List<Social> socials =
//        socialRepository.findAllByGatheringDateBeforeOrderByGatheringDateDesc(TOMORROW);
//
//    assertAll(
//        () -> assertThat(socials).hasSize(2),
//        () -> assertThat(socials.get(0).getName()).isEqualTo(TODAY_SOCIAL_CREATE_REQUEST.name()),
//        () -> assertThat(socials.get(1).getName()).isEqualTo(PAST_SOCIAL_CREATE_REQUEST.name()));
//  }
//
//  @Test
//  void 어제_기준_이후의_모임을_조회한다() {
//    List<Social> socials =
//        socialRepository.findAllByGatheringDateAfterOrderByGatheringDateDesc(YESTERDAY);
//
//    assertAll(
//        () -> assertThat(socials).hasSize(2),
//        () -> assertThat(socials.get(0).getName()).isEqualTo(FUTURE_SOCIAL_CREATE_REQUEST.name()),
//        () -> assertThat(socials.get(1).getName()).isEqualTo(TODAY_SOCIAL_CREATE_REQUEST.name()));
//  }
//
//  @Test
//  void 취소된_모임을_생성일자_내림차순으로_조회한다() {
//    Social social = socialRepository.save(new Social(account, TODAY_SOCIAL_CREATE_REQUEST));
//    social.cancel();
//
//    List<Social> socials = socialRepository.findAllByCanceledTrueOrderByCreatedAtDesc();
//
//    assertThat(socials).hasSize(1);
//  }
//
//  @Test
//  void 특정_주최자의_모임을_생성일자_오름차순으로_조회한다() {
//    List<Social> socials =
//        socialRepository.findAllByOwnerEntityIdOrderByCreatedAtDesc(ACCOUNT.getEntityId());
//
//    assertThat(socials).hasSize(0);
//  }
// }
