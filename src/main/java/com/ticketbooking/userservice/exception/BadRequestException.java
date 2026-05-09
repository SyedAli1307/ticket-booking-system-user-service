package com.ticketbooking.userservice.exception;

public class BadRequestException extends BusinessException {
    public BadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST);
    }
}
