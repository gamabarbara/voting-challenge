package com.barbaragama.votingchallenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@Schema(description = "DTO for associate creation request")
@NoArgsConstructor
public class AssociateRequestDTO {
    @NotBlank(message = "Name is required")
    @Schema(description = "Name of the associate", example = "Maria da Silva")
    private String name;

    @NotBlank(message = "CPF is required")
    @Schema(description = "CPF of the associate", example = "12345678909")
    @CPF(message = "Invalid CPF format")
    private String cpf;

    public AssociateRequestDTO(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

}
