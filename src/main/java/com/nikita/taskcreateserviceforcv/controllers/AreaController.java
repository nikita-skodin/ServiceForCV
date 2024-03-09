package com.nikita.taskcreateserviceforcv.controllers;

import com.nikita.taskcreateserviceforcv.DTOs.AreaDTO;
import com.nikita.taskcreateserviceforcv.entities.Area;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.services.AreaService;
import com.nikita.taskcreateserviceforcv.util.mappers.AreaMapper;
import com.nikita.taskcreateserviceforcv.util.validators.AreaValidator;
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

@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
public class AreaController extends MainController {

    AreaValidator areaValidator;
    AreaService areaService;
    AreaMapper areaMapper;

    @GetMapping
    public ResponseEntity<Page<AreaDTO>>
    getAll(@RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) Integer offset,
           @RequestParam(value = "limit", required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit,
           @RequestParam(name = "keyword", required = false) String keyword) {

        Page<Area> areaPage = areaService.findAll(offset, limit, keyword);
        return ResponseEntity.ok(areaPage.map(areaMapper::getDTO));
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<AreaDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AreaDTO areaDTO, BindingResult bindingResult) {

        areaDTO.setId(id);

        Area area = areaMapper.getEntity(areaDTO);

        areaValidator.validate(area, bindingResult);
        checkBindingResult(bindingResult);

        Area updatedArea = areaService.update(area);

        return ResponseEntity.ok(areaMapper.getDTO(updatedArea));
    }

}