package com.moe.metricsconsumer.config;

import java.io.IOException;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/**/*")
      .addResourceLocations("classpath:/static/")
      .resourceChain(true)
      .addResolver(new PathResourceResolver() {
        @Override
        protected Resource getResource(String resourcePath,
                                       Resource location) throws IOException {
          Resource requestedResource = location.createRelative(resourcePath);
          return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
            : new ClassPathResource("/static/index.html");
        }
      });
  }

//  @Override
//  public void addViewControllers(ViewControllerRegistry registry) {
//    registry.addViewController("/notFound").setViewName("forward:/index.html");
//  }
//
//
//  @Bean
//  public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
//    return container -> {
//      container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
//        "/user/login"));
//    };
//  }

}
