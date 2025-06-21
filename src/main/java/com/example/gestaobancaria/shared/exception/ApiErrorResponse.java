package com.example.gestaobancaria.shared.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        int statusCode,
        String message,
        LocalDateTime timestamp
) {
}
