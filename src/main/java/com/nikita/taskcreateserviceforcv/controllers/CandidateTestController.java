package com.nikita.taskcreateserviceforcv.controllers;

import com.nikita.taskcreateserviceforcv.DTOs.AreaDTO;
import com.nikita.taskcreateserviceforcv.DTOs.CandidateTestDTO;
import com.nikita.taskcreateserviceforcv.DTOs.ExceptionDTO;
import com.nikita.taskcreateserviceforcv.entities.CandidateTest;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.services.CandidateTestService;
import com.nikita.taskcreateserviceforcv.util.mappers.CandidateTestMapper;
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

@Tag(name = "CandidateTest", description = "CandidateTest operations")
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/candidates_tests")
@RequiredArgsConstructor
public class CandidateTestController extends MainController {

    CandidateTestService candidateTestService;
    CandidateTestMapper candidateTestMapper;

    @Operation(
            summary = "Get candidateTest",
            description = "Get set of CandidateTestDTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully"),
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
            }
    )
    @GetMapping
    public ResponseEntity<Page<CandidateTestDTO>>
    getAll(@Parameter(description = "Offset for pagination", example = "0")
           @RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) Integer offset,

           @Parameter(description = "Number of items per page", example = "20")
           @RequestParam(value = "limit", required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit,

           @Parameter(description = "Test id for filtering", example = "20")
           @RequestParam(name = "test_id", required = false) String testId,

           @Parameter(description = "Candidate id for filtering", example = "20")
           @RequestParam(name = "candidate_id", required = false) String candidateId,

           @Parameter(description = "Boolean flag, do it need to sort by data", example = "true")
           @RequestParam(name = "sorted_by_data", required = false, defaultValue = "false") String isSortedByData,

           @Parameter(description = "Boolean flag, do it need to sort by score", example = "true")
           @RequestParam(name = "sorted_by_score", required = false, defaultValue = "false") String isSortedByScore) {

        Long testIdLong = null;
        Long candidateIdLong = null;

        try {
            testIdLong = testId == null ? null : Long.parseLong(testId);
            candidateIdLong = candidateId == null ? null : Long.parseLong(candidateId);
        } catch (NumberFormatException e) {
            //  ignore
        }

        Page<CandidateTest> candidateTestPage = candidateTestService
                .findAll(offset, limit, testIdLong, candidateIdLong,
                        isSortedByData.equals("true"), isSortedByScore.equals("true"));

        return ResponseEntity.ok(candidateTestPage.map(candidateTestMapper::getDTO));
    }

    @Operation(
            summary = "Create new candidateTest",
            description = "Create new candidateTest",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON CandidateTestDTO without id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CandidateTestDTO.class))),
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
    public ResponseEntity<CandidateTestDTO> create(@Valid @RequestBody CandidateTestDTO candidateTestDTO) {

        if (candidateTestDTO.getId() != null) {
            throw new BadRequestException("A new CandidateTest cannot has an id");
        }

        CandidateTest candidateTest = candidateTestMapper.getEntity(candidateTestDTO);

        boolean isExists = candidateTestService.exists(Example.of(candidateTest));

        if (isExists) {
            throw new BadRequestException("CandidateTest is already exists");
        }

        CandidateTest savedCandidateTest = candidateTestService.save(candidateTest);

        return ResponseEntity.ok(candidateTestMapper.getDTO(savedCandidateTest));
    }

    @Operation(
            summary = "Update an candidateTest",
            description = "Update existing or create new candidateTest",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON CandidateTestDTO with id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CandidateTestDTO.class))),
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
    public ResponseEntity<CandidateTestDTO> update(@Valid @RequestBody CandidateTestDTO candidateTestDTO) {

        if (candidateTestDTO.getId() == null) {
            throw new BadRequestException("Updatable CandidateTest must has an id");
        }

        CandidateTest candidateTest = candidateTestMapper.getEntity(candidateTestDTO);

        CandidateTest updatedCandidateTest = candidateTestService.update(candidateTest);

        return ResponseEntity.ok(candidateTestMapper.getDTO(updatedCandidateTest));
    }

}
