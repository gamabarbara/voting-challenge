package com.barbaragama.votingchallenge.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
@Schema(description = "DTO for session creation request")
public class SessionRequestDTO {

    @NotNull(message = "Agenda ID is required")
    @Schema(description = "ID of the agenda", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID agendaId;

    @Schema(description = "Duration of the session in minutes", example = "60")
    private long durationMinutes;
}
