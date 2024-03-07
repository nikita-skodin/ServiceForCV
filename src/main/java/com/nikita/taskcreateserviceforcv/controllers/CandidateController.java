package com.nikita.taskcreateserviceforcv.controllers;

import com.nikita.taskcreateserviceforcv.DTOs.CandidateDTO;
import com.nikita.taskcreateserviceforcv.DTOs.ExceptionDTO;
import com.nikita.taskcreateserviceforcv.entities.Candidate;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.services.CandidateService;
import com.nikita.taskcreateserviceforcv.util.mappers.CandidateMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Candidate", description = "Candidate operations")
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController extends MainController {

    CandidateService candidateService;
    CandidateMapper candidateMapper;

    @Operation(
            summary = "Get candidates",
            description = "Get set of CandidateDTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully"),
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
            }
    )
    @GetMapping
    public ResponseEntity<Page<CandidateDTO>>
    getAll(@Parameter(description = "Offset for pagination", example = "0")
           @RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) Integer offset,

           @Parameter(description = "Number of items per page", example = "20")
           @RequestParam(value = "limit", required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit,

           @Parameter(description = "Keyword for finding items", example = "candidate")
           @RequestParam(name = "keyword", required = false) String keyword,

           @Parameter(description = "Boolean flag, do it need to sort by lastname", example = "true")
           @RequestParam(name = "sorted", required = false, defaultValue = "false") String sorted) {

        Page<Candidate> candidatePage = candidateService
                .findAll(offset, limit, keyword, sorted.equals("true"));

        return ResponseEntity.ok(candidatePage.map(candidateMapper::getDTO));
    }

    @Operation(
            summary = "Create new candidate",
            description = "Create new candidate",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON candidateDTO without id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CandidateDTO.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDTO.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CandidateDTO> create(@Valid @RequestBody CandidateDTO candidateDTO) {

        if (candidateDTO.getId() != null) {
            throw new BadRequestException("A new Candidate cannot has an id");
        }

        Candidate candidate = candidateMapper.getEntity(candidateDTO);

        boolean isExists = candidateService.exists(Example.of(candidate));

        if (isExists) {
            throw new BadRequestException("The same Candidate is already exists");
        }

        Candidate savedCandidate = candidateService.save(candidate);

        return ResponseEntity.ok(candidateMapper.getDTO(savedCandidate));
    }

    @Operation(
            summary = "Update a candidate",
            description = "Update existing or create new candidate",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON CandidateDTO with id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CandidateDTO.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionDTO.class)
                            )
                    )
            }
    )
    @PutMapping
    public ResponseEntity<CandidateDTO> update(@Valid @RequestBody CandidateDTO candidateDTO) {

        if (candidateDTO.getId() == null) {
            throw new BadRequestException("Updatable Candidate must has an id");
        }

        Candidate candidate = candidateMapper.getEntity(candidateDTO);

        boolean isExists = candidateService.exists(Example.of(candidate));

        if (isExists) {
            throw new BadRequestException("The same Candidate is already exists");
        }

        Candidate updatedCandidate = candidateService.update(candidate);

        return ResponseEntity.ok(candidateMapper.getDTO(updatedCandidate));
    }
}
