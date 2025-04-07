package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.dto.request.SessionRequestDTO;
import com.barbaragama.votingchallenge.dto.response.SessionResponseDTO;
import com.barbaragama.votingchallenge.dto.response.SessionResultResponseDTO;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.service.SessionService;
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

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotingSessionController.class)
@Import(VotingSessionControllerTest.SessionServiceTestConfig.class)
class VotingSessionControllerTest {

    @TestConfiguration
    public static class SessionServiceTestConfig {
        @Bean
        @Primary
        public SessionService sessionService() {
            return Mockito.mock(SessionService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionService sessionService;

    private SessionRequestDTO sessionRequestDTO;
    private SessionResponseDTO sessionResponseDTO;
    private UUID sessionId;

    @BeforeEach
    void setUp() {
        Mockito.reset(sessionService);
        UUID agendaId = UUID.randomUUID();
        sessionId = UUID.randomUUID();

        sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setAgendaId(agendaId);
        sessionRequestDTO.setDurationMinutes(5);

        sessionResponseDTO = new SessionResponseDTO();
        sessionResponseDTO.setId(sessionId);
        sessionResponseDTO.setAgendaId(agendaId);
        sessionResponseDTO.setDurationMinutes(5);
    }

    @Test
    @DisplayName("Should get all sessions successfully")
    void getAllSuccess() throws Exception {
        when(sessionService.getAll()).thenReturn(Collections.singletonList(sessionResponseDTO));

        mockMvc.perform(get("/api/voting")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(sessionId.toString()));
    }

    @Test
    @DisplayName("Should get session by id successfully")
    void getByIdSuccess() throws Exception {
        when(sessionService.getById(sessionId)).thenReturn(sessionResponseDTO);

        mockMvc.perform(get("/api/voting/" + sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionId.toString()));
    }

    @Test
    @DisplayName("Should return 404 when session not found by id")
    void getByIdNotFound() throws Exception {
        when(sessionService.getById(any(UUID.class)))
                .thenThrow(new AppException("Session not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/voting/" + sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Session not found")));
    }

    @Test
    @DisplayName("Should open voting session successfully")
    void openVotingSessionSuccess() throws Exception {
        when(sessionService.openVotingSession(any(SessionRequestDTO.class))).thenReturn(sessionResponseDTO);

        mockMvc.perform(post("/api/voting/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(sessionId.toString()));
    }

    @Test
    @DisplayName("Should return 400 when session request is invalid")
    void openVotingSessionInvalidRequest() throws Exception {
        sessionRequestDTO.setAgendaId(null);

        mockMvc.perform(post("/api/voting/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get voting result successfully")
    void getVotingResultSuccess() throws Exception {
        SessionResultResponseDTO resultDTO = new SessionResultResponseDTO();
        resultDTO.setSessionId(sessionId);
        resultDTO.setTotalVotes(10);
        resultDTO.setYesVotes(7);
        resultDTO.setNoVotes(3);

        when(sessionService.getVotingResult(sessionId)).thenReturn(resultDTO);

        mockMvc.perform(get("/api/voting/result/" + sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId.toString()))
                .andExpect(jsonPath("$.totalVotes").value(10));
    }

    @Test
    @DisplayName("Should return 400 when session id is invalid")
    void getVotingResultInvalidId() throws Exception {
        mockMvc.perform(get("/api/voting/result/invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid UUID format")));
    }
}