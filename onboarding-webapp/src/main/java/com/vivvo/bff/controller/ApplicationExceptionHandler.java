package com.vivvo.bff.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map> handleValidationException(BadRequestException e) {
        return ResponseEntity.badRequest().body(e.getResponse().readEntity(Map.class));
    }

    @ExceptionHandler
    public ResponseEntity<Map> handleNotFoundException(NotFoundException exception) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
