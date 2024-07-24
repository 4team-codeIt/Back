package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;

public record ParticipantCount(
    @Schema(description = "최소 인원 수", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Integer min,

    @Schema(description = "최대 인원 수", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Integer max,

    @Schema(description = "현재 인원 수", requiredMode = RequiredMode.NOT_REQUIRED)
    Integer current
) {
}
