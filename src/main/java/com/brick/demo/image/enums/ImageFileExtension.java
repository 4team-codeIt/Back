package com.brick.demo.image.enums;

import com.brick.demo.common.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageFileExtension {
	JPEG("jpeg"),
	JPG("jpg"),
	PNG("png"),
	WEBP("webp");

	private final String uploadExtension;

}