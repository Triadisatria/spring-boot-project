package com.project.miniproject.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Object> handleRequestException(RequestException exception){
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setResponseCode(String.valueOf(exception.getHttpStatus()));
        errorResponse.setResponseMessage(exception.getMessage());

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(errorResponse);
    }
}
