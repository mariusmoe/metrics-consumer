package com.moe.metricsconsumer.apiErrorHandling;

import org.springframework.util.StringUtils;

public class CouldNotSaveException extends Throwable {

  public CouldNotSaveException(Class clazz, String... searchParams) {
    super(CouldNotSaveException.generateMessage(clazz.getSimpleName(), searchParams));
  }

  private static String generateMessage(String entity, String[] searchParams) {

    return StringUtils.capitalize(entity) +
      " Could not save for parameters: " + String.join(",", searchParams);

  }
}
