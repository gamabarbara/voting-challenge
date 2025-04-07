package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.dto.request.VoteRequestDTO;
import com.barbaragama.votingchallenge.dto.response.VoteResponseDTO;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
@Import(VoteControllerTest.VoteServiceTestConfig.class)
class VoteControllerTest {

    @TestConfiguration
    public static class VoteServiceTestConfig {
        @Bean
        @Primary
        public VoteService voteService() {
            return Mockito.mock(VoteService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VoteService voteService;

    private VoteRequestDTO voteRequestDTO;
    private VoteResponseDTO voteResponseDTO;

    @BeforeEach
    void setUp() {
        Mockito.reset(voteService);
        UUID sessionId = UUID.randomUUID();

        voteRequestDTO = new VoteRequestDTO();
        voteRequestDTO.setSessionId(sessionId);
        voteRequestDTO.setName("João Silva");
        voteRequestDTO.setCpf("486.681.640-66");
        voteRequestDTO.setOption("YES");

        voteResponseDTO = new VoteResponseDTO();
        voteResponseDTO.setMessage("Vote registered successfully");
    }
    @Test
    @DisplayName("Should cast vote successfully")
    void castVoteSuccess() throws Exception {
        when(voteService.castVote(any(VoteRequestDTO.class))).thenReturn(voteResponseDTO);

        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Vote registered successfully")));
    }

    @Test
    @DisplayName("Should return 400 when vote request is invalid")
    void castVoteInvalidRequest() throws Exception {
        voteRequestDTO.setCpf(null);

        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 403 when associate already voted")
    void castVoteAssociateAlreadyVoted() throws Exception {
        when(voteService.castVote(any(VoteRequestDTO.class)))
                .thenThrow(new AppException("Associate has already voted in this session", HttpStatus.FORBIDDEN));

        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequestDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", is("Associate has already voted in this session")));
    }

    @Test
    @DisplayName("Should return 404 when session not found")
    void castVoteSessionNotFound() throws Exception {
        when(voteService.castVote(any(VoteRequestDTO.class)))
                .thenThrow(new AppException("Session not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Session not found")));
    }

    @Test
    @DisplayName("Should return 403 when associate is unable to vote")
    void castVoteAssociateUnableToVote() throws Exception {
        when(voteService.castVote(any(VoteRequestDTO.class)))
                .thenThrow(new AppException("Associate is not able to vote", HttpStatus.FORBIDDEN));

        mockMvc.perform(post("/api/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequestDTO)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", is("Associate is not able to vote")));
    }
}