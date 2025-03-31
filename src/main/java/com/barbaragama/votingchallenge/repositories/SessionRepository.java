package com.barbaragama.votingchallenge.repositories;

import com.barbaragama.votingchallenge.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
}
