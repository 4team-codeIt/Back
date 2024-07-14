package com.brick.demo.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

  private final String code;
  private final HttpStatus httpStatus;
  private final String additionalInfo;

  public CustomException(ErrorDetails errorDetails) {
    super(errorDetails.getMessage());
    this.code = errorDetails.getCode();
    this.httpStatus = errorDetails.getStatus();
    this.additionalInfo = null;
  }


  public CustomException(ErrorDetails errorDetails, String additionalInfo) {
    super(errorDetails.getMessage());
    this.code = errorDetails.getCode();
    this.httpStatus = errorDetails.getStatus();
    this.additionalInfo = additionalInfo;
  }
}