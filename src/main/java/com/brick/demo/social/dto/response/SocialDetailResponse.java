package com.brick.demo.social.dto.response;

import com.brick.demo.social.dto.common.Participant;
import com.brick.demo.social.dto.common.ParticipantCount;
import com.brick.demo.social.dto.common.SocialIntroduction;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialDetail;
import com.brick.demo.social.enums.Delimiter;
import com.brick.demo.social.enums.ParticipantRole;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record SocialDetailResponse(
		@Schema(description = "모임 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull Long id,

		@Schema(description = "모임 이름", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotEmpty String name,

		@Schema(description = "모임 취소 여부", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull Boolean canceled,

		@Schema(description = "모임 소개", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotEmpty String description,

		@Schema(description = "모임 일정", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotEmpty LocalDateTime gatheringDate,

		@Schema(description = "모임 정원", requiredMode = Schema.RequiredMode.REQUIRED)
		@Valid ParticipantCount participantCount,

		@Schema(description = "이미지 URL 배열")
		List<String> imageUrls,

		@Schema(description = "활동비", requiredMode = RequiredMode.NOT_REQUIRED)
		Integer dues,

		@Schema(description = "태그", requiredMode = Schema.RequiredMode.REQUIRED)
		List<String> tags,

		@Schema(description = "주최자", requiredMode = RequiredMode.REQUIRED)
		@Valid Participant owner,

		@Schema(description = "참여자", requiredMode = RequiredMode.REQUIRED)
		@Valid List<Participant> participants,

		@Schema(description = "모임 소개 세부 정보", requiredMode = RequiredMode.REQUIRED)
		@Valid SocialIntroduction introduction
) {

	public static SocialDetailResponse from(final Social social, final SocialDetail detail) {
		ParticipantCount participantCount =
				new ParticipantCount(
						social.getMinCount(), social.getMaxCount(), social.getParticipants().size());

		Participant owner = Participant.from(social.getOwner(), ParticipantRole.OWNER);

		return new SocialDetailResponse(
				social.getId(),
				social.getName(),
				social.getCanceled() == null ? false : social.getCanceled(),
				detail.getDescription(),
				social.getGatheringDate(),
				participantCount,
				List.of(social.getImageUrls().split(Delimiter.IMAGE_URLS.value())),
				social.getDues(),
				List.of(social.getTags().split(Delimiter.TAGS.value())),
				owner,
				makeParticipants(social, owner),
				SocialIntroduction.from(social.getAddress(), detail)
		);
	}

	private static List<Participant> makeParticipants(final Social social, final Participant owner) {
		List<Participant> participants = social.getParticipants().stream()
				.filter(participant -> !participant.getAccount().getEntityId().equals(social.getOwner().getEntityId()))
				.map(participant -> Participant.from(participant.getAccount(), ParticipantRole.PARTICIPANT))
				.collect(Collectors.toList());
		participants.add(owner);
		return participants;
	}
}
