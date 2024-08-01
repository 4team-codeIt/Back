package com.brick.demo.social.dto;

import com.brick.demo.social.entity.SocialDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SocialIntroduction(
    @Schema(description = "모임 소개", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty String description,

    @Schema(description = "장소 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid Place place
) {

    public static SocialIntroduction fromEntities(final String fullAddress, final SocialDetail detail) {
        String[] addresses = fullAddress.split(",");
        String[] geoLocations = detail.getGeoLocation().split(" ");

        Place place = new Place(addresses[0], addresses[1], Double.parseDouble(geoLocations[0]), Double.parseDouble(geoLocations[1]));

        return new SocialIntroduction(detail.getDescription(), place);
    }
}
