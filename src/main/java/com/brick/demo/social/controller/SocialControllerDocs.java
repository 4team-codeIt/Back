package com.brick.demo.social.controller;

import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialUpdateRequest;
import com.brick.demo.social.entity.Social;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface SocialControllerDocs {

  @Operation(summary = "모임 전체 조회", description = "전체 모임을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @GetMapping
  List<Social> findAll();

  @Operation(summary = "특정 모임 조회", description = "해당 아이디의 모임을 조회합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "조회 성공"),
    @ApiResponse(responseCode = "404", description = "조회 실패 - 존재하지 않는 모임 아이디")
  })
  @GetMapping("/{id}")
  ResponseEntity<SocialResponse> findById(@PathVariable final Long id);

  @Operation(summary = "모임 생성", description = "모임을 생성합니다.")
  @ApiResponse(responseCode = "200", description = "모임 생성 성공")
  @PostMapping
  ResponseEntity<SocialCreateResponse> save(@Valid final SocialCreateRequest dto);

  @Operation(summary = "모임 수정", description = "모임을 수정합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "수정 성공"),
    @ApiResponse(responseCode = "403", description = "수정 실패 - 주최자 권한이 없음"),
    @ApiResponse(responseCode = "404", description = "수정 실패 - 존재하지 않는 모임 아이디")
  })
  @PatchMapping("/{id}")
  ResponseEntity<String> update(
      @PathVariable Long id, @RequestBody @Valid final SocialUpdateRequest dto);

  // TODO 불필요한 경우 추후 삭제
  //  @Operation(summary = "모임 삭제", description = "모임을 삭제합니다.")
  //  @ApiResponses({
  //    @ApiResponse(responseCode = "200", description = "삭제 성공"),
  //    @ApiResponse(responseCode = "403", description = "삭제 실패 - 주최자 권한이 없음"),
  //    @ApiResponse(responseCode = "404", description = "삭제 실패 - 존재하지 않는 모임 아이디")
  //  })
  //  @DeleteMapping("/{id}")
  //  ResponseEntity<Void> delete(@PathVariable final Long id);
}
