package com.brick.demo.social.controller;

import com.brick.demo.social.dto.SocialCreateRequest;
import com.brick.demo.social.dto.SocialCreateResponse;
import com.brick.demo.social.dto.SocialDetailResponse;
import com.brick.demo.social.dto.SocialResponse;
import com.brick.demo.social.dto.SocialResponses;
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

@Tag(name = "모임", description = "모임 조회, 수정, 취소와 관련된 그룹입니다.")
public interface SocialControllerDocs {

  @Operation(summary = "모임 전체 조회", description = "전체 모임을 조회합니다.")
  @ApiResponse(responseCode = "200")
  @GetMapping
  ResponseEntity<SocialResponses> getSocials(
      @Schema(description = "offset", example = "0", requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(defaultValue = "0")
          final int offset,
      @Schema(description = "limit", example = "30", requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(defaultValue = "30")
          final int limit,
      @Schema(
              description = "모임 필터링 기준",
              pattern = "(open|close|cancel)?",
              allowableValues = {"open", "close", "cancel"},
              requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(required = false)
          final String filterBy,
      @Schema(
              description = "모임 정렬 기준",
              pattern = "(popularity)?",
              allowableValues = {"popularity"},
              requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(required = false)
          final String orderBy,
      @Schema(
              description = "찜한 모임 아이디 목록",
              example = "1,2,3",
              requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(required = false)
          final List<Long> ids);

  @Operation(summary = "내가 생성한 모임 조회", description = "내가 생성한 모임을 조회합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/me")
  ResponseEntity<SocialResponses> getMySocials(
      @Schema(description = "offset", example = "0", requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(defaultValue = "0")
          final int offset,
      @Schema(description = "limit", example = "30", requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(defaultValue = "30")
          final int limit,
      @Schema(
              description = "모임 필터링 기준",
              pattern = "(open|close|cancel)?",
              allowableValues = {"open", "close", "cancel"},
              requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(required = false)
          final String filterBy,
      @Schema(
              description = "모임 정렬 기준",
              pattern = "(popularity)?",
              allowableValues = {"popularity"},
              requiredMode = RequiredMode.NOT_REQUIRED)
          @RequestParam(required = false)
          final String orderBy);

  @Operation(summary = "특정 모임 조회", description = "해당 아이디의 모임을 조회합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(
        responseCode = "404",
        description = "Not Found - 존재하지 않는 모임 아이디",
        content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping("/{id}")
  ResponseEntity<SocialResponse> getSocialById(
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
  ResponseEntity<SocialDetailResponse> getDetailBySocialId(
      @Schema(description = "상세 정보 조회하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED)
          @PathVariable
          final Long id);
}
