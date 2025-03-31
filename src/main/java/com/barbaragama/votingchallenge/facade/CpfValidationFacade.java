package com.barbaragama.votingchallenge.facade;

import com.barbaragama.votingchallenge.client.CpfValidationClient;
import com.barbaragama.votingchallenge.enums.VoteAbility;
import com.barbaragama.votingchallenge.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CpfValidationFacade {

    private final CpfValidationClient validationClient;

    public VoteAbility validateCpfForVoting(String cpf) {
        if (!validationClient.isCpfValid(cpf)) {
            throw new AppException("Invalid CPF", HttpStatus.NOT_FOUND);
        }
        return validationClient.checkVoteAbility(cpf);
    }
}