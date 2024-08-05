package com.brick.demo.social.dto.common;

import com.brick.demo.social.entity.Social;
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

    public ParticipantCount(final Integer min, final Integer max) {
        this(min, max, null);
    }

    public static ParticipantCount from(final Social social) {
        return new ParticipantCount(social.getMinCount(), social.getMaxCount(), social.getParticipants().size());
    }
}
