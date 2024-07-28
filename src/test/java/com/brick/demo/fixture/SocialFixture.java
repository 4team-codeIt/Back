package com.brick.demo.fixture;

import com.brick.demo.social.dto.ParticipantCount;
import com.brick.demo.social.dto.Place;
import com.brick.demo.social.dto.SocialCreateRequest;
import java.time.LocalDateTime;

public class SocialFixture {
  private static final String ADDRESS = "서울시 강남구 테헤란로 123";
  private static final String DETAIL_ADDRESS = "456동 789호";
  private static final Double LATITUDE = 37.5665;
  private static final Double LONGITUDE = 126.978;

  public static final LocalDateTime NOW = LocalDateTime.now();
  public static final LocalDateTime YESTERDAY = LocalDateTime.now().minusDays(1);
  public static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
  public static final LocalDateTime ONE_MONTH_AGO = LocalDateTime.now().minusMonths(1);
  public static final LocalDateTime ONE_MONTH_LATER = LocalDateTime.now().plusMonths(1);

  public static final Place PLACE = new Place(ADDRESS, DETAIL_ADDRESS, LATITUDE, LONGITUDE);
  public static final ParticipantCount PARTICIPANT_COUNT = new ParticipantCount(2, 5);

  public static final SocialCreateRequest TODAY_SOCIAL =
      new SocialCreateRequest("오늘 모임", "오늘 진행하는 모임입니다.", NOW, PARTICIPANT_COUNT, PLACE);
  public static final SocialCreateRequest PAST_SOCIAL =
      new SocialCreateRequest(
          "한달 전 모임", "한달 전에 진행했던 모임입니다.", ONE_MONTH_AGO, PARTICIPANT_COUNT, PLACE);
  public static final SocialCreateRequest FUTURE_SOCIAL =
      new SocialCreateRequest(
          "한달 후 모임", "한달 후에 진행할 모임입니다.", ONE_MONTH_LATER, PARTICIPANT_COUNT, PLACE);
}
