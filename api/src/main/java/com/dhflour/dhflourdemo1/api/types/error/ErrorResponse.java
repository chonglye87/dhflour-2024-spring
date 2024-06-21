package com.dhflour.dhflourdemo1.api.types.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "error message", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "")
    private String message;
}
