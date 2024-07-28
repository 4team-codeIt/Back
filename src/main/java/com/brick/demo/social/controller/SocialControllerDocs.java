package com.brick.demo.social.controller;

import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialDetailResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "모임", description = "모임 조회, 참여, 취소와 관련된 그룹입니다.")
public interface SocialControllerDocs {

  @Operation(summary = "모임 전체 조회", description = "전체 모임을 조회합니다.")
  @ApiResponse(responseCode = "200")
  @GetMapping
  ResponseEntity<List<SocialResponse>> selectSocials(
      @RequestParam(required = false) final String orderBy);

  @Operation(summary = "특정 모임 조회", description = "해당 아이디의 모임을 조회합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(
        responseCode = "404",
        description = "Not Found - 존재하지 않는 모임 아이디",
        content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/{id}")
  ResponseEntity<SocialResponse> selectSocialById(
      @Schema(description = "조회하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED) @PathVariable
          final Long id);

  @Operation(summary = "모임 생성", description = "모임을 생성합니다.")
  @ApiResponse(responseCode = "200")
  @PostMapping
  ResponseEntity<SocialCreateResponse> createSocial(
      @RequestBody @Valid final SocialCreateRequest dto);

  @Operation(summary = "특정 모임 수정", description = "해당 아이디의 모임을 수정합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - 주최자 권한이 없음",
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "404",
        description = "Not Found - 존재하지 않는 모임 아이디",
        content = @Content(schema = @Schema(hidden = true)))
  })
  @PatchMapping("/{id}")
  ResponseEntity<String> updateSocial(
      @Schema(description = "수정하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED) @PathVariable
          Long id,
      @RequestBody @Valid final SocialUpdateRequest dto);

  @Operation(summary = "특정 모임 취소", description = "해당 아이디의 모임을 취소합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - 주최자 권한이 없음",
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "404",
        description = "Not Found - 존재하지 않는 모임 아이디",
        content = @Content(schema = @Schema(hidden = true))),
  })
  @DeleteMapping("/{id}")
  ResponseEntity<String> cancelSocial(
      @Schema(description = "취소하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED) @PathVariable
          final Long id);

  @Operation(summary = "모임 상세 정보 조회", description = "모임의 상세 정보를 조회합니다.")
  @ApiResponse(responseCode = "200")
  @GetMapping("/{id}/details")
  ResponseEntity<SocialDetailResponse> selectDetailBySocialId(
      @Schema(description = "상세 정보 조회하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED)
          @PathVariable
          final Long id);

  @Operation(summary = "특정 모임 참여", description = "해당 아이디의 모임에 참여합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request - 이미 참여한 모임이거나 더 이상 인원을 수용할 수 없음",
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "404",
        description = "Not Found - 존재하지 않는 모임 아이디",
        content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping("/{id}/participants")
  ResponseEntity<String> joinSocial(
      @Schema(description = "참여하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED) @PathVariable
          final Long id);

  @Operation(summary = "특정 모임 참여 취소", description = "해당 아이디의 모임에 참여를 취소합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request - 이미 지났거나 참여하지 않은 모임",
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "403",
        description = "Forbidden - 모임 주최자의 참여 취소 시도",
        content = @Content(schema = @Schema(hidden = true))),
    @ApiResponse(
        responseCode = "404",
        description = "Not Found - 존재하지 않는 모임 아이디",
        content = @Content(schema = @Schema(hidden = true)))
  })
  @DeleteMapping("/{id}/participants")
  ResponseEntity<String> leaveSocial(
      @Schema(description = "참여 취소하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED) @PathVariable
          final Long id);
}
