package com.nikita.taskcreateserviceforcv.controllers;

import com.nikita.taskcreateserviceforcv.DTOs.AreaDTO;
import com.nikita.taskcreateserviceforcv.DTOs.ExceptionDTO;
import com.nikita.taskcreateserviceforcv.entities.Area;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.services.AreaService;
import com.nikita.taskcreateserviceforcv.util.mappers.AreaMapper;
import com.nikita.taskcreateserviceforcv.util.validators.AreaValidator;
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

@Tag(name = "Area", description = "Area operations")
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
public class AreaController extends MainController {

    AreaValidator areaValidator;
    AreaService areaService;
    AreaMapper areaMapper;

    @Operation(
            summary = "Get areas",
            description = "Get set of AreaDTO",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully"),
                    @ApiResponse(responseCode = "200", description = "Successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
            }
    )
    @GetMapping
    public ResponseEntity<Page<AreaDTO>>
    getAll(@Parameter(description = "Offset for pagination", example = "0")
           @RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) Integer offset,

           @Parameter(description = "Number of items per page", example = "20")
           @RequestParam(value = "limit", required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit,

           @Parameter(description = "Keyword for finding items", example = "20")
           @RequestParam(name = "keyword", required = false) String keyword) {

        Page<Area> areaPage = areaService.findAll(offset, limit, keyword);
        return ResponseEntity.ok(areaPage.map(areaMapper::getDTO));
    }

    @Operation(
            summary = "Create new area",
            description = "Create new area",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON AreaDTO without id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AreaDTO.class))),
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
    public ResponseEntity<AreaDTO> create(@Valid @RequestBody AreaDTO areaDTO, BindingResult bindingResult) {

        if (areaDTO.getId() != null) {
            throw new BadRequestException("A new area cannot has an id");
        }

        Area area = areaMapper.getEntity(areaDTO);

        areaValidator.validate(area, bindingResult);
        checkBindingResult(bindingResult);

        Area savedArea = areaService.save(area);
        return ResponseEntity.ok(areaMapper.getDTO(savedArea));
    }

    @Operation(
            summary = "Update an area",
            description = "Update existing or create new area",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "empty JSON AreaDTO with id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AreaDTO.class))),
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
    public ResponseEntity<AreaDTO> update(@Valid @RequestBody AreaDTO areaDTO, BindingResult bindingResult) {
        if (areaDTO.getId() == null) {
            throw new BadRequestException("Updatable area must has an id");
        }

        Area area = areaMapper.getEntity(areaDTO);

        areaValidator.validate(area, bindingResult);
        checkBindingResult(bindingResult);

        Area updatedArea = areaService.update(area);

        return ResponseEntity.ok(areaMapper.getDTO(updatedArea));
    }

}