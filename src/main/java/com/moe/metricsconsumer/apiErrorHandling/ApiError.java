package com.moe.metricsconsumer.apiErrorHandling;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getDetailedErrorMessage() {
    return detailedErrorMessage;
  }

  public void setDetailedErrorMessage(String detailedErrorMessage) {
    this.detailedErrorMessage = detailedErrorMessage;
  }

  private HttpStatus status;
  private LocalDateTime timestamp;
  private String errorMessage;
  private String detailedErrorMessage;


  ApiError(HttpStatus status) {
    this();
    this.status = status;
  }

  ApiError(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.detailedErrorMessage = ex.getLocalizedMessage();
  }


  private ApiError() {
    timestamp = LocalDateTime.now();
  }

  ApiError(HttpStatus status, String errorMessage, Throwable ex) {
    this();
    this.status = status;
    this.errorMessage = errorMessage;
    this.detailedErrorMessage = ex.getLocalizedMessage();

  }
}
