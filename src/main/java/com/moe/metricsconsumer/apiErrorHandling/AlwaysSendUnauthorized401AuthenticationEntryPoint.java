package com.moe.metricsconsumer.apiErrorHandling;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AlwaysSendUnauthorized401AuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public final void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
    System.out.println("Pre-authenticated entry point called. Rejecting access");
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
