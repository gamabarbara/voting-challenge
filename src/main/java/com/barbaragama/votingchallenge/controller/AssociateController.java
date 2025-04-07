package com.barbaragama.votingchallenge.controller;

import com.barbaragama.votingchallenge.domain.Associate;
import com.barbaragama.votingchallenge.dto.request.AssociateRequestDTO;
import com.barbaragama.votingchallenge.service.AssociateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/associate")
@Tag(name = "Associate", description = "Associate management")
public class AssociateController {
    private final AssociateService associateService;

    @GetMapping
    @Operation(
            summary = "Get all associates",
            description = "Retrieve a list of all associates",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Associates retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Associates not found")
            }
    )
    public ResponseEntity<List<Associate>> getAllAssociates() {
        return ResponseEntity.ok(associateService.getAllAssociates());
    }

    @GetMapping("/{cpf}")
    @Operation(
            summary = "Get associate by ID",
            description = "Retrieve an associate by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Associate retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Associate not found")
            }
    )
    public ResponseEntity<Associate> getAssociateById(@PathVariable("cpf") String cpf) {
        Associate associate = associateService.getAssociate(cpf);
        return ResponseEntity.ok(associate);
    }

    @PostMapping
    @Operation(
            summary = "Create a new associate",
            description = "Create a new associate with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Associate created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<Associate> createAssociate(@Valid @RequestBody AssociateRequestDTO associateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(associateService.createAssociate(associateRequestDTO));
    }

}
