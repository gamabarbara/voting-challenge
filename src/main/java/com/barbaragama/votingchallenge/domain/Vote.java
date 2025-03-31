package com.barbaragama.votingchallenge.domain;

import com.barbaragama.votingchallenge.enums.VoteOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"associate_id", "session_id"})
})
@Schema(description = "Represents a vote cast by an associate in a voting session.")
public class Vote {
    @Id
    @GeneratedValue
    @Schema(description = "Unique identifier for the vote.", example = "e0c8f1b2-3d4e-4a5b-8c6f-7a8b9c0d1e2f")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "associate_id")
    @Schema(description = "The associate who cast the vote.")
    private Associate associate;

    @ManyToOne
    @JoinColumn(name = "session_id")
    @Schema(description = "The voting session in which the vote was cast.")
    private Session session;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The option chosen by the associate in the vote.", example = "YES")
    private VoteOption option;

    @CreationTimestamp
    @Schema(description = "The timestamp when the vote was cast.")
    private LocalDateTime votedAt;
}
