package com.barbaragama.votingchallenge.repositories;

import com.barbaragama.votingchallenge.domain.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgendaRepository extends JpaRepository<Agenda, UUID> {
}
