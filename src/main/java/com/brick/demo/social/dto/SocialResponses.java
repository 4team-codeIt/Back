package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import java.util.List;

public record SocialResponses(
    @Schema(description = "모임 전체 개수", requiredMode = RequiredMode.REQUIRED)
    Long totalCount,

    @Schema(description = "모임 목록", requiredMode = RequiredMode.REQUIRED)
    @Valid List<SocialResponse> socials
) {
}
