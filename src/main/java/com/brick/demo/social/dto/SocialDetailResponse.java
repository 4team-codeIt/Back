package com.brick.demo.social.dto;

import com.brick.demo.social.entity.Social;
import com.brick.demo.social.entity.SocialDetail;
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
		@Valid List<Participant> participants
) {

	public static SocialDetailResponse fromEntities(final Social social, final SocialDetail detail) {
		ParticipantCount participantCount =
				new ParticipantCount(
						social.getMinCount(), social.getMaxCount(), social.getParticipants().size());

		Participant owner = new Participant(
				social.getOwner().getEntityId(),
				social.getOwner().getName(),
				"TODO",
				ParticipantRole.OWNER.name(),
				social.getOwner().getIntroduce());
		List<Participant> participants = makeParticipants(social, owner);

		return new SocialDetailResponse(
				social.getId(),
				social.getName(),
				detail.getDescription(),
				social.getGatheringDate(),
				participantCount,
				List.of(social.getImageUrls().split(",")),
				social.getDues(),
				List.of(social.getTags().split(",")),
				owner,
				participants
		);
	}

	private static List<Participant> makeParticipants(final Social social, final Participant owner) {
		List<Participant> participants = social.getParticipants().stream()
				.filter(participant -> !participant.getId().equals(social.getOwner().getEntityId()))
				.map(participant -> new Participant(
						participant.getAccount().getEntityId(),
						participant.getAccount().getName(),
						"TODO",
						ParticipantRole.PARTICIPANT.name(),
						participant.getAccount().getIntroduce()
				)).collect(Collectors.toList());
		participants.add(owner);
		return participants;
	}
}
