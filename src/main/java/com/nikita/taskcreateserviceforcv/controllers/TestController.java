package com.nikita.taskcreateserviceforcv.controllers;

import com.nikita.taskcreateserviceforcv.DTOs.AreaDTO;
import com.nikita.taskcreateserviceforcv.DTOs.ExceptionDTO;
import com.nikita.taskcreateserviceforcv.DTOs.TestDTO;
import com.nikita.taskcreateserviceforcv.entities.Test;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.services.TestService;
import com.nikita.taskcreateserviceforcv.util.mappers.TestMapper;
import com.nikita.taskcreateserviceforcv.util.validators.TestValidator;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Test", description = "Test operations")
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController extends MainController {

    TestValidator testValidator;
    TestService testService;
    TestMapper testMapper;

    @Operation(
            summary = "Get tests",
            description = "Get set of TestDTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully"),
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
            }
    )
    @GetMapping
    public ResponseEntity<Page<TestDTO>>
    getAll(@Parameter(description = "Offset for pagination", example = "0")
           @RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) Integer offset,

           @Parameter(description = "Number of items per page", example = "20")
           @RequestParam(value = "limit", required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit,

           @Parameter(description = "Keyword for finding items", example = "20")
           @RequestParam(name = "keyword", required = false) String keyword) {

        Page<Test> areaPage = testService.findAll(offset, limit, keyword);
        return ResponseEntity.ok(areaPage.map(testMapper::getDTO));
    }

    @Operation(
            summary = "Create new test",
            description = "Create new test",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON TestDTO without id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TestDTO.class))),
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
    public ResponseEntity<TestDTO> create(@Valid @RequestBody TestDTO testDTO, BindingResult bindingResult) {

        if (testDTO.getId() != null) {
            throw new BadRequestException("A new test cannot has an id");
        }

        Test test = testMapper.getEntity(testDTO);

        testValidator.validate(test, bindingResult);
        checkBindingResult(bindingResult);

        Test savedTest = testService.save(test);

        return ResponseEntity.ok(testMapper.getDTO(savedTest));
    }

    @Operation(
            summary = "Update an test",
            description = "Update existing or create new test",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON TestDTO with id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TestDTO.class))),
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
    public ResponseEntity<TestDTO> update(@Valid @RequestBody TestDTO testDTO, BindingResult bindingResult) {

        if (testDTO.getId() == null) {
            throw new BadRequestException("Updatable test must has an id");
        }

        Test test = testMapper.getEntity(testDTO);

        testValidator.validate(test, bindingResult);
        checkBindingResult(bindingResult);

        Test updatedTest = testService.update(test);

        return ResponseEntity.ok(testMapper.getDTO(updatedTest));
    }


}
