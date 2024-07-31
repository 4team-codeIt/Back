package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SocialDetailIntroductionResponse (
    @Schema(description = "모임 소개", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String description,

    @Schema(description = "장소 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid Place place,

    @Schema(description = "참여자", requiredMode = RequiredMode.REQUIRED)
    @Valid List<String> participants
) {
}
