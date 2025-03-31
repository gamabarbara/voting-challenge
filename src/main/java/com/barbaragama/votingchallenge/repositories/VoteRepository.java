package com.barbaragama.votingchallenge.repositories;

import com.barbaragama.votingchallenge.domain.Vote;
import com.barbaragama.votingchallenge.enums.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
    boolean existsByAssociateIdAndSessionId(UUID associateId, UUID sessionId);
    long countBySessionId(UUID sessionId);
    long countBySessionIdAndOptionEquals(UUID sessionId, VoteOption option);
}
