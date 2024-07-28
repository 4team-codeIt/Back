package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SocialCreateResponse(
    @Schema(description = "모임 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Long id,

    @Schema(description = "모임 생성 성공 메시지", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String message
) {
}