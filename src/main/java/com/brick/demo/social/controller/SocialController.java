package com.brick.demo.social.controller;

import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialDetailResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialUpdateRequest;
import com.brick.demo.social.service.SocialService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("socials")
@RequiredArgsConstructor
public class SocialController implements SocialControllerDocs {

	private final SocialService socialService;

  @GetMapping
  public ResponseEntity<List<SocialResponse>> selectSocials(
      @RequestParam(required = false) final String orderBy) {
    List<SocialResponse> response = socialService.selectSocials(orderBy);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SocialResponse> selectSocialById(@PathVariable final Long id) {
    SocialResponse response = socialService.selectSocialById(id);

		return ResponseEntity.ok(response);
	}

  @PostMapping
  public ResponseEntity<SocialCreateResponse> createSocial(
      @RequestBody @Valid final SocialCreateRequest dto) {
    SocialCreateResponse response = socialService.createSocial(dto);

		return ResponseEntity.ok(response);
	}

  @PatchMapping("/{id}")
  public ResponseEntity<String> updateSocial(
      @PathVariable final Long id, @RequestBody @Valid final SocialUpdateRequest dto) {
    socialService.updateSocial(id, dto);

		return ResponseEntity.ok("모임을 성공적으로 수정했습니다");
	}

  @DeleteMapping("/{id}")
  public ResponseEntity<String> cancelSocial(@PathVariable final Long id) {
    socialService.cancelSocial(id);

    return ResponseEntity.ok("모임을 성공적으로 취소했습니다");
  }

  @GetMapping("/{id}/details")
  public ResponseEntity<SocialDetailResponse> selectDetailBySocialId(@PathVariable final Long id) {
    SocialDetailResponse response = socialService.selectDetailBySocialId(id);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/{id}/participants")
  public ResponseEntity<String> joinSocial(@PathVariable final Long id) {
    socialService.joinSocial(id);

    return ResponseEntity.ok("모임에 성공적으로 참여했습니다");
  }

  @DeleteMapping("/{id}/participants")
  public ResponseEntity<String> leaveSocial(@PathVariable final Long id) {
    socialService.leaveSocial(id);

    return ResponseEntity.ok("모임을 성공적으로 나왔습니다");
  }
}
