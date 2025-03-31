package com.barbaragama.votingchallenge.client;

import com.barbaragama.votingchallenge.enums.VoteAbility;
import org.springframework.stereotype.Component;

@Component
public class CpfValidationClientFake implements CpfValidationClient {

    @Override
    public boolean isCpfValid(String cpf) {
        return Math.random() > 0.3;
    }

    @Override
    public VoteAbility checkVoteAbility(String cpf) {
        return Math.random() > 0.5 ? VoteAbility.ABLE_TO_VOTE : VoteAbility.UNABLE_TO_VOTE;
    }
}