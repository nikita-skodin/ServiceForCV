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

@Schema(description = "DTO for representing an candidate")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateDTO {

    @Schema(name = "id", example = "1", title = "candidate`s id")
    Long id;

    @Schema(name = "name", example = "Nikita", title = "candidate`s name")
    @NotBlank(message = "name must not be empty")
    @Size(min = 1, max = 20,
            message = "name`s length must be between 1 and 20 chars")
    String name;

    @Schema(name = "lastname", example = "Skodin", title = "candidate`s lastname")
    @NotBlank(message = "lastname must not be empty")
    @Size(min = 1, max = 20,
            message = "lastname`s length must be between 1 and 20 chars")
    String lastname;

    @Schema(name = "patronymic", example = "Dmitrievich", title = "candidate`s patronymic")
    @NotBlank(message = "patronymic must not be empty")
    @Size(min = 1, max = 20,
            message = "patronymic`s length must be between 1 and 20 chars")
    String patronymic;

    // TODO
    @Schema(name = "photo", example = "SGVsbG8sIFdvcmxkIQ==", title = "candidate`s photo")
    byte[] photo;

    @Schema(name = "description", example = "Java/Spring developer, 3 years of experience", title = "candidate`s description")
    @Size(max = 1000,
            message = "description`s length must be less than 1000 chars")
    String description;

    // TODO
    @Schema(name = "cvFile", example = "SGVsbG8sIFdvcmxkIQ==", title = "candidate`s cvFile")
    byte[] cvFile;

    @Schema(type = "set", implementation = AreaDTO.class, name = "possible_areas", title = "set of possible areas")
    @JsonProperty("possible_areas")
    Set<AreaDTO> possibleAreas = new HashSet<>();
}
