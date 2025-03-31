package com.barbaragama.votingchallenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for Vote")
public class VoteResponseDTO {

    @Schema(description = "ID of the vote", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID associateId;

    @Schema(description = "ID of the session", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID sessionId;

    @Schema(description = "Vote option", example = "YES")
    private String option;

    @Schema(description = "Message indicating the result of the vote", example = "Vote successfully registered")
    private String message;
}