package com.barbaragama.votingchallenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Data
@Schema(description = "DTO for voting request")
public class VoteRequestDTO {

    @NotNull(message = "Session ID cannot be null")
    @Schema(description = "ID of the voting session", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID sessionId;

    @NotBlank(message = "Option cannot be blank")
    @Schema(description = "Voting option", example = "YES")
    private String option;

    @NotBlank(message = "CPF cannot be blank")
    @CPF(message = "Invalid CPF format")
    @Schema(description = "CPF of the associate", example = "123.456.789-09")
    private String cpf;

}
