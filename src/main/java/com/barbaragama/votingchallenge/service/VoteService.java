package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.domain.Vote;
import com.barbaragama.votingchallenge.domain.Session;
import com.barbaragama.votingchallenge.dto.request.VoteRequestDTO;
import com.barbaragama.votingchallenge.dto.response.VoteResponseDTO;
import com.barbaragama.votingchallenge.enums.VoteOption;
import com.barbaragama.votingchallenge.repositories.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final SessionValidationService sessionValidationService;
    private final AssociateService associateService;
    private final VoteValidationService voteValidationService;
    private final VoteRepository voteRepository;

    @Transactional
    public VoteResponseDTO castVote(VoteRequestDTO voteRequestDTO) {
        Session session = sessionValidationService.validateAndGetSession(voteRequestDTO.getSessionId());
        Associate associate = associateService.validateAndGetAssociate(voteRequestDTO.getName(), voteRequestDTO.getCpf(), session.getId());
        VoteOption voteOption = voteValidationService.validateVoteOption(voteRequestDTO.getOption());

        Vote vote = Vote.builder()
                .associate(associate)
                .session(session)
                .option(voteOption)
                .build();
        voteRepository.save(vote);

        return VoteResponseDTO.builder()
                .associateId(associate.getId())
                .sessionId(voteRequestDTO.getSessionId())
                .option(voteRequestDTO.getOption())
                .message("Vote successfully cast")
                .build();
    }
}