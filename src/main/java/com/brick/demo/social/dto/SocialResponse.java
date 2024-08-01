package com.brick.demo.social.dto;

import com.brick.demo.social.entity.Social;
import com.brick.demo.social.enums.ParticipantRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record SocialResponse(
    @Schema(description = "모임 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Long id,

    @Schema(description = "모임 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String name,

    @Schema(description = "모임 일정", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty LocalDateTime gatheringDate,

    @Schema(description = "장소", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String address,

    @Schema(description = "모임 정원", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid ParticipantCount participantCount,

    @Schema(description = "썸네일 URL")
    String thumbnail,

    @Schema(description = "태그", requiredMode = Schema.RequiredMode.REQUIRED)
    List<String> tags,

    @Schema(description = "주최자", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid Participant owner
) {

    public static SocialResponse fromEntity(Social social) {
        ParticipantCount participantCount =
            new ParticipantCount(
                social.getMinCount(), social.getMaxCount(), social.getParticipants().size());

        String thumbnail = social.getImageUrls().split(",")[0];

        Participant owner = new Participant(
            social.getOwner().getEntityId(),
            social.getOwner().getName(),
            "TODO 프로필 URL",
            ParticipantRole.OWNER.name(),
            social.getOwner().getIntroduce());

        return new SocialResponse(
            social.getId(),
            social.getName(),
            social.getGatheringDate(),
            social.getAddress(),
            participantCount,
            thumbnail,
            List.of(social.getTags().split(",")),
            owner
        );
    }
}
