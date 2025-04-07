package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.dto.request.AssociateRequestDTO;
import com.barbaragama.votingchallenge.exception.AppException;
import com.barbaragama.votingchallenge.service.AssociateService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssociateController.class)
@Import(AssociateControllerTest.AssociateServiceTestConfig.class)
class AssociateControllerTest {

    @TestConfiguration
    public static class AssociateServiceTestConfig {
        @Bean
        @Primary
        public AssociateService associateService() {
            return Mockito.mock(AssociateService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AssociateService associateService;

    @Autowired
    private ObjectMapper objectMapper;

    private Associate associate1;
    private Associate associate2;
    private AssociateRequestDTO associateRequestDTO;

    @BeforeEach
    void setUp() {
        Mockito.reset(associateService);

        associate1 = new Associate();
        associate1.setCpf("486.681.640-66");
        associate1.setName("João Silva");

        associate2 = new Associate();
        associate2.setCpf("620.009.280-05");
        associate2.setName("Maria Santos");

        associateRequestDTO = new AssociateRequestDTO();
        associateRequestDTO.setCpf("830.703.480-92");
        associateRequestDTO.setName("Novo Associado");
    }

    @Test
    @DisplayName("Should return all associates")
    void getAllAssociatesSuccess() throws Exception {
        List<Associate> associates = Arrays.asList(associate1, associate2);
        when(associateService.getAllAssociates()).thenReturn(associates);

        mockMvc.perform(get("/api/associate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("João Silva")))
                .andExpect(jsonPath("$[1].name", is("Maria Santos")));
    }

    @Test
    @DisplayName("Should return an associate by CPF")
    void getAssociateByCpfSuccess() throws Exception {
        when(associateService.getAssociate(associate1.getCpf())).thenReturn(associate1);

        mockMvc.perform(get("/api/associate/" + associate1.getCpf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("João Silva")))
                .andExpect(jsonPath("$.cpf", is("486.681.640-66")));
    }

    @Test
    @DisplayName("Should create a new associate successfully")
    void createAssociateSuccess() throws Exception {
        when(associateService.createAssociate(any(AssociateRequestDTO.class))).thenReturn(associate1);

        mockMvc.perform(post("/api/associate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associateRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("João Silva")))
                .andExpect(jsonPath("$.cpf", is("486.681.640-66")));
    }

    @Test
    @DisplayName("Should return 400 when associate creation fails")
    void createAssociateBadRequest() throws Exception {
        associateRequestDTO.setCpf(null);

        mockMvc.perform(post("/api/associate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associateRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when associate not found")
    void getAssociateByIdNotFound() throws Exception {
        when(associateService.getAssociate("99999999999")).thenThrow(new AppException("Associate not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/associate/99999999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Associate not found")));
    }
}