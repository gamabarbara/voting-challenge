package com.barbaragama.votingchallenge.repositories;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.dto.request.AssociateRequestDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AssociateRepositoryTest {

    @Autowired
    AssociateRepository associateRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get an associate by CPF")
    void findByCpfSuccess() {
        String cpf = "123.456.789-09";
        AssociateRequestDTO data = new AssociateRequestDTO("Bárbara", cpf);
        this.createAssociate(data);
        Optional<Associate> result =  this.associateRepository.findByCpf(cpf);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get an associate by CPF")
    void findByCpfError() {
        String cpf = "123.456.789-09";
        Optional<Associate> result =  this.associateRepository.findByCpf(cpf);
        assertThat(result.isEmpty()).isTrue();
    }

    private void createAssociate(AssociateRequestDTO associateRequestDTO) {
        Associate newAssociate = new Associate(associateRequestDTO);
        this.entityManager.persist(newAssociate);
    }
}