package com.nikita.taskcreateserviceforcv.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Schema(description = "DTO for representing an area")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AreaDTO {

    @Schema(name = "id", example = "1", title = "area`s id")
    Long id;

    @Schema(name = "title", example = "Backend-Java", title = "area`s title")
    @NotBlank(message = "title must not be blank")
    @Size(min = 3, max = 50,
            message = "title`s length must be between 3 and 50 chars")
    String title;

    @Schema(name = "description", example = "Development of microservices in java/spring", title = "area`s description")
    @Size(max = 1000,
            message = "description`s length must be less than 1000 chars")
    String description;

}
