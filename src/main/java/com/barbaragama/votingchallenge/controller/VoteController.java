package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.dto.request.VoteRequestDTO;
import com.barbaragama.votingchallenge.dto.response.VoteResponseDTO;
import com.barbaragama.votingchallenge.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/vote")
@Tag(name = "Vote", description = "Endpoints for casting votes")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @Operation(
            summary = "Cast a vote",
            description = "Endpoint to cast a vote for a specific session and associate,",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Vote successfully cast"),
                    @ApiResponse(responseCode = "400", description = "Invalid data provided"),
                    @ApiResponse(responseCode = "403", description = "User is not able to vote"),
                    @ApiResponse(responseCode = "404", description = "Session not found"),
                    @ApiResponse(responseCode = "409", description = "Associate has already voted in this session")
            }
    )
    public ResponseEntity<VoteResponseDTO> castVote(@Parameter(description = "Vote data") @Valid @RequestBody VoteRequestDTO voteRequestDTO) {
        VoteResponseDTO responseDTO = voteService.castVote(voteRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
