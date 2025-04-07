package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.dto.request.AssociateRequestDTO;
import com.barbaragama.votingchallenge.enums.VoteAbility;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.facade.CpfValidationFacade;
import com.barbaragama.votingchallenge.repositories.AssociateRepository;
import com.barbaragama.votingchallenge.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssociateService {

    private final AssociateRepository associateRepository;
    private final VoteRepository voteRepository;
    private final CpfValidationFacade cpfValidationFacade;

    public List<Associate> getAllAssociates() {
        return associateRepository.findAll();
    }

    public Associate getAssociate(String cpf) {
        return associateRepository.findByCpf(cpf)
                .orElseThrow(() -> new AppException("Associate not found", HttpStatus.NOT_FOUND));
    }

    public Associate createAssociate(AssociateRequestDTO associateRequestDTO) {
        if (associateRepository.findByCpf(associateRequestDTO.getCpf()).isPresent()) {
            throw new AppException("Associate already exists", HttpStatus.CONFLICT);
        }
        Associate associate = Associate.builder()
                .name(associateRequestDTO.getName())
                .cpf(associateRequestDTO.getCpf())
                .build();
        return associateRepository.save(associate);
    }

    public Associate validateAndGetAssociate(String name, String cpf, UUID sessionId) {
        Associate associate = associateRepository.findByCpf(cpf)
                .orElse(null);

        if (!Objects.isNull(associate)) {
            boolean hasVoted = voteRepository.existsByAssociateIdAndSessionId(associate.getId(), sessionId);
            if (hasVoted) {
                throw new AppException("Associate has already voted in this session", HttpStatus.FORBIDDEN);
            }
        }

        VoteAbility ability = cpfValidationFacade.validateCpfForVoting(cpf);
        if (ability == VoteAbility.UNABLE_TO_VOTE) {
            throw new AppException("Associate is not able to vote", HttpStatus.FORBIDDEN);
        }

        if (Objects.isNull(associate)) {
            associate = new Associate();
            associate.setName(name);
            associate.setCpf(cpf);
            associate = associateRepository.save(associate);
        }

        return associate;
    }
}