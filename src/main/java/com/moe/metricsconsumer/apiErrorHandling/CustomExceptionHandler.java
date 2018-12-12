package com.moe.metricsconsumer.apiErrorHandling;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  ObjectMapper mapper;


  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
    ApiError apiError = new ApiError(NOT_FOUND,EntityNotFoundException.class.getSimpleName(), ex);
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(CouldNotSaveException.class)
  protected ResponseEntity<Object> handleEntityNotFound(CouldNotSaveException ex) {
    ApiError apiError = new ApiError(NOT_FOUND,CouldNotSaveException.class.getSimpleName(), ex);
    return buildResponseEntity(apiError);
  }


  @ExceptionHandler(MultipartException.class)
  public ObjectNode handleError1(MultipartException e, RedirectAttributes redirectAttributes) {
    ObjectNode res = mapper.createObjectNode();
    res.put("message", e.getCause().getMessage());
    res.put("status", "4000");

    return res;
  }

  //CommonsMultipartResolver
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ObjectNode handleError2(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
    ObjectNode res = mapper.createObjectNode();
    res.put("message", e.getCause().getMessage());
    res.put("status", "4000");

    return res;
  }
}
