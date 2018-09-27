package com.moe.metricsconsumer;

import com.moe.metricsconsumer.repositories.MeasureRepository;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableOAuth2Sso
public class MetricsConsumerApplication extends WebSecurityConfigurerAdapter implements CommandLineRunner {

  @Autowired
  private MeasureRepository repository;



    public static void main(String[] args) {
        SpringApplication.run(MetricsConsumerApplication.class, args);
    }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
      .antMatchers("/index.html", "/login").permitAll()
      .anyRequest().authenticated()
      .and().logout()
      .logoutSuccessUrl("/some/login")
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
      .deleteCookies("JSESSIONID").invalidateHttpSession(false);

      // Should redirect onlogoutSuccess to bellow, but then cors and csrf has to be fixed!
//    https://auth.dataporten.no/logout

  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("https://auth.dataporten.no", "http://localhost:8080"));
    configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

    @Override
    public void run(String... args) throws Exception{
      repository.deleteAll();

      //save some
      List<SpecificMeasure> someSpecificMeasure = new ArrayList<>();
      someSpecificMeasure.add(new SpecificMeasure("cyclomaticComplexity", 7));
      List<Measure> someMeasure = new ArrayList<>();
      someMeasure.add(new Measure("org.metrics.cyclomatic", someSpecificMeasure));

      repository.save(new MeasureSummary("Account-oppgave", "stateandbehavior.Account", someMeasure));
      repository.save(new MeasureSummary("Account-oppgave", "stateandbehavior.Account", someMeasure));


      // fetch all
      System.out.println("MeasureSummary found with findAll():");
      System.out.println("------------------------------------");
      for (MeasureSummary measureSummary : repository.findAll()) {
        System.out.println(measureSummary);
      }
      System.out.println();

      // fetch an induvidial MeasureSummary
      System.out.println("Customer found with findByFirstName('Alice'):");
      System.out.println("--------------------------------");
      System.out.println(repository.findByTaskName("Alice"));

      System.out.println("Customers found with findByLastName('Account-oppgave'):");
      System.out.println("--------------------------------");
      for (MeasureSummary x : repository.findByTaskName("Account-oppgave")) {
        System.out.println(x);
      }

    }
}
