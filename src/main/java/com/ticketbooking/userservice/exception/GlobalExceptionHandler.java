package com.ticketbooking.userservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Validation Exception (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(ApiErrorResponse.validationError(HttpStatus.BAD_REQUEST.value(), request.getRequestURI(), errors), HttpStatus.BAD_REQUEST);
    }

    // 2. Custom Business Exceptions (ResourceNotFound, DuplicateResource, etc.)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof ResourceNotFoundException) status = HttpStatus.NOT_FOUND;
        else if (ex instanceof DuplicateResourceException) status = HttpStatus.CONFLICT;
        else if (ex instanceof BadRequestException) status = HttpStatus.BAD_REQUEST;
        else if (ex instanceof UnauthorizedException) status = HttpStatus.UNAUTHORIZED;
        else if (ex instanceof ForbiddenException) status = HttpStatus.FORBIDDEN;

        ApiErrorResponse response = ApiErrorResponse.error(status.value(), ex.getErrorCode(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(response, status);
    }

    // 8. Invalid JSON Exception
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.BAD_REQUEST.value(), ErrorCode.INVALID_JSON, "Invalid JSON input", request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    // 9. Missing Parameter Exception
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.BAD_REQUEST.value(), ErrorCode.MISSING_PARAMETER, ex.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    // 10. HTTP Method Not Allowed
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.METHOD_NOT_ALLOWED.value(), ErrorCode.METHOD_NOT_ALLOWED, ex.getMessage(), request.getRequestURI()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 11. File Upload Exception
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleFileUpload(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.PAYLOAD_TOO_LARGE.value(), ErrorCode.FILE_UPLOAD_ERROR, "File size exceeds limit", request.getRequestURI()), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    // 12. SQL Exception
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiErrorResponse> handleSqlException(SQLException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SQL_EXCEPTION, "Database error", request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 13. Null Pointer Exception
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiErrorResponse> handleNullPointer(NullPointerException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.NULL_POINTER, "A null pointer occurred", request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 14. Illegal Argument Exception
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.BAD_REQUEST.value(), ErrorCode.ILLEGAL_ARGUMENT, ex.getMessage(), request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }

    // 5. Unauthorized Exception
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(org.springframework.security.core.AuthenticationException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.UNAUTHORIZED.value(), ErrorCode.UNAUTHORIZED, "Full authentication is required to access this resource", request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }

    // 7. Access Denied Exception
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.FORBIDDEN.value(), ErrorCode.FORBIDDEN, "You do not have permission to access this resource", request.getRequestURI()), HttpStatus.FORBIDDEN);
    }

    // 16. Generic Exception (Catch All)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        return new ResponseEntity<>(ApiErrorResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
