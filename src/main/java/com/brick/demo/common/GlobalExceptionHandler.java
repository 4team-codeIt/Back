package com.brick.demo.common;

import com.brick.demo.auth.controller.AuthController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Getter
@AllArgsConstructor
class ErrorResponse {

  private final String code;
  private final String message;

}

@RestControllerAdvice()
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    logger.error("CustomException 발생: Code = {}, Message = {}", ex.getCode(), ex.getMessage(),
        ex);

    ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
  }
}