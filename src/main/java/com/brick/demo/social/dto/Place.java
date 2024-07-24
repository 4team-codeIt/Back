package com.brick.demo.social.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record Place(
    @Schema(description = "주소", example = "서울시 강남구 테헤란로 123", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String address,

    @Schema(description = "상세 주소", example = "456동 789호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String detailAddress,

    @Schema(description = "모임이 열리는 장소의 위도", example = "37.5665", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Double latitude,

    @Schema(description = "모임이 열리는 장소의 경도", example = "126.978", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull Double longitude
) {
}
