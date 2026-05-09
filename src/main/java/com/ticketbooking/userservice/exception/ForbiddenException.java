package com.ticketbooking.userservice.exception;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(String message) {
        super(message, ErrorCode.FORBIDDEN);
    }
}
