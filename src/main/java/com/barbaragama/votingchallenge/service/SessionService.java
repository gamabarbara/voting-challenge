package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Agenda;
import com.barbaragama.votingchallenge.domain.Session;
import com.barbaragama.votingchallenge.dto.request.SessionRequestDTO;
import com.barbaragama.votingchallenge.dto.response.SessionResponseDTO;
import com.barbaragama.votingchallenge.dto.response.SessionResultResponseDTO;
import com.barbaragama.votingchallenge.enums.SessionStatus;
import com.barbaragama.votingchallenge.enums.VoteOption;
import com.barbaragama.votingchallenge.enums.VotingResult;
import com.barbaragama.votingchallenge.repositories.AgendaRepository;
import com.barbaragama.votingchallenge.repositories.VoteRepository;
import com.barbaragama.votingchallenge.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.barbaragama.votingchallenge.exception.AppException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {


    private final VoteRepository voteRepository;
    private final SessionRepository sessionRepository;
    private final AgendaRepository agendaRepository;

    public List<SessionResponseDTO> getAll() {
        return sessionRepository.findAll().stream()
                .map(this::convertToResponseToDTO)
                .collect(Collectors.toList());
    }

    public SessionResponseDTO getById(UUID id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new AppException("Voting session not found", HttpStatus.NOT_FOUND));
        return this.convertToResponseToDTO(session);
    }

    public SessionResponseDTO openVotingSession(SessionRequestDTO sessionRequestDTO) {
        Agenda agenda = agendaRepository.findById(sessionRequestDTO.getAgendaId())
                .orElseThrow(() -> new AppException("Agenda not found", HttpStatus.NOT_FOUND));

        Session session = new Session();
        session.setAgenda(agenda);
        session.setStartTime(LocalDateTime.now());
        session.setDurationMinutes(sessionRequestDTO.getDurationMinutes() != 0 ? sessionRequestDTO.getDurationMinutes() : 1);
        session.setEndTime(session.getStartTime().plusMinutes(session.getDurationMinutes() != 0 ? session.getDurationMinutes() : 1));
        session.setSessionStatus(SessionStatus.OPEN);

        Session createdSession = sessionRepository.save(session);

        return convertToResponseToDTO(createdSession);
    }

    public SessionResultResponseDTO getVotingResult(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException("Voting session not found", HttpStatus.NOT_FOUND));

        long totalVotes = voteRepository.countBySessionId(sessionId);
        long yesVotes = voteRepository.countBySessionIdAndOptionEquals(sessionId, VoteOption.YES);
        long noVotes = voteRepository.countBySessionIdAndOptionEquals(sessionId, VoteOption.NO);

        VotingResult result = determineVotingResult(session, yesVotes, noVotes);

        return SessionResultResponseDTO.builder()
                .sessionId(sessionId)
                .agendaId(session.getAgenda().getId())
                .agendaTitle(session.getAgenda().getTitle())
                .yesVotes(yesVotes)
                .noVotes(noVotes)
                .totalVotes(totalVotes)
                .result(result)
                .build();
    }


    private VotingResult determineVotingResult(Session session, long yesVotes, long noVotes) {
        if (session.isOpen()) {
            return VotingResult.SESSION_IN_PROGRESS;
        }

        if (yesVotes > noVotes) {
            return VotingResult.APPROVED;
        } else if (noVotes > yesVotes) {
            return VotingResult.REJECTED;
        } else {
            return VotingResult.TIE;
        }
    }

    private SessionResponseDTO convertToResponseToDTO(Session session) {
        SessionResponseDTO responseDTO = new SessionResponseDTO();
        responseDTO.setId(session.getId());
        responseDTO.setAgendaId(session.getAgenda().getId());
        responseDTO.setStartTime(session.getStartTime());
        responseDTO.setEndTime(session.getEndTime());
        responseDTO.setSessionStatus(session.getSessionStatus());
        responseDTO.setDurationMinutes(session.getDurationMinutes());
        return responseDTO;
    }
}
