package com.barbaragama.votingchallenge.client;

import com.barbaragama.votingchallenge.enums.VoteAbility;

public interface CpfValidationClient {
    boolean isCpfValid(String cpf);
    VoteAbility checkVoteAbility(String cpf);
}
