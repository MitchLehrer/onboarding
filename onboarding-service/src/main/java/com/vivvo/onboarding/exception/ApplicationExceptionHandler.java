package com.vivvo.onboarding.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @Autowired
    private MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler
    public ResponseEntity<Map> handleNotFoundException(NotFoundException exception) {
        Map<String, String> body = Collections.singletonMap("message", "Could not find resource by id " + exception.getId());
        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map> handlePhoneVerificationException(PhoneVerificationException exception) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("phoneNumber", exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map> handleValidationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(getTranslatedErrors(exception.getErrors()));
    }

    private Map<String, String> getTranslatedErrors(Map<String, String> untranslatedErrors) {
        return untranslatedErrors
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> messageSourceAccessor.getMessage(e.getValue(), e.getValue(), LocaleContextHolder.getLocale())));

    }

}
