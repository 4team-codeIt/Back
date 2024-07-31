package com.brick.demo.common;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
    logger.error("CustomException 발생: Code = {}, Message = {}", ex.getCode(), ex.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class) // @RequestBody에서 유효성 검사가 실패할때
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  // @RequestParam, @PathVariable, 메서드 파라미터에서 유효성 검사가 실패할때
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(
            violation -> {
              String fieldName = violation.getPropertyPath().toString();
              String errorMessage = violation.getMessage();
              errors.put(fieldName, errorMessage);
            });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    logger.error(
        "MissingServletRequestParameterException 발생: Parameter = {}, Message = {}",
        ex.getParameterName(),
        ex.getMessage(),
        ex);

    String code = "MISSING_PARAMETER";
    String message = String.format("필수 파라미터 '%s'가 누락되었습니다.", ex.getParameterName());
    ErrorResponse errorResponse = new ErrorResponse(code, message);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
