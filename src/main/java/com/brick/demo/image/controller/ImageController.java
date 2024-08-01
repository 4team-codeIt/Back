package com.brick.demo.image.controller;

import com.brick.demo.image.enums.ImageFileExtension;
//import com.brick.demo.image.dto.PresignedUrlRequest;
import com.brick.demo.image.dto.PresignedUrlResponse;
import com.brick.demo.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@GetMapping("/auth/users/images")
	public PresignedUrlResponse upload(@RequestParam ImageFileExtension imageFileExtension) {
		return imageService.createUserProfileImagePresignedUrl(imageFileExtension);
	}

	@GetMapping("/socials/{id}/images")
	public PresignedUrlResponse createSocilaImagePresignedUrl(@PathVariable final Long id,
			@RequestParam ImageFileExtension imageFileExtension) {
		return imageService.createSocialImagePresignedUrl(id, imageFileExtension);
	}
}
