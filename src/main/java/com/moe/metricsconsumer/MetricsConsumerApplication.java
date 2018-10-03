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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.POST;

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
    http.csrf().disable().authorizeRequests().antMatchers("/").permitAll();


//     http
//      .csrf().disable()
//      .authorizeRequests()
//      .antMatchers("/index.html", "/login").permitAll()
//      .anyRequest().authenticated()
//      .and().logout()
//      .logoutSuccessUrl("/some/login")
//      .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
//      .deleteCookies("JSESSIONID").invalidateHttpSession(false);

      // Should redirect onlogoutSuccess to bellow, but then cors and csrf has to be fixed!
//    https://auth.dataporten.no/logout

  }

  // TODO - remove!
  @Override
  public void configure(WebSecurity webSecurity) throws Exception {
    webSecurity.ignoring().antMatchers(POST, "/api");
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

      //save some dummy data
      List<SpecificMeasure> someSpecificMeasure = new ArrayList<>();
      someSpecificMeasure.add(new SpecificMeasure("cyclomatic Complexity", 7));
      List<SpecificMeasure> someSpecificMeasure21 = new ArrayList<>();
      someSpecificMeasure21.add(new SpecificMeasure("for", 7));
      someSpecificMeasure21.add(new SpecificMeasure("foreach", 4));
      someSpecificMeasure21.add(new SpecificMeasure("while", 1));

      List<Measure> someMeasure = new ArrayList<>();
      someMeasure.add(new Measure("org.metrics.cyclomatic", someSpecificMeasure));
      someMeasure.add(new Measure("no.hal.javalang", someSpecificMeasure21));


      List<SpecificMeasure> someSpecificMeasure2 = new ArrayList<>();
      someSpecificMeasure2.add(new SpecificMeasure("cyclomatic Complexity 2", 3));
      List<Measure> someMeasure2 = new ArrayList<>();
      someMeasure2.add(new Measure("org.metrics.cyclomatic.2", someSpecificMeasure2));

      List<SpecificMeasure> someSpecificMeasure3 = new ArrayList<>();
      someSpecificMeasure3.add(new SpecificMeasure("cyclomatic Complexity 3", 5));
      List<Measure> someMeasure3 = new ArrayList<>();
      someMeasure3.add(new Measure("org.metrics.cyclomatic.3", someSpecificMeasure3));

      repository.save(new MeasureSummary("001","Account-oppgave", "stateandbehavior.Account", someMeasure));
      repository.save(new MeasureSummary("001","Location-oppgave", "stateandbehavior.Location", someMeasure2));
      repository.save(new MeasureSummary("001","Digit-oppgave", "stateandbehavior.Digit", someMeasure3));

//      //save some dummy data
//      List<SpecificMeasure> someSpecificMeasure4 = new ArrayList<>();
//      someSpecificMeasure4.add(new SpecificMeasure("cyclomatic Complexity", 3));
//      List<Measure> someMeasure4 = new ArrayList<>();
//      someMeasure4.add(new Measure("org.metrics.cyclomatic", someSpecificMeasure4));
//
//
//      List<SpecificMeasure> someSpecificMeasure5 = new ArrayList<>();
//      someSpecificMeasure5.add(new SpecificMeasure("cyclomatic Complexity 2", 2));
//      List<Measure> someMeasure5 = new ArrayList<>();
//      someMeasure5.add(new Measure("org.metrics.cyclomatic.2", someSpecificMeasure5));
//
//      List<SpecificMeasure> someSpecificMeasure6 = new ArrayList<>();
//      someSpecificMeasure6.add(new SpecificMeasure("cyclomatic Complexity 3", 6));
//      List<Measure> someMeasure6 = new ArrayList<>();
//      someMeasure6.add(new Measure("org.metrics.cyclomatic.3", someSpecificMeasure6));
//
//      repository.save(new MeasureSummary("001",true,"Account-oppgave", "stateandbehavior.Account", someMeasure4));
//      repository.save(new MeasureSummary("001",true,"Account-oppgave", "stateandbehavior.Account", someMeasure5));
//      repository.save(new MeasureSummary("001",true,"Account-oppgave", "stateandbehavior.Account", someMeasure6));

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
