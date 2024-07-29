package com.brick.demo.social.repository;

import static com.brick.demo.fixture.AccountFixture.ACCOUNT;
import static com.brick.demo.fixture.SocialFixture.TODAY_SOCIAL_CREATE_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

import com.brick.demo.auth.entity.Account;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialParticipant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@DataJpaTest
@SpringJUnitConfig
class SocialParticipantRepositoryTest {

  @Autowired private AccountRepository accountRepository;

  @Autowired private SocialRepository socialRepository;

  @Autowired private SocialParticipantRepository socialParticipantRepository;

  @Test
  void 모임_참여_이력을_찾아_모임_참가를_취소한다() {
    Account account = accountRepository.save(ACCOUNT);
    Social social = socialRepository.save(new Social(account, TODAY_SOCIAL_CREATE_REQUEST));

    SocialParticipant participant = new SocialParticipant(social, account);
    socialParticipantRepository.save(participant);

    socialParticipantRepository.deleteAllBySocialIdAndAccountEntityId(
        social.getId(), account.getEntityId());

    assertThat(socialParticipantRepository.findById(participant.getId())).isEmpty();
  }
}
