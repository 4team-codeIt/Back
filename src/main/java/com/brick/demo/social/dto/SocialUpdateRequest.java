package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SocialUpdateRequest(
    @Schema(description = "모임 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String name,

    @Schema(description = "모임 소개", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String description,

    @Schema(description = "장소 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid Place place,

    @Schema(description = "이미지 URL 배열")
    List<String> imageUrls
) {
}
