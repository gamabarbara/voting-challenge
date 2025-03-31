package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.enums.VoteAbility;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.facade.CpfValidationFacade;
import com.barbaragama.votingchallenge.repositories.AssociateRepository;
import com.barbaragama.votingchallenge.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssociateService {

    private final AssociateRepository associateRepository;
    private final VoteRepository voteRepository;
    private final CpfValidationFacade cpfValidationFacade;

    public Associate validateAndGetAssociate(String cpf, UUID sessionId) {
        Associate associate = associateRepository.findByCpf(cpf)
                .orElse(null);

        if (!Objects.isNull(associate)) {
            boolean hasVoted = voteRepository.existsByAssociateIdAndSessionId(associate.getId(), sessionId);
            if (hasVoted) {
                throw new AppException("Associate has already voted in this session", HttpStatus.CONFLICT);
            }
        }

        VoteAbility ability = cpfValidationFacade.validateCpfForVoting(cpf);
        if (ability == VoteAbility.UNABLE_TO_VOTE) {
            throw new AppException("User is not able to vote", HttpStatus.FORBIDDEN);
        }

        if (Objects.isNull(associate)) {
            associate = new Associate();
            associate.setCpf(cpf);
            associate = associateRepository.save(associate);
        }

        return associate;
    }
}