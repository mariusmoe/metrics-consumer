package com.moe.metricsconsumer.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
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

  @Value("${mom.staticResource}")
  private String staticPath;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    //  Not really clear from docs what order is prevalent -> blame this guy:
    //  https://visola.github.io/2018/01/17/spa-with-spring-boot/index.html
    registry
      .addResourceHandler( "/**/*.png")
      .setCachePeriod(0)
      .addResourceLocations("file:" + this.staticPath);


    registry.addResourceHandler("/", "/**")
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


}
