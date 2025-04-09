package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.dto.request.AssociateRequestDTO;
import com.barbaragama.votingchallenge.enums.VoteAbility;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.facade.CpfValidationFacade;
import com.barbaragama.votingchallenge.repositories.AssociateRepository;
import com.barbaragama.votingchallenge.repositories.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AssociateServiceTest {

    @Mock
    private AssociateRepository associateRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private CpfValidationFacade cpfValidationFacade;

    @InjectMocks
    private AssociateService associateService;

    private String name;
    private String cpf;
    private UUID sessionId;
    private AssociateRequestDTO associateRequestDTO;



    @BeforeEach
    void setUp() {
        name = "Bárbara Gama";
        cpf = "836.088.390-46";
        sessionId = UUID.randomUUID();
        associateRequestDTO = new AssociateRequestDTO();
        associateRequestDTO.setName(name);
        associateRequestDTO.setCpf(cpf);

    }

    @Nested
    @DisplayName("getAllAssociates")
    class getAllAssociates {

        @Test
        @DisplayName("Should return a list of all associates")
        void getAllAssociatesSuccess(){
            when(associateRepository.findAll()).thenReturn(List.of(new Associate(name, cpf)));
            List<Associate> result = associateService.getAllAssociates();
            assertNotNull(result);
            verify(associateRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return an empty list when no associates are found")
        void getAllAssociatesEmpty(){
            when(associateRepository.findAll()).thenReturn(List.of());
            List<Associate> result = associateService.getAllAssociates();
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(associateRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getAssociate")
    class getAssociate {

        @Test
        @DisplayName("Should return an associate when found by CPF")
        void getAssociateByCpfSuccess() {
            Associate associate = new Associate(name, cpf);
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.of(associate));
            Associate result = associateService.getAssociate(associate.getCpf());
            assertNotNull(result);
            assertEquals(associate.getCpf(), result.getCpf());
            assertEquals(associate.getName(), result.getName());
            verify(associateRepository, times(1)).findByCpf(associate.getCpf());
        }

        @Test
        @DisplayName("Should throw an exception when associate is not found by CPF")
        void getAssociateByCpfNotFound() {
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.empty());
            assertThrows(AppException.class, () -> associateService.getAssociate(cpf));
            verify(associateRepository, times(1)).findByCpf(cpf);
        }
    }

    @Nested
    @DisplayName("createAssociate")
    class createAssociate {

        @Test
        @DisplayName("Should create an associate successfully")
        void createAssociateSuccess() {
            Associate associate = new Associate(name, cpf);
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.empty());
            when(associateRepository.save(associate)).thenReturn(associate);

            Associate result = associateService.createAssociate(associateRequestDTO);

            assertNotNull(result);
            assertEquals(associate.getCpf(), result.getCpf());
            assertEquals(associate.getName(), result.getName());
            verify(associateRepository, times(1)).findByCpf(cpf);
        }

        @Test
        @DisplayName("Should throw an exception when associate already exists")
        void createAssociateAlreadyExists() {
            Associate associate = new Associate(name, cpf);
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.of(associate));
            assertThrows(AppException.class, () -> associateService.createAssociate(associateRequestDTO));
            verify(associateRepository, times(1)).findByCpf(cpf);
        }
    }

    @Nested
    @DisplayName("validateAndGetAssociate")
    class validateAndGetAssociate {

        @Test
        @DisplayName("Should return a new associate when CPF exists")
        void shouldReturnExistingAssociateWhenCpfExists() {
            Associate associate = new Associate(name, cpf);
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.of(associate));
            when(voteRepository.existsByAssociateIdAndSessionId(associate.getId(), sessionId)).thenReturn(false);
            when(cpfValidationFacade.validateCpfForVoting(cpf)).thenReturn(VoteAbility.ABLE_TO_VOTE);

            Associate result = associateService.validateAndGetAssociate(name, cpf, sessionId);

            assertNotNull(result);
            assertEquals(associate.getId(), result.getId());
            assertEquals(associate.getCpf(), result.getCpf());
            assertEquals(associate.getName(), result.getName());
            verify(associateRepository, never()).save(any(Associate.class));
        }

        @Test
        @DisplayName("Should create an associate when CPF does not exist")
        void shouldCreateAssociateWhenCpfDoesNotExist() {
           Associate associate = new Associate(UUID.randomUUID(), name, cpf);
           when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.empty());
           when(cpfValidationFacade.validateCpfForVoting(cpf)).thenReturn(VoteAbility.ABLE_TO_VOTE);
           when(associateRepository.save(any(Associate.class))).thenReturn(associate);

           Associate result = associateService.validateAndGetAssociate(name, cpf, sessionId);

           assertNotNull(result);
           assertEquals(associate.getId(), result.getId());
           assertEquals(associate.getCpf(), result.getCpf());
           assertEquals(associate.getName(), result.getName());
           verify(associateRepository, times(1)).save(any(Associate.class));
        }

        @Test
        @DisplayName("Should throw an exception when associate has already voted")
        void shouldThrowExceptionWhenAssociateAlreadyVoted() {
            Associate associate = new Associate(name, cpf);
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.of(associate));
            when(voteRepository.existsByAssociateIdAndSessionId(associate.getId(), sessionId)).thenReturn(true);

            AppException exception = assertThrows(AppException.class, () -> associateService.validateAndGetAssociate(name, cpf, sessionId));

            assertEquals("Associate has already voted in this session", exception.getMessage());
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
            verify(cpfValidationFacade, never()).validateCpfForVoting(cpf);
            verify(associateRepository, never()).save(any(Associate.class));
        }

        @Test
        @DisplayName("Should throw an exception when user is not able to vote")
        void shouldThrowExceptionWhenUserIsNoAbleToVote() {
            Associate associate = new Associate(name, cpf);
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.of(associate));
            when(cpfValidationFacade.validateCpfForVoting(cpf)).thenReturn(VoteAbility.UNABLE_TO_VOTE);

            AppException exception = assertThrows(AppException.class, () -> associateService.validateAndGetAssociate(associate.getName(), cpf, sessionId));

            assertEquals("Associate is not able to vote", exception.getMessage());
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
            verify(associateRepository, never()).save(any(Associate.class));
        }

        @Test
        @DisplayName("Should validate existing associate even when name is different")
        void shouldValidateExistingAssociateEvenWhenNameIsDifferent() {
            Associate associate = new Associate(name, cpf);
            when(associateRepository.findByCpf(cpf)).thenReturn(java.util.Optional.of(associate));
            when(voteRepository.existsByAssociateIdAndSessionId(associate.getId(), sessionId)).thenReturn(false);
            when(cpfValidationFacade.validateCpfForVoting(cpf)).thenReturn(VoteAbility.ABLE_TO_VOTE);

            Associate result = associateService.validateAndGetAssociate("Different name", cpf, sessionId);

            assertNotNull(result);
            assertEquals(associate.getId(), result.getId());
            assertEquals(associate.getCpf(), result.getCpf());
            assertEquals(associate.getName(), result.getName());
            verify(associateRepository, never()).save(any(Associate.class));
        }
    }
}