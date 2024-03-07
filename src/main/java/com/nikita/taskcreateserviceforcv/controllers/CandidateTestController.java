package com.nikita.taskcreateserviceforcv.controllers;

import com.nikita.taskcreateserviceforcv.DTOs.CandidateTestDTO;
import com.nikita.taskcreateserviceforcv.entities.CandidateTest;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.services.CandidateTestService;
import com.nikita.taskcreateserviceforcv.util.mappers.CandidateTestMapper;
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

@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/candidates_tests")
@RequiredArgsConstructor
public class CandidateTestController extends MainController {

    CandidateTestService candidateTestService;
    CandidateTestMapper candidateTestMapper;

    @GetMapping
    public ResponseEntity<Page<CandidateTestDTO>>
    getAll(@RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) Integer offset,
           @RequestParam(value = "limit", required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit,
           @RequestParam(name = "test_id", required = false) String testId,
           @RequestParam(name = "candidate_id", required = false) String candidateId,
           @RequestParam(name = "sorted_by_data", required = false, defaultValue = "false") String isSortedByData,
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
