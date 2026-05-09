package com.ticketbooking.userservice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
    private boolean success;
    private int status;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private Map<String, String> validationErrors;

    public static ApiErrorResponse error(int status, ErrorCode errorCode, String message, String path) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setSuccess(false);
        response.setStatus(status);
        response.setErrorCode(errorCode.getCode());
        response.setMessage(message != null ? message : errorCode.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setPath(path);
        return response;
    }

    public static ApiErrorResponse validationError(int status, String path, Map<String, String> errors) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setSuccess(false);
        response.setStatus(status);
        response.setErrorCode(ErrorCode.VALIDATION_ERROR.getCode());
        response.setMessage("Validation failed");
        response.setTimestamp(LocalDateTime.now());
        response.setPath(path);
        response.setValidationErrors(errors);
        return response;
    }
}
