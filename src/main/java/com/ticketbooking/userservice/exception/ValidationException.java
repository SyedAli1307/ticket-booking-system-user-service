package com.ticketbooking.userservice.exception;

import lombok.Getter;
import java.util.Map;

@Getter
public class ValidationException extends BusinessException {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Validation failed", ErrorCode.VALIDATION_ERROR);
        this.errors = errors;
    }
}
