package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Agenda;
import com.barbaragama.votingchallenge.domain.Session;
import com.barbaragama.votingchallenge.dto.request.SessionRequestDTO;
import com.barbaragama.votingchallenge.dto.response.SessionResponseDTO;
import com.barbaragama.votingchallenge.enums.SessionStatus;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.repositories.AgendaRepository;
import com.barbaragama.votingchallenge.repositories.SessionRepository;

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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private SessionService sessionService;

    private Agenda agenda;
    private SessionRequestDTO sessionRequestDTO;
    private UUID sessionId;

    @BeforeEach
    void setUp() {
        agenda = new Agenda();
        agenda.setId(UUID.randomUUID());

        sessionRequestDTO = new SessionRequestDTO();
        sessionRequestDTO.setAgendaId(agenda.getId());
        sessionRequestDTO.setDurationMinutes(30);

        sessionId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("getAllSessions")
    class GetAllSessions {

        @Test
        @DisplayName("Should return list of all sessions")
        void getAllSessions() {
            when(sessionRepository.findAll()).thenReturn(List.of(new Session(agenda, 30)));

            List<SessionResponseDTO> sessions = sessionService.getAll();

            assertNotNull(sessions);
            assertFalse(sessions.isEmpty());
            assertEquals(1, sessions.size());
            verify(sessionRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no sessions exist")
        void getAllSessionsEmpty() {
            when(sessionRepository.findAll()).thenReturn(List.of());

            List<SessionResponseDTO> result = sessionService.getAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(sessionRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getSessionById")
    class GetSessionById {

        @Test
        @DisplayName("Should return session by ID")
        void getSessionById() {
            Session session = new Session(agenda, 30);

            when(sessionRepository.findById(session.getId())).thenReturn(java.util.Optional.of(session));

            SessionResponseDTO result = sessionService.getById(session.getId());

            assertNotNull(result);
            assertEquals(session.getId(), result.getId());
            assertEquals(session.getAgenda().getId(), result.getAgendaId());
            assertEquals(session.getStartTime(), result.getStartTime());
            assertEquals(session.getEndTime(), result.getEndTime());
            assertEquals(session.getSessionStatus(), result.getSessionStatus());
            verify(sessionRepository, times(1)).findById(session.getId());
        }

        @Test
        @DisplayName("Should throw exception when session not found by ID")
        void getSessionBayIdNotFound() {
            when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.empty());

            AppException exception = assertThrows(AppException.class, () -> sessionService.getById(sessionId));

            assertEquals("Voting session not found", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }

    @Nested
    @DisplayName("openVotingSession")
    class OpenVotingSession {

        @Test
        @DisplayName("Should open a new voting session successfully")
        void openVotingSession() {
            when(agendaRepository.findById(sessionRequestDTO.getAgendaId())).thenReturn(Optional.of(agenda));
            when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
                Session session = invocation.getArgument(0);
                session.setId(UUID.randomUUID());
                return session;
            });

            SessionResponseDTO result = sessionService.openVotingSession(sessionRequestDTO);

            assertNotNull(result);
            assertEquals(agenda.getId(), result.getAgendaId());
            assertEquals(sessionRequestDTO.getDurationMinutes(), result.getDurationMinutes());
            assertEquals(SessionStatus.OPEN, result.getSessionStatus());
            assertNotNull(result.getStartTime());
            assertNotNull(result.getEndTime());

            ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
            verify(sessionRepository).save(sessionCaptor.capture());

            Session capturedSession = sessionCaptor.getValue();
            assertEquals(agenda, capturedSession.getAgenda());
            assertEquals(sessionRequestDTO.getDurationMinutes(), capturedSession.getDurationMinutes());
            assertEquals(SessionStatus.OPEN, capturedSession.getSessionStatus());
            assertTrue(capturedSession.getEndTime().isAfter(capturedSession.getStartTime()));
        }

        @Test
        @DisplayName("Should open voting session with default duration")
        void openVotingSessionWithDefaultDuration() {
            sessionRequestDTO.setDurationMinutes(0);
            when(agendaRepository.findById(sessionRequestDTO.getAgendaId())).thenReturn(Optional.of(agenda));
            when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
                Session session = invocation.getArgument(0);
                session.setId(UUID.randomUUID());
                return session;
            });

            SessionResponseDTO result = sessionService.openVotingSession(sessionRequestDTO);

            assertNotNull(result);
            assertEquals(1, result.getDurationMinutes());

            ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
            verify(sessionRepository).save(sessionCaptor.capture());

            Session capturedSession = sessionCaptor.getValue();
            assertEquals(1, capturedSession.getDurationMinutes());
            assertEquals(capturedSession.getStartTime().plusMinutes(1), capturedSession.getEndTime() );
        }

        @Test
        @DisplayName("Should throw exception when agenda not found")
        void openVotingSessionAgendaNotFound() {
            when(agendaRepository.findById(sessionRequestDTO.getAgendaId())).thenReturn(Optional.empty());

            AppException exception = assertThrows(AppException.class, () -> sessionService.openVotingSession(sessionRequestDTO));

            assertEquals("Agenda not found", exception.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        }
    }
}