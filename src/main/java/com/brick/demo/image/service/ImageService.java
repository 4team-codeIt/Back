package com.brick.demo.image.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.brick.demo.auth.repository.AccountRepository;
import com.brick.demo.image.enums.ImageFileExtension;
import com.brick.demo.image.dto.PresignedUrlResponse;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final AmazonS3 amazonS3;
	private final AccountRepository accountRepository;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.base-url}")
	private String baseUrl;

	@Value("${cloud.aws.s3.folder-name.user-profile}")
	private String userProfileFolder;

	@Value("${cloud.aws.s3.folder-name.socials}")
	private String socialsFolder;

	public PresignedUrlResponse createUserProfileImagePresignedUrl(ImageFileExtension fileExtension) {
//		fileExtension.validate();
		String fixedFileExtension = fileExtension.getUploadExtension();
		String fileName = getFileNameInFolder(userProfileFolder, fixedFileExtension);
		URL url = amazonS3.generatePresignedUrl(
				getGeneratePresignedUrlRequest(bucket, fileName, fixedFileExtension));
		return PresignedUrlResponse.of(url.toString(), fileName, baseUrl);
	}

	public PresignedUrlResponse createSocialImagePresignedUrl(ImageFileExtension fileExtension) {
//		fileExtension.validate();
		String fixedFileExtension = fileExtension.getUploadExtension();
		LocalDate nowDate = LocalDate.now();
		String fileName = getFileNameInFolder(socialsFolder + nowDate + "/", fixedFileExtension);
		URL url = amazonS3.generatePresignedUrl(
				getGeneratePresignedUrlRequest(bucket, fileName, fixedFileExtension));
		return PresignedUrlResponse.of(url.toString(), fileName, baseUrl);
	}

	private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String bucket, String fileName,
			String fileExtension) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				bucket, fileName)
				.withMethod(HttpMethod.PUT)
				.withKey(fileName)
				.withContentType("image/" + fileExtension)
				.withExpiration(getPresignedUrlExpiration());
		return generatePresignedUrlRequest;
	}

	private Date getPresignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 3; // 3분
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	private String getFileNameInFolder(String folder, String fileExtension) {
		return folder + UUID.randomUUID() + "." + fileExtension;
	}

}
