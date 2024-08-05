package com.brick.demo.social.dto.response;

import com.brick.demo.social.entity.Social;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public record SocialResponses(
    @Schema(description = "모임 전체 개수", requiredMode = RequiredMode.REQUIRED)
    Long totalElement,

    @Schema(description = "모임 전체 페이지", requiredMode = RequiredMode.REQUIRED)
    Integer totalPages,

    @Schema(description = "모임 현재 페이지", requiredMode = RequiredMode.REQUIRED)
    Integer currentPage,

    @Schema(description = "모임 목록", requiredMode = RequiredMode.REQUIRED)
    @Valid List<SocialResponse> socials
) {
    public static SocialResponses from(Page<Social> page) {
        return new SocialResponses(
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.stream().map(SocialResponse::from).collect(Collectors.toList())
        );
    }
}
