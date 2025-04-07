package com.barbaragama.votingchallenge.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "agenda")
@Schema(description = "Agenda entity representing a voting agenda")
public class Agenda {
    @Id
    @GeneratedValue
    @Schema(description = "Unique identifier for the agenda", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotBlank(message = "Title cannot be blank")
    @Schema(description = "Title of the agenda", example = "Vote on new project")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Column(length = 1000)
    @Schema(description = "Description of the agenda", example = "Vote on the new project proposal for 2025")
    private String description;

    @CreationTimestamp
    @Schema(description = "Creation timestamp of the agenda", example = "2023-10-01T12:00:00")
    private LocalDateTime createdAt;

    public Agenda(UUID id) {
        this.id = id;
    }

    public Agenda(String title, String description) {
        this.title = title;
        this.description = description;
    }
}