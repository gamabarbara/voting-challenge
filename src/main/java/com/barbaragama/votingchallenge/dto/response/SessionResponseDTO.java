package com.barbaragama.votingchallenge.dto.response;

import com.barbaragama.votingchallenge.enums.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "Session response DTO")
public class SessionResponseDTO {
    @Schema(description = "Session ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Agenda ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID agendaId;

    @Schema(description = "Session start time", example = "2023-10-01T10:00:00")
    private LocalDateTime startTime;

    @Schema(description = "Session end time", example = "2023-10-01T11:00:00")
    private LocalDateTime endTime;

    @Schema(description = "Session status", example = "OPEN")
    private SessionStatus sessionStatus;

    @Schema(description = "Session duration in minutes", example = "60")
    private long durationMinutes;
}
