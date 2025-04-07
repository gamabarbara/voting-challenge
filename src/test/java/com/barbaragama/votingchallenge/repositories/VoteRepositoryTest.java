package com.barbaragama.votingchallenge.repositories;

import com.barbaragama.votingchallenge.domain.Agenda;
import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.domain.Session;
import com.barbaragama.votingchallenge.domain.Vote;
import com.barbaragama.votingchallenge.enums.VoteOption;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class VoteRepositoryTest {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Should check if a vote exists by associate ID and session ID")
    void existsByAssociateIdAndSessionId() {
        Associate associate = new Associate("Bárbara Gama", "123.456.789-09");
        this.entityManager.persist(associate);

        Agenda agenda = new Agenda("Vote on new project", "Vote on the new project proposal for 2025");
        this.entityManager.persist(agenda);

        Session session = new Session(agenda, 60);
        this.entityManager.persist(session);

        Vote vote = new Vote(associate.getId(), session.getId(), VoteOption.YES);
        this.entityManager.persist(vote);

        boolean exists = this.voteRepository.existsByAssociateIdAndSessionId(associate.getId(), session.getId());
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should count votes by session ID")
    void countBySessionId() {
        Associate associate1 = new Associate("Bárbara Gama", "557.073.220-94");
        this.entityManager.persist(associate1);

        Associate associate2 = new Associate("Anderson Gama", "501.348.290-93");
        this.entityManager.persist(associate2);

        Associate associate3 = new Associate("Lucas Gama", "236.869.860-43");
        this.entityManager.persist(associate3);

        Agenda agenda = new Agenda("Vote on another new project", "Vote on another new project proposal for 2025");
        this.entityManager.persist(agenda);

        Session session = new Session(agenda, 60);
        this.entityManager.persist(session);

        Vote vote = new Vote(associate1.getId(), session.getId(), VoteOption.YES);
        this.entityManager.persist(vote);

        Vote vote2 = new Vote(associate2.getId(), session.getId(), VoteOption.YES);
        this.entityManager.persist(vote2);

        Vote vote3 = new Vote(associate3.getId(), session.getId(), VoteOption.NO);
        this.entityManager.persist(vote3);

        long count = this.voteRepository.countBySessionId(session.getId());
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Should count votes by session ID and option")
    void countBySessionIdAndOptionEquals() {
        Associate associate1 = new Associate("Bárbara Gama", "557.073.220-94");
        this.entityManager.persist(associate1);

        Associate associate2 = new Associate("Anderson Gama", "501.348.290-93");
        this.entityManager.persist(associate2);

        Associate associate3 = new Associate("Lucas Gama", "236.869.860-43");
        this.entityManager.persist(associate3);

        Agenda agenda = new Agenda("Vote on new project", "Vote on the new project proposal for 2025");
        this.entityManager.persist(agenda);

        Session session = new Session(agenda, 60);
        this.entityManager.persist(session);

        Vote vote = new Vote(associate1.getId(), session.getId(), VoteOption.YES);
        this.entityManager.persist(vote);

        Vote vote2 = new Vote(associate2.getId(), session.getId(), VoteOption.YES);
        this.entityManager.persist(vote2);

        Vote vote3 = new Vote(associate3.getId(), session.getId(), VoteOption.NO);
        this.entityManager.persist(vote3);

        long countYes = this.voteRepository.countBySessionIdAndOptionEquals(session.getId(), VoteOption.YES);
        long countNo = this.voteRepository.countBySessionIdAndOptionEquals(session.getId(), VoteOption.NO);

        assertThat(countYes).isEqualTo(2);
        assertThat(countNo).isEqualTo(1);
    }
}