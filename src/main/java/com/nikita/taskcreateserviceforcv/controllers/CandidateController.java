package com.nikita.taskcreateserviceforcv.controllers;

import com.nikita.taskcreateserviceforcv.DTOs.CandidateDTO;
import com.nikita.taskcreateserviceforcv.entities.Candidate;
import com.nikita.taskcreateserviceforcv.exceptions.BadRequestException;
import com.nikita.taskcreateserviceforcv.services.CandidateService;
import com.nikita.taskcreateserviceforcv.util.mappers.CandidateMapper;
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
import org.springframework.web.multipart.MultipartFile;

@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController extends MainController {

    CandidateService candidateService;
    CandidateMapper candidateMapper;

    @GetMapping
    public ResponseEntity<Page<CandidateDTO>>
    getAll(@RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) Integer offset,
           @RequestParam(value = "limit", required = false, defaultValue = "20") @Min(1) @Max(100) Integer limit,
           @RequestParam(name = "keyword", required = false) String keyword,
           @RequestParam(name = "sorted", required = false, defaultValue = "false") String sorted) {

        Page<Candidate> candidatePage = candidateService
                .findAll(offset, limit, keyword, sorted.equals("true"));

        return ResponseEntity.ok(candidatePage.map(candidateMapper::getDTO));
    }

    @PostMapping
    public ResponseEntity<CandidateDTO> create(@RequestPart("photo") MultipartFile photo,
                                               @RequestPart("cv_file") MultipartFile cvFile,
                                               @Valid @RequestPart("candidateDTO") CandidateDTO candidateDTO) {

        if (photo.isEmpty()) {
            throw new BadRequestException("photo cannot be empty");
        }

        if (cvFile.isEmpty()) {
            throw new BadRequestException("cv_file cannot be empty");
        }

        if (candidateDTO.getId() != null) {
            throw new BadRequestException("A new Candidate cannot has an id");
        }

        Candidate candidate = candidateMapper.getEntity(candidateDTO);

        boolean isExists = candidateService.exists(Example.of(candidate));

        if (isExists) {
            throw new BadRequestException("The same Candidate is already exists");
        }

        Candidate savedCandidate = candidateService.save(candidate, photo, cvFile);

        return ResponseEntity.ok(candidateMapper.getDTO(savedCandidate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> update(@PathVariable Long id,
                                               @RequestPart("photo") MultipartFile photo,
                                               @RequestPart("cv_file") MultipartFile cvFile,
                                               @Valid @RequestPart("candidateDTO") CandidateDTO candidateDTO) {

        candidateDTO.setId(id);

        Candidate candidate = candidateMapper.getEntity(candidateDTO);

        Candidate updatedCandidate = candidateService.update(candidate, photo, cvFile);

        return ResponseEntity.ok(candidateMapper.getDTO(updatedCandidate));
    }
}
