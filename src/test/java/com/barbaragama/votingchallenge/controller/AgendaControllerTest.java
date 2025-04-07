package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.domain.Agenda;
import com.barbaragama.votingchallenge.dto.request.AgendaRequestDTO;
import com.barbaragama.votingchallenge.service.AgendaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendaController.class)
@Import(AgendaControllerTest.AgendaServiceTestConfig.class)
class AgendaControllerTest {

    @TestConfiguration
    public static class AgendaServiceTestConfig {

        @Bean
        @Primary
        public AgendaService agendaService() {
            return Mockito.mock(AgendaService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Agenda agenda;
    private Agenda agenda2;
    private AgendaRequestDTO agendaRequestDTO;
    private UUID id;

    @BeforeEach
    void setUp() {
        Mockito.reset(agendaService);
        id = UUID.randomUUID();

        agenda = new Agenda();
        agenda.setId(id);
        agenda.setTitle("Agenda");
        agenda.setDescription("Description");

        agenda2 = new Agenda();
        agenda2.setId(UUID.randomUUID());
        agenda2.setTitle("Agenda 2");
        agenda2.setDescription("Description 2");

        agendaRequestDTO = new AgendaRequestDTO();
        agendaRequestDTO.setTitle("New Agenda");
        agendaRequestDTO.setDescription("New Description");
    }

    @Test
    @DisplayName("Should return all agendas")
    void getAllAgendasSuccess() throws Exception {
        List<Agenda> agendas = Arrays.asList(agenda, agenda2);
        when(agendaService.getAllAgendas()).thenReturn(agendas);

        mockMvc.perform(get("/api/agenda").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Agenda")))
                .andExpect(jsonPath("$[1].title", is("Agenda 2")));
    }

    @Test
    @DisplayName("Should return agenda by ID")
    void getAgendaByIdSuccess() throws Exception {
        when(agendaService.getAgendaById(agenda.getId())).thenReturn(agenda);

        mockMvc.perform(get("/api/agenda/" + agenda.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Agenda")))
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.id", is(agenda.getId().toString())));
    }

    @Test
    @DisplayName("Should create a new agenda")
    void creatAgendaSuccess() throws Exception {
        when(agendaService.createAgenda(any(AgendaRequestDTO.class))).thenReturn(agenda);

        mockMvc.perform(post("/api/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Agenda")))
                .andExpect(jsonPath("$.description", is("Description")));
    }

    @Test
    @DisplayName("Should return 404 when agenda not found")
    void getAgendaByIdNotFound() throws  Exception {
        when(agendaService.getAgendaById(any(UUID.class))).thenReturn(null);
        mockMvc.perform(get("/api/agenda" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendaRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 400 when creating agenda with invalid data")
    void createAgendaBadRequest() throws Exception {
        agendaRequestDTO.setTitle(null);

        mockMvc.perform(post("/api/agenda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequestDTO)))
                .andExpect(status().isBadRequest());
    }

}