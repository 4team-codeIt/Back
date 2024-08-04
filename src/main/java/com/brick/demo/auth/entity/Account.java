package com.brick.demo.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Account {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long entityId;

	@NotEmpty(message = "Name is required")
	@Column(nullable = false, unique = true)
	private String name;

	@NotEmpty(message = "Email is required")
	@Column(nullable = false, unique = true)
	private String email;

	@NotEmpty(message = "Password is required")
	@Column(length = 60, nullable = false)
	private String password;

	private LocalDate birthday;

	private String introduce;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "oauth_provider")
	private OAuthProvider oauthProvider;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public Account(String name, String email, String password, OAuthProvider oauthProvider) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.oauthProvider = oauthProvider;
	}

	public void update(LocalDate birthday, String introduce, String profileImageUrl) {
		this.birthday = birthday;
		this.introduce = introduce;
		this.profileImageUrl = profileImageUrl;
	}

	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		this.email = this.email + ".deleted." + this.deletedAt;
		this.name = this.name + ".deleted." + this.deletedAt;
	}
}