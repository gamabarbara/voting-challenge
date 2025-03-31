package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.dto.request.SessionRequestDTO;
import com.barbaragama.votingchallenge.dto.response.SessionResponseDTO;
import com.barbaragama.votingchallenge.dto.response.SessionResultResponseDTO;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voting")
@Tag(name = "Voting Session", description = "Endpoints for managing voting sessions")
public class VotingSessionController {

    private final SessionService sessionService;

    @GetMapping
    @Operation(
            summary = "Get all voting sessions",
            description = "Retrieve a list of all voting sessions",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all voting sessions")
    }
    )
    public ResponseEntity<List<SessionResponseDTO>> getAll() {
        return ResponseEntity.ok(sessionService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get voting session by ID",
            description = "Retrieve a voting session by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved voting session"),
                    @ApiResponse(responseCode = "404", description = "Voting session not found")
            }
    )
    public ResponseEntity<SessionResponseDTO> getById(@Parameter(description = "Id session") @PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.getById(id));
    }

    @PostMapping("/sessions")
    @Operation(
            summary = "Open a new voting session",
            description = "Create a new voting session for an agenda",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully opened a new voting session"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "404", description = "Agenda not found")
            }
    )
    public ResponseEntity<SessionResponseDTO> openVotingSession(@Parameter(description = "Session data") @Valid @RequestBody SessionRequestDTO sessionRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.openVotingSession(sessionRequestDTO));
    }

    @GetMapping("/result/{sessionId}")
    @Operation(
            summary = "Get voting result",
            description = "Retrieve the voting result for a specific session",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved voting result"),
                    @ApiResponse(responseCode = "400", description = "Invalid session ID"),
                    @ApiResponse(responseCode = "404", description = "Voting session not found")
            }
    )
    public ResponseEntity<SessionResultResponseDTO> getVotingResult(@Parameter(description = "Session ID") @PathVariable String sessionId) {
        try {
            return ResponseEntity.ok(sessionService.getVotingResult(UUID.fromString(sessionId)));
        } catch (IllegalArgumentException e) {
            throw new AppException("Invalid UUID format", HttpStatus.BAD_REQUEST);
        }
    }
}