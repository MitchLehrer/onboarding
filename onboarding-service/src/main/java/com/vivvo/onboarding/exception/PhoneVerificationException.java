package com.vivvo.onboarding.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneVerificationException extends RuntimeException {
    public PhoneVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
