package com.brick.demo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

class ErrorResponse {

  private final String code;
  private final String message;

  ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }
}

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    logger.error("CustomException occurred: Code = {}, Message = {}", ex.getCode(), ex.getMessage(),
        ex);

    ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
  }
}