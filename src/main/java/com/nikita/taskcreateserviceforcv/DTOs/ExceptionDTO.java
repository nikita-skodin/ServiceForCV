package com.nikita.taskcreateserviceforcv.DTOs;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionDTO {
    String error;
    String message;
}