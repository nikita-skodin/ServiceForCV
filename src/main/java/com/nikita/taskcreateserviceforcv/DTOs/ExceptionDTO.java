package com.nikita.taskcreateserviceforcv.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Schema(description = "DTO for representing an exception")
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionDTO {

    @Schema(name = "Error cause", example = "404", title = "exception description")
    String error;

    @Schema(name = "Error message", example = "Not found", title = "exception message")
    String message;
}