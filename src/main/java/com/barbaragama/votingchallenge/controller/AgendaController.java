package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.domain.Agenda;
import com.barbaragama.votingchallenge.dto.request.AgendaRequestDTO;
import com.barbaragama.votingchallenge.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/agenda")
@Tag(name = "Agenda", description = "Agenda management")
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping
    @Operation(
            summary = "Get all agendas",
            description = "Retrieve a list of all agendas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agendas retrieved successfully"),
            }
    )
    public ResponseEntity<List<Agenda>> getAllAgendas() {
        return ResponseEntity.ok(agendaService.getAllAgendas());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get agenda by ID",
            description = "Retrieve an agenda by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agenda retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Agenda not found")
            }
    )
    public ResponseEntity<Agenda> getAgendaById(@Parameter(description = "agenda ID") @PathVariable UUID id) {
        return ResponseEntity.ok(agendaService.getAgendaById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new agenda",
            description = "Create a new agenda with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Agenda created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<Agenda> createAgenda(@Parameter(description = "Agenda data") @Valid @RequestBody AgendaRequestDTO agendaRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.createAgenda(agendaRequestDTO));
    }
}
