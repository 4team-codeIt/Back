package com.brick.demo.image.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageFileExtension {
	JPEG("jpeg"),
	JPG("jpg"),
	PNG("png");

	private final String uploadExtension;

//	public static ImageFileExtension fromFilename(String filename) {
//		String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
//		for (ImageFileExtension extension : values()) {
//			if (extension.getUploadExtension().equals(ext)) {
//				return extension;
//			}
//		}
//		return null; // 지원되지 않는 파일 형식일 경우 null 반환
//	}
}