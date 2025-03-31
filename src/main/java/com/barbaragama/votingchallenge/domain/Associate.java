package com.barbaragama.votingchallenge.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "associate")
@Schema(description = "Represents an associate in the voting system.")
public class Associate {
    @Id
    @GeneratedValue
    @Schema(description = "Unique identifier of the associate.", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Name of the associate.", example = "Maria da Silva")
    private String name;

    @CPF(message = "Invalid CPF format")
    @NotBlank(message = "CPF is mandatory")
    @Column(unique = true)
    @Schema(description = "CPF of the associate.", example = "123.456.789-09")
    private String cpf;
}
