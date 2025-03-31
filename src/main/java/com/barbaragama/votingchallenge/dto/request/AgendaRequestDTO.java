package com.barbaragama.votingchallenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO for agenda creation request")
public class AgendaRequestDTO {

    @NotBlank(message = "Title is required")
    @Schema(description = "Title of the agenda", example = "New Voting Agenda")
    private String title;

    @NotBlank(message = "Description is required")
    @Schema(description = "Description of the agenda", example = "This is a description of the new voting agenda.")
    private String description;
}
