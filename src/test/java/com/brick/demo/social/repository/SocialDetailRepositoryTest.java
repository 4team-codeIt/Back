package com.brick.demo.social.repository;

import static com.brick.demo.fixture.AccountFixture.ACCOUNT;
import static com.brick.demo.fixture.SocialFixture.PLACE;
import static com.brick.demo.fixture.SocialFixture.TODAY_SOCIAL;
import static org.assertj.core.api.Assertions.assertThat;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialDetail;
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
    Account account = accountRepository.save(ACCOUNT);

    Social social = Social.save(account, TODAY_SOCIAL);
    socialRepository.save(social);

    socialDetailRepository.save(SocialDetail.save(social, TODAY_SOCIAL));
    SocialDetail detail = socialDetailRepository.findBySocialId(social.getId()).get();

    assertThat(detail.getGeoLocation()).isEqualTo(PLACE.latitude() + " " + PLACE.longitude());
  }
}
