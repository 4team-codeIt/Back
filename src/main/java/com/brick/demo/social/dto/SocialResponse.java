package com.brick.demo.social.dto;

import com.brick.demo.social.entity.Social;
import com.brick.demo.social.enums.Delimiter;
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

    @Schema(description = "모임 취소 여부", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Boolean canceled,

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

    public static SocialResponse from(Social social) {
        return new SocialResponse(
            social.getId(),
            social.getName(),
            social.getCanceled() == null ? false : social.getCanceled(),
            social.getGatheringDate(),
            social.getAddress().replace(Delimiter.ADDRESS.value(), Delimiter.ADDRESS_REPLACE.value()),
            ParticipantCount.from(social),
            thumbnailFrom(social.getImageUrls()),
            List.of(social.getTags().split(Delimiter.TAGS.value())),
            Participant.from(social.getOwner(), ParticipantRole.OWNER)
        );
    }

    private static String thumbnailFrom(final String imageUrls) {
        return imageUrls.split(Delimiter.IMAGE_URLS.value())[0];
    }
}
