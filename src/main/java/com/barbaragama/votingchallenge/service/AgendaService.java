package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.dto.request.AgendaRequestDTO;
import com.barbaragama.votingchallenge.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.barbaragama.votingchallenge.domain.Agenda;
import com.barbaragama.votingchallenge.repositories.AgendaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    @Transactional
    public Agenda createAgenda(AgendaRequestDTO agendaRequestDTO) {
        Agenda agenda = Agenda.builder()
                .title(agendaRequestDTO.getTitle())
                .description(agendaRequestDTO.getDescription())
                .build();

        return agendaRepository.save(agenda);
    }

    public List<Agenda> getAllAgendas() {
        return agendaRepository.findAll();
    }

    public Agenda getAgendaById(UUID id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new AppException("Agenda not found.", HttpStatus.NOT_FOUND));
    }
}
