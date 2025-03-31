package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.enums.VoteOption;
import com.barbaragama.votingchallenge.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteValidationService {

    public VoteOption validateVoteOption(String optionText) {
        try {
            return VoteOption.valueOf(optionText.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException("Invalid vote option. Use 'YES' or 'NO'", HttpStatus.BAD_REQUEST);
        }
    }
}