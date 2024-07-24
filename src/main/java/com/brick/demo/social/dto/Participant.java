package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record Participant(
    @Schema(description = "모임 참가자의 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty  String name,

    @Schema(description = "모임 참가자의 프로필")
    String profileUrl,

    @Schema(description = "모임 참가자의 역할")
    String role
) {
}
