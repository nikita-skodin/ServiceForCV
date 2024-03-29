package com.nikita.taskcreateserviceforcv.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TestDTO {

    Long id;

    @NotBlank(message = "title must not be blank")
    @Size(min = 3, max = 50,
            message = "title`s length must be between 3 and 50 chars")
    String title;

    @Size(max = 1000,
            message = "description`s length must be less than 1000 chars")
    String description;

    @JsonProperty("applicable_areas")
    Set<AreaDTO> applicableAreas = new HashSet<>();

}
