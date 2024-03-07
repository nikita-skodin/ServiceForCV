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

@Schema(description = "DTO for representing an test")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestDTO {

    @Schema(name = "id", example = "1", title = "test`s id")
    Long id;

    @Schema(name = "title", example = "Java core", title = "test`s title")
    @NotBlank(message = "title must not be blank")
    @Size(min = 3, max = 50,
            message = "title`s length must be between 3 and 50 chars")
    String title;

    @Schema(name = "description", example = "Basic knowledge in Java core", title = "test`s description")
    @Size(max = 1000,
            message = "description`s length must be less than 1000 chars")
    String description;

    @Schema(type = "set", implementation = AreaDTO.class, name = "applicable_areas", title = "set of applicable areas")
    @JsonProperty("applicable_areas")
    Set<AreaDTO> applicableAreas = new HashSet<>();

}
