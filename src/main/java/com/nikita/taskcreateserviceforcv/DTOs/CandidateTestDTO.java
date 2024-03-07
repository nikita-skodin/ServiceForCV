package com.nikita.taskcreateserviceforcv.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Schema(description = "DTO for representing an candidateTest")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateTestDTO {

    @Schema(name = "id", example = "1", title = "candidateTest`s id")
    Long id;

    @Schema(implementation = CandidateDTO.class, name = "candidate", title = "candidate")
    @NotNull
    CandidateDTO candidate;

    @Schema(implementation = TestDTO.class, name = "test", title = "test")
    @NotNull
    TestDTO test;

    @Schema(name = "date_of_passing", example = "2024-02-06", title = "date of test passing")
    @NotNull
    @JsonProperty("date_of_passing")
    LocalDate dateOfPassing;

    @Schema(name = "score", example = "80", title = "candidates score")
    @NotNull
    Integer score;
}
