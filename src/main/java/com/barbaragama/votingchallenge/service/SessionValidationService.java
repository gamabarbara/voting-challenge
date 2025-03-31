package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Session;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionValidationService {

    private final SessionRepository sessionRepository;

    public Session validateAndGetSession(UUID sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException("Voting session not found", HttpStatus.NOT_FOUND));

        if (!session.isOpen()) {
            throw new AppException("The voting session is closed", HttpStatus.BAD_REQUEST);
        }

        return session;
    }
}