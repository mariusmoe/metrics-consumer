package com.moe.metricsconsumer.apiErrorHandling;

import org.springframework.util.StringUtils;

/**
 * Custom error for entity not found
 *
 * Usually thrown if the request was not found in the db or something is wrong with the db
 */
public class EntityNotFoundException extends Exception {

  public EntityNotFoundException(Class clazz, String... searchParams) {
    super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), searchParams));
  }

  private static String generateMessage(String entity, String[] searchParams) {

    return StringUtils.capitalize(entity) +
      " was not found for parameters: " + String.join(",", searchParams);

  }
}
