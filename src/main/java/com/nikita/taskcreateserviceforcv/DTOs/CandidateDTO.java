package com.nikita.taskcreateserviceforcv.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateDTO {

    Long id;

    @NotBlank(message = "name must not be empty")
    @Size(min = 1, max = 20,
            message = "name`s length must be between 1 and 20 chars")
    String name;

    @NotBlank(message = "lastname must not be empty")
    @Size(min = 1, max = 20,
            message = "lastname`s length must be between 1 and 20 chars")
    String lastname;

    @NotBlank(message = "patronymic must not be empty")
    @Size(min = 1, max = 20,
            message = "patronymic`s length must be between 1 and 20 chars")
    String patronymic;

    // TODO
    byte[] photo;

    @Size(max = 1000,
            message = "description`s length must be less than 1000 chars")
    String description;

    // TODO
    byte[] cvFile;

    @JsonProperty("possible_areas")
    Set<AreaDTO> possibleAreas = new HashSet<>();
}
