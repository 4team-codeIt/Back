package com.brick.demo.social.controller;

import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialUpdateRequest;
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
import org.springframework.web.bind.annotation.RequestParam;

public interface SocialControllerDocs {

  @Operation(summary = "모임 전체 조회", description = "전체 모임을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @GetMapping
  ResponseEntity<List<SocialResponse>> findAll(
      @RequestParam(required = false) final String orderBy);

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

  @Operation(summary = "특정 모임 수정", description = "해당 아이디의 모임을 수정합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "수정 성공"),
    @ApiResponse(responseCode = "403", description = "수정 실패 - 주최자 권한이 없음"),
    @ApiResponse(responseCode = "404", description = "수정 실패 - 존재하지 않는 모임 아이디")
  })
  @PatchMapping("/{id}")
  ResponseEntity<String> update(
      @PathVariable Long id, @RequestBody @Valid final SocialUpdateRequest dto);

  //  @Operation(summary = "특정 모임 취소", description = "해당 아이디의 모임을 취소합니다.")
  //  @ApiResponses({
  //    @ApiResponse(responseCode = "200", description = "취소 성공"),
  //    @ApiResponse(responseCode = "403", description = "취소 실패 - 주최자 권한이 없음"),
  //    @ApiResponse(responseCode = "404", description = "취소 실패 - 존재하지 않는 모임 아이디")
  //  })
  //  @DeleteMapping("/{id}")
  //  ResponseEntity<String> delete(@PathVariable final Long id);
  //
  //  @Operation(summary = "모임 상세 정보 조회", description = "모임의 상세 정보를 조회합니다.")
  //  @ApiResponse(responseCode = "200", description = "조회 성공")
  //  @GetMapping("/{id}/details")
  //  ResponseEntity<SocialDetailResponse> findDetailBySocialId(@PathVariable final Long id);
  //
  //  @Operation(summary = "특정 모임 참여", description = "해당 아이디의 모임에 참여합니다.")
  //  @ApiResponses({
  //    @ApiResponse(responseCode = "200", description = "참여 성공"),
  //    @ApiResponse(responseCode = "400", description = "참여 실패 - 이미 참여한 모임이거나 더 이상 인원을 수용할 수 없음"),
  //    @ApiResponse(responseCode = "401", description = "참여 실패 - 로그인하지 않은 사용자"),
  //    @ApiResponse(responseCode = "404", description = "참여 실패 - 존재하지 않는 모임 아이디")
  //  })
  //  @PostMapping("/{id}/participants")
  //  ResponseEntity<String> saveParticipant(@PathVariable final Long id);
  //
  //  @Operation(summary = "특정 모임 참여 취소", description = "해당 아이디의 모임에 참여를 취소합니다.")
  //  @ApiResponses({
  //    @ApiResponse(responseCode = "200", description = "취소 성공"),
  //    @ApiResponse(responseCode = "400", description = "취소 실패 - 이미 지났거나 참여하지 않은 모임"),
  //    @ApiResponse(responseCode = "401", description = "취소 실패 - 로그인하지 않은 사용자"),
  //    @ApiResponse(responseCode = "404", description = "취소 실패 - 존재하지 않는 모임 아이디")
  //  })
  //  @DeleteMapping("/{id}/participants")
  //  ResponseEntity<String> deleteParticipant(@PathVariable final Long id);
}
