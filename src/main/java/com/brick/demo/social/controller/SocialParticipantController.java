package com.brick.demo.social.controller;

import com.brick.demo.social.service.SocialParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("socials/{id}/participants")
@RequiredArgsConstructor
public class SocialParticipantController implements SocialParticipantControllerDocs {

  private final SocialParticipantService socialParticipantService;

  @PostMapping
  public ResponseEntity<String> joinSocial(@PathVariable final Long id) {
    socialParticipantService.joinSocial(id);

    return ResponseEntity.ok("모임에 성공적으로 참여했습니다");
  }

  @DeleteMapping
  public ResponseEntity<String> leaveSocial(@PathVariable final Long id) {
    socialParticipantService.leaveSocial(id);

    return ResponseEntity.ok("모임을 성공적으로 나왔습니다");
  }
}
