package com.brick.demo.image.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PresignedUrlResponse {

	@Schema(description = "presignedUrl 주소 : 해당 주소로 이미지 등록")
	private final String presignedUrl;

	@Schema(description = "이미지 조회 가능 url")
	private final String imageUrl;

	@Schema(description = "이미지 파일명")
	private final String filename;

	public static PresignedUrlResponse of(String presignedUrl, String filename,
			String baseUrl) {
		return PresignedUrlResponse.builder()
				.presignedUrl(presignedUrl)
				.imageUrl(baseUrl + filename)
				.filename(filename)
				.build();
	}
}
