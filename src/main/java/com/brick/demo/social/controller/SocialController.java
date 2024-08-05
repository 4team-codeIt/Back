package com.brick.demo.social.controller;

import com.brick.demo.social.dto.request.SocialCreateRequest;
import com.brick.demo.social.dto.request.SocialQuery;
import com.brick.demo.social.dto.request.SocialUpdateRequest;
import com.brick.demo.social.dto.response.SocialCreateResponse;
import com.brick.demo.social.dto.response.SocialDetailResponse;
import com.brick.demo.social.dto.response.SocialResponse;
import com.brick.demo.social.dto.response.SocialResponses;
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
  public ResponseEntity<SocialResponses> getSocials(
      @RequestParam(defaultValue = "0") final int offset,
      @RequestParam(defaultValue = "30") final int limit,
      @RequestParam(required = false) final String filterBy,
      @RequestParam(required = false) final String orderBy,
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final List<String> tags,
      @RequestParam(required = false) final List<Long> ids) {
    SocialResponses response =
        socialService.getSocials(
            new SocialQuery(offset, limit, filterBy, orderBy, name, tags, ids));

    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<SocialResponses> getMySocials(
      @RequestParam(defaultValue = "0") final int offset,
      @RequestParam(defaultValue = "30") final int limit,
      @RequestParam(required = false) final String filterBy,
      @RequestParam(required = false) final String orderBy) {
    SocialResponses response =
        socialService.getMySocials(SocialQuery.forMe(offset, limit, filterBy, orderBy));

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SocialResponse> getSocialById(@PathVariable final Long id) {
    SocialResponse response = socialService.getSocialById(id);

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
  public ResponseEntity<SocialDetailResponse> getDetailBySocialId(@PathVariable final Long id) {
    SocialDetailResponse response = socialService.getDetailBySocialId(id);

    return ResponseEntity.ok(response);
  }
}
