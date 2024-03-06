package com.nikita.taskcreateserviceforcv.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateTestDTO {

    Long id;

    CandidateDTO candidate;

    TestDTO test;

    @NotNull
    @JsonProperty("date_of_passing")
    LocalDate dateOfPassing;

    @NotNull
    Integer score;

}
