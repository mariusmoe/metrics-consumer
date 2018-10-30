package com.moe.metricsconsumer;

import com.moe.metricsconsumer.repositories.MeasureRepository;

import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;

import no.hal.learning.fv.ExpressionFeatures;
import no.hal.learning.fv.FeatureList;
import no.hal.learning.fv.FilteredFeatures2;
import no.hal.learning.fv.FvFactory;
import no.hal.learning.fv.impl.FeatureListImpl;
import no.hal.learning.fv.util.Op1;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMapUtil.FeatureValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

import org.springframework.context.annotation.Bean;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.context.annotation.FilterType.CUSTOM;
import static org.springframework.http.HttpMethod.POST;

@SpringBootApplication
public class MetricsConsumerApplication implements CommandLineRunner {

  @Autowired
  private MeasureRepository repository;



  public static void main(String[] args) {
        SpringApplication.run(MetricsConsumerApplication.class, args);
    }



  
  	



//  // TODO - remove!
//  @Override
//  public void configure(WebSecurity webSecurity) throws Exception {
//    webSecurity.ignoring().antMatchers(POST, "/api");
//  }



  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("https://auth.dataporten.no", "http://localhost:8080"));
    configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT", "OPTIONS"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

    @Override
    public void run(String... args) throws Exception{
      repository.deleteAll();

      //save some dummy data
      List<SpecificMeasure> someSpecificMeasure = new ArrayList<>();
      someSpecificMeasure.add(new SpecificMeasure("cyclomatic complexity", 7));
      List<SpecificMeasure> someSpecificMeasure21 = new ArrayList<>();
      someSpecificMeasure21.add(new SpecificMeasure("for", 7));
      someSpecificMeasure21.add(new SpecificMeasure("foreach", 4));
      someSpecificMeasure21.add(new SpecificMeasure("while", 1));

      List<Measure> someMeasure = new ArrayList<>();
      someMeasure.add(new Measure("org.metrics.cyclomatic", someSpecificMeasure));
      someMeasure.add(new Measure("no.hal.javalang", someSpecificMeasure21));


      List<SpecificMeasure> someSpecificMeasure2 = new ArrayList<>();
      someSpecificMeasure2.add(new SpecificMeasure("cyclomatic complexity 2", 3));
      List<Measure> someMeasure2 = new ArrayList<>();
      someMeasure2.add(new Measure("org.metrics.cyclomatic.2", someSpecificMeasure2));

      List<SpecificMeasure> someSpecificMeasure3 = new ArrayList<>();
      someSpecificMeasure3.add(new SpecificMeasure("cyclomatic complexity 3", 5));
      List<Measure> someMeasure3 = new ArrayList<>();
      someMeasure3.add(new Measure("org.metrics.cyclomatic.3", someSpecificMeasure3));

      repository.save(new MeasureSummary("001","Account-oppgave", "stateandbehavior.Account", someMeasure));
      repository.save(new MeasureSummary("001","Location-oppgave", "stateandbehavior.Location", someMeasure2));
      repository.save(new MeasureSummary("001","Digit-oppgave", "stateandbehavior.Digit", someMeasure3));




      List<SpecificMeasure> specificMeasureSolution1 = new ArrayList<>();
      specificMeasureSolution1.add(new SpecificMeasure("cyclomatic complexity", 3));
      List<SpecificMeasure> specificMeasureSolution12 = new ArrayList<>();
      specificMeasureSolution12.add(new SpecificMeasure("for", 4));
      specificMeasureSolution12.add(new SpecificMeasure("foreach", 6));
      specificMeasureSolution12.add(new SpecificMeasure("while", 1));

      List<Measure> someMeasureSolution = new ArrayList<>();
      someMeasureSolution.add(new Measure("org.metrics.cyclomatic", specificMeasureSolution1));
      someMeasureSolution.add(new Measure("no.hal.javalang", specificMeasureSolution12));


      repository.save(new MeasureSummary("002", true,"Account-oppgave", "stateandbehavior.Account", someMeasureSolution));
      repository.save(new MeasureSummary("002", true,"Location-oppgave", "stateandbehavior.Location", someMeasure2));
      repository.save(new MeasureSummary("002", true, "Digit-oppgave", "stateandbehavior.Digit", someMeasure3));






      FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
		featureList.getFeatures().put("f1",  1.0);
		featureList.getFeatures().put("f2",  2.0);
       
       System.out.println("------------------------------------");
       System.out.println("------------------------------------");
       System.out.println("------------------------------------");
       System.out.println(featureList.toString());
       System.out.println(featureList.getFeatureNames());
       featureList.set(FeatureListImpl.valueOf("f1", 3.0, "f3", 4.0), false);
       System.out.println(featureList.toString());
       featureList.set(FeatureListImpl.valueOf("f1", 9.0, "f5", 4.0), true);
       System.out.println(featureList.toString());
       System.out.println("************************************");
       featureList.apply(Op1.NEG);
       System.out.println(featureList.toString());
       FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
       
       
		FeatureList f = FvFactory.eINSTANCE.createFeatureList();
		f.getFeatures().put("m_s",  1.0);
		f.getFeatures().put("n",  2.0);
		ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
		//expressionFeatures.getContained().add(f);
		expressionFeatures.setOther(f);
		expressionFeatures.getFeatures().put("sum", "m_s+n");
		expressionFeatures.getFeatures().put("product and one", "m_s * n + 1");

		//expressionFeatures.getFeatures().put("average", "");
		
		
		System.out.println(expressionFeatures.getFeatureNames());
		System.out.println(expressionFeatures.getFeatures().toString());

		System.out.println(expressionFeatures.getFeatureValue("sum"));
		System.out.println(expressionFeatures.getFeatureValue("product and one"));

		// System.out.println(expressionFeatures.getFeatureValue("average"));

       System.out.println("------------------------------------");
       System.out.println("------------------------------------");
       System.out.println("------------------------------------");
       





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
