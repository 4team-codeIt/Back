package com.brick.demo.social.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "모임 참여", description = "모임 참여 및 취소와 관련된 그룹입니다.")
public interface SocialParticipantControllerDocs {

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
  @PostMapping
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
  @DeleteMapping
  ResponseEntity<String> leaveSocial(
      @Schema(description = "참여 취소하려는 모임 아이디", requiredMode = RequiredMode.REQUIRED) @PathVariable
          final Long id);
}
