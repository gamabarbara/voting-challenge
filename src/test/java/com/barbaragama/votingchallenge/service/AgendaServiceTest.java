package com.barbaragama.votingchallenge.service;

import com.barbaragama.votingchallenge.domain.Agenda;
import com.barbaragama.votingchallenge.dto.request.AgendaRequestDTO;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.repositories.AgendaRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaService agendaService;

    private AgendaRequestDTO agendaRequestDTO;
    private Agenda agenda;

    @BeforeEach
    void setUp() {

        agendaRequestDTO = new AgendaRequestDTO();
        agendaRequestDTO.setTitle("Test Agenda");
        agendaRequestDTO.setDescription("Test Description");
    }

    @Nested
    @DisplayName("createAgenda")
    class CreateAgendaTests {

        @Test
        @DisplayName("Should create an agenda successfully when valid data is provided")
        void createAgendaSuccess() {
            Agenda agenda = Agenda.builder()
                    .title(agendaRequestDTO.getTitle())
                    .description(agendaRequestDTO.getDescription())
                    .build();

            when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);
            Agenda result = agendaService.createAgenda(agendaRequestDTO);

            assertNotNull(result);
            assertEquals(agendaRequestDTO.getTitle(), result.getTitle());
            assertEquals(agendaRequestDTO.getDescription(), result.getDescription());
            verify(agendaRepository, times(1)).save(any(Agenda.class));
        }

        @Test
        @DisplayName("Should throw an exception when repository fails to save")
        void shouldThrowExceptionWhenRepositoryFailsToSave() {
            when(agendaRepository.save(any(Agenda.class))).thenThrow(new DataIntegrityViolationException("Database error"));

            assertThrows(DataIntegrityViolationException.class, () -> agendaService.createAgenda(agendaRequestDTO));
            verify(agendaRepository, times(1)).save(any(Agenda.class));
        }

        @Test
        @DisplayName("Should throw an exception when a constraint violation occurs")
        void shouldThrowExceptionWhenConstraintViolationOccurs() {
            when(agendaRepository.save(any(Agenda.class))).thenThrow(new ConstraintViolationException("Constraint violation", null));
            assertThrows(ConstraintViolationException.class, () -> agendaService.createAgenda(agendaRequestDTO));
            verify(agendaRepository, times(1)).save(any(Agenda.class));
        }
    }

    @Nested
    @DisplayName("getAllAgendas")
    class GetAllAgendasTests {

        @Test
        @DisplayName("Should return a list of agendas")
        void getAllAgendasSuccess() {
            Agenda agenda = new Agenda();
            agenda.setTitle("Test Agenda");
            agenda.setDescription("Test Description");

            Agenda agenda2 = new Agenda();
            agenda2.setTitle("Test Agenda 2");
            agenda2.setDescription("Test Description 2");

            when(agendaRepository.findAll()).thenReturn(List.of(agenda, agenda2));
            List<Agenda> result = agendaService.getAllAgendas();
            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Should return an empty list when no agendas are found")
        void getAllAgendasEmpty() {
            when(agendaRepository.findAll()).thenReturn(List.of());
            List<Agenda> result = agendaService.getAllAgendas();
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(agendaRepository, times(1)).findAll();
        }

    }

    @Nested
    @DisplayName("getAgendaById")
    class GetAgendaByIdTests {

        @Test
        @DisplayName("Should return an agenda when found by ID")
        void getAgendaByIdSuccess() {
            UUID id = UUID.randomUUID();
            Agenda agenda = new Agenda();
            agenda.setId(id);
            agenda.setTitle("title");
            agenda.setDescription("description");
            when(agendaRepository.findById(agenda.getId())).thenReturn(java.util.Optional.of(agenda));
            Agenda result = agendaService.getAgendaById(id);
            assertNotNull(result);
            assertEquals(agenda.getId(), result.getId());
            assertEquals(agenda.getTitle(), result.getTitle());
            assertEquals(agenda.getDescription(), result.getDescription());
            verify(agendaRepository, times(1)).findById(agenda.getId());
        }

        @Test
        @DisplayName("Should throw an exception when agenda is not found by ID")
        void getAgendaByIdNotFound() {
            UUID id = UUID.randomUUID();
            when(agendaRepository.findById(id)).thenReturn(java.util.Optional.empty());
            assertThrows(AppException.class, () -> agendaService.getAgendaById(id));
            verify(agendaRepository, times(1)).findById(id);
        }
    }
}