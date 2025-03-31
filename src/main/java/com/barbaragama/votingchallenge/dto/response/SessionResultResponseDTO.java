package com.barbaragama.votingchallenge.dto.response;
import com.barbaragama.votingchallenge.enums.VotingResult;
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
@Schema(description = "Response DTO for session result")
public class SessionResultResponseDTO {

    @Schema(description = "Session ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID sessionId;

    @Schema(description = "Agenda ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID agendaId;

    @Schema(description = "Agenda title", example = "Vote on the new project")
    private String agendaTitle;

    @Schema(description = "Agenda description", example = "Discussion on the new project proposal")
    private long yesVotes;

    @Schema(description = "Agenda description", example = "Discussion on the new project proposal")
    private long noVotes;

    @Schema(description = "Votes total", example = "100")
    private long totalVotes;

    @Schema(description = "Voting result", example = "APPROVED")
    private VotingResult result;
}
