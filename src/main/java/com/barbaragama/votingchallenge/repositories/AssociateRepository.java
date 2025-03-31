package com.barbaragama.votingchallenge.repositories;

import com.barbaragama.votingchallenge.domain.Associate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssociateRepository extends JpaRepository<Associate, UUID> {
    Optional<Associate> findByCpf(String cpf);
}
