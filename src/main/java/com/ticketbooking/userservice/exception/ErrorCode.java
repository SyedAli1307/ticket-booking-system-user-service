package com.ticketbooking.userservice.exception;

public enum ErrorCode {
    // General Errors
    INTERNAL_SERVER_ERROR("ERR_001", "An unexpected error occurred"),
    INVALID_JSON("ERR_002", "Invalid JSON format"),
    MISSING_PARAMETER("ERR_003", "Missing required parameter"),
    METHOD_NOT_ALLOWED("ERR_004", "HTTP method not allowed"),
    NULL_POINTER("ERR_005", "Null pointer exception"),
    ILLEGAL_ARGUMENT("ERR_006", "Illegal argument provided"),
    
    // Business & Security Errors
    BAD_REQUEST("ERR_101", "Bad request"),
    UNAUTHORIZED("ERR_102", "Authentication failed"),
    FORBIDDEN("ERR_103", "Access denied"),
    VALIDATION_ERROR("ERR_104", "Validation failed"),
    RESOURCE_NOT_FOUND("ERR_105", "Resource not found"),
    DUPLICATE_RESOURCE("ERR_106", "Resource already exists"),
    BUSINESS_ERROR("ERR_107", "Business rule violation"),
    
    // Database Errors
    SQL_EXCEPTION("ERR_201", "Database error occurred"),
    CONSTRAINT_VIOLATION("ERR_202", "Database constraint violation"),
    
    // File Errors
    FILE_UPLOAD_ERROR("ERR_301", "File upload failed");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
