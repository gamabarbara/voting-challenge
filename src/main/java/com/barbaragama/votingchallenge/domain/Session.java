package com.barbaragama.votingchallenge.domain;

import com.barbaragama.votingchallenge.enums.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
@Table(name = "voting_session")
@Schema(description = "Voting session entity")
public class Session {
    @Id
    @GeneratedValue
    @Schema(description = "Session ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "agenda_id")
    @Schema(description = "Agenda associated with the session")
    private Agenda agenda;

    @CreationTimestamp
    @Schema(description = "Session creation time", example = "2023-10-01T12:00:00")
    private LocalDateTime startTime;

    @Schema(description = "Session end time", example = "2023-10-01T12:30:00")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Session status", example = "OPEN")
    private SessionStatus sessionStatus;

    public SessionStatus getSessionStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (endTime.isBefore(now)) {
            return SessionStatus.CLOSED;
        }
        return sessionStatus;
    }

    public boolean isOpen() {
        return getSessionStatus().equals(SessionStatus.OPEN);
    }

    @Schema(description = "Session duration in minutes", example = "30")
    private long durationMinutes;
}
