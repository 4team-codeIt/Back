package com.brick.demo.social.controller;

import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialUpdateRequest;
import com.brick.demo.social.entity.Social;
import com.brick.demo.social.service.SocialService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("social")
@RequiredArgsConstructor
public class SocialController implements SocialControllerDocs {

  private final SocialService socialService;

  @GetMapping
  public List<Social> findAll() {
    return socialService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<SocialResponse> findById(@PathVariable final Long id) {
    SocialResponse response = socialService.findById(id);

    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<SocialCreateResponse> save(
      @RequestBody @Valid final SocialCreateRequest dto) {
    SocialCreateResponse response = socialService.save(dto);

    return ResponseEntity.ok(response);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<String> update(
      @PathVariable final Long id, @RequestBody @Valid final SocialUpdateRequest dto) {
    socialService.update(id, dto);

    return ResponseEntity.ok("모임을 성공적으로 수정했습니다");
  }

  // TODO 불필요한 경우 추후 삭제
  //  @DeleteMapping("/{id}")
  //  public ResponseEntity<Void> delete(@PathVariable final Long id) {
  //    socialService.delete(id);
  //    return null;
  //  }
}
