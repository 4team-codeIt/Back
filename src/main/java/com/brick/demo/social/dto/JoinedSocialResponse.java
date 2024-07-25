package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record JoinedSocialResponse(
    @Schema(description = "모임 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Long id,

    @Schema(description = "모임 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String name,

    @Schema(description = "모임 일정", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty LocalDateTime gatheringDate,

    @Schema(description = "가입 일시", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty LocalDateTime joinedAt,

    @Schema(description = "장소", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String address,

    @Schema(description = "모임 정원", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid ParticipantCount participantCount,

    @Schema(description = "썸네일 URL")
    String thumbnail,

    @Schema(description = "태그", requiredMode = Schema.RequiredMode.REQUIRED)
    String[] tags,

    @Schema(description = "주최자", requiredMode = RequiredMode.REQUIRED)
    @Valid Participant owner,

    @Schema(description = "참여자", requiredMode = RequiredMode.REQUIRED)
    @Valid Participant[] participants
) {
}
