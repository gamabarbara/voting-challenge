package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.domain.Session;
import com.barbaragama.votingchallenge.domain.Vote;
import com.barbaragama.votingchallenge.dto.request.VoteRequestDTO;
import com.barbaragama.votingchallenge.dto.response.VoteResponseDTO;
import com.barbaragama.votingchallenge.enums.VoteOption;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.repositories.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private SessionValidationService sessionValidationService;
    @Mock
    private AssociateService associateService;
    @Mock
    private VoteValidationService voteValidationService;

    @InjectMocks
    private VoteService voteService;

    private VoteRequestDTO voteRequestDTO;
    private Session session;
    private Associate associate;

    @BeforeEach
    void setUp() {
        session = Session.builder()
                .id(UUID.randomUUID())
                .build();

        associate = Associate.builder()
                .id(UUID.randomUUID())
                .name("Maria Silva")
                .cpf("376.368.240-60")
                .build();

        voteRequestDTO = new VoteRequestDTO();
        voteRequestDTO.setSessionId(UUID.randomUUID());
        voteRequestDTO.setOption("YES");
        voteRequestDTO.setCpf("376.368.240-60");
        voteRequestDTO.setName("Maria Silva");
    }

    @Nested
    @DisplayName("Success scenarios")
    class SuccessScenarios {
        @Test
        @DisplayName("Should cast a vote successfully")
        void castVoteSuccess() {
            when(sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId())).thenReturn(session);
            when(associateService.validateAndGetAssociate(voteRequestDTO.getName(), voteRequestDTO.getCpf(), session.getId())).thenReturn(associate);
            when(voteValidationService.validateVoteOption(voteRequestDTO.getOption())).thenReturn(VoteOption.YES);

            ArgumentCaptor<Vote> voteCaptor = ArgumentCaptor.forClass(Vote.class);

            VoteResponseDTO response = voteService.castVote(voteRequestDTO);

            assertNotNull(response);
            assertEquals(associate.getId(), response.getAssociateId());
            assertEquals(voteRequestDTO.getSessionId(), response.getSessionId());
            assertEquals(voteRequestDTO.getOption(), response.getOption());
            assertEquals("Vote successfully cast", response.getMessage());

            verify(voteRepository).save(voteCaptor.capture());
            Vote savedVote = voteCaptor.getValue();
            assertEquals(associate, savedVote.getAssociate());
            assertEquals(session, savedVote.getSession());
            assertEquals(VoteOption.YES, savedVote.getOption());
        }

        @Test
        @DisplayName("Should handle repository save failure")
        void handleSaveFailure() {
            when(sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId())).thenReturn(session);
            when(associateService.validateAndGetAssociate(voteRequestDTO.getName(), voteRequestDTO.getCpf(), session.getId())).thenReturn(associate);
            when(voteValidationService.validateVoteOption(voteRequestDTO.getOption())).thenReturn(VoteOption.YES);
            when(voteRepository.save(any())).thenThrow(new RuntimeException("Database error"));

            assertThrows(RuntimeException.class, () -> voteService.castVote(voteRequestDTO));
        }
    }

    @Nested
    @DisplayName("Error scenarios")
    class ErrorScenarios {
        @Test
        @DisplayName("Should throw exception when session not found")
        void sessionNotFound() {
            when(sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId()))
                    .thenThrow(new AppException("Voting session not found", HttpStatus.NOT_FOUND));

            AppException exception = assertThrows(AppException.class,
                    () -> voteService.castVote(voteRequestDTO));

            assertEquals("Voting session not found", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            verify(associateService, never()).validateAndGetAssociate(anyString(), anyString(), any());
        }

        @Test
        @DisplayName("Should throw exception when session is closed")
        void sessionClosed() {
            when(sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId()))
                    .thenThrow(new AppException("Voting session is closed", HttpStatus.BAD_REQUEST));

            AppException exception = assertThrows(AppException.class,
                    () -> voteService.castVote(voteRequestDTO));

            assertEquals("Voting session is closed", exception.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            verify(associateService, never()).validateAndGetAssociate(anyString(), anyString(), any());
        }

        @Test
        @DisplayName("Should throw exception when associate already voted")
        void associateAlreadyVoted() {
            when(sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId())).thenReturn(session);
            when(associateService.validateAndGetAssociate(voteRequestDTO.getName(), voteRequestDTO.getCpf(), session.getId()))
                    .thenThrow(new AppException("Associate has already voted in this session", HttpStatus.FORBIDDEN));

            AppException exception = assertThrows(AppException.class,
                    () -> voteService.castVote(voteRequestDTO));

            assertEquals("Associate has already voted in this session", exception.getMessage());
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
            verify(voteValidationService, never()).validateVoteOption(anyString());
        }

        @Test
        @DisplayName("Should throw exception when associate is not able to vote")
        void associateNotAbleToVote() {
            when(sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId())).thenReturn(session);
            when(associateService.validateAndGetAssociate(voteRequestDTO.getName(), voteRequestDTO.getCpf(), session.getId()))
                    .thenThrow(new AppException("Associate is not able to vote", HttpStatus.FORBIDDEN));

            AppException exception = assertThrows(AppException.class,
                    () -> voteService.castVote(voteRequestDTO));

            assertEquals("Associate is not able to vote", exception.getMessage());
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
            verify(voteValidationService, never()).validateVoteOption(anyString());
        }

        @Test
        @DisplayName("Should throw exception when vote option is invalid")
        void invalidVoteOption() {
            when(sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId())).thenReturn(session);
            when(associateService.validateAndGetAssociate(voteRequestDTO.getName(), voteRequestDTO.getCpf(), session.getId())).thenReturn(associate);
            when(voteValidationService.validateVoteOption(voteRequestDTO.getOption()))
                    .thenThrow(new AppException("Invalid vote option. Use 'YES' or 'NO'", HttpStatus.BAD_REQUEST));

            AppException exception = assertThrows(AppException.class,
                    () -> voteService.castVote(voteRequestDTO));

            assertEquals("Invalid vote option. Use 'YES' or 'NO'", exception.getMessage());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            verify(voteRepository, never()).save(any());
        }
    }
}