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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class MetricsConsumerApplication extends WebSecurityConfigurerAdapter implements CommandLineRunner {

  @Autowired
  private MeasureRepository repository;

    @RequestMapping("/resource")
    public Principal user(Principal principal) {
      return principal;
    }

    public static void main(String[] args) {
        SpringApplication.run(MetricsConsumerApplication.class, args);
    }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .mvcMatchers("/index.html").permitAll()
      .anyRequest().authenticated();
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
