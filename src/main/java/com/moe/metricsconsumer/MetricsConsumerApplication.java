package com.moe.metricsconsumer;

import com.moe.metricsconsumer.controllers.ConfigCreator;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.models.rewards.AchievementState;
import com.moe.metricsconsumer.models.FvConfiguration;
import com.moe.metricsconsumer.models.rewards.UserAchievement;
import com.moe.metricsconsumer.repositories.*;


import javafx.util.Pair;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.eclipse.emf.ecore.resource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.io.*;
import java.nio.file.Files;
import java.util.*;

@SpringBootApplication
@EnableConfigurationProperties
public class MetricsConsumerApplication implements CommandLineRunner {

  @Autowired
  private MeasureRepository repository;

  @Autowired
  private FvConfigurationRepository fvConfigurationRepository;

  @Autowired
  private AchievementRepository achievementRepository;

  @Autowired
  private UserAchievementRepository userAchievementRepository;

  @Autowired
  private ExerciseDocumentRepository exerciseDocumentRepository;



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
      fvConfigurationRepository.deleteAll();
      achievementRepository.deleteAll();
      userAchievementRepository.deleteAll();
      exerciseDocumentRepository.deleteAll();



//      //save some dummy data
//      List<SpecificMeasure> someSpecificMeasure = new ArrayList<>();
//      someSpecificMeasure.add(new SpecificMeasure("cyclomatic complexity", 7));
//      List<SpecificMeasure> someSpecificMeasure21 = new ArrayList<>();
//      someSpecificMeasure21.add(new SpecificMeasure("for", 7));
//      someSpecificMeasure21.add(new SpecificMeasure("foreach", 4));
//      someSpecificMeasure21.add(new SpecificMeasure("while", 1));
//
//      List<Measure> someMeasure = new ArrayList<>();
//      someMeasure.add(new Measure("org.metrics.cyclomatic", someSpecificMeasure));
//      someMeasure.add(new Measure("no.hal.javalang", someSpecificMeasure21));
//
//
//      List<SpecificMeasure> someSpecificMeasure2 = new ArrayList<>();
//      someSpecificMeasure2.add(new SpecificMeasure("cyclomatic complexity 2", 3));
//      List<Measure> someMeasure2 = new ArrayList<>();
//      someMeasure2.add(new Measure("org.metrics.cyclomatic.2", someSpecificMeasure2));
//
//      List<SpecificMeasure> someSpecificMeasure3 = new ArrayList<>();
//      someSpecificMeasure3.add(new SpecificMeasure("cyclomatic complexity 3", 5));
//      List<Measure> someMeasure3 = new ArrayList<>();
//      someMeasure3.add(new Measure("org.metrics.cyclomatic.3", someSpecificMeasure3));
//
//      repository.save(new MeasureSummary("001","Account-oppgave", "stateandbehavior.Account", someMeasure));
//      repository.save(new MeasureSummary("001","Location-oppgave", "stateandbehavior.Location", someMeasure2));
//      repository.save(new MeasureSummary("001","Digit-oppgave", "stateandbehavior.Digit", someMeasure3));
//
//
//
//
//      List<SpecificMeasure> specificMeasureSolution1 = new ArrayList<>();
//      specificMeasureSolution1.add(new SpecificMeasure("cyclomatic complexity", 3));
//      List<SpecificMeasure> specificMeasureSolution12 = new ArrayList<>();
//      specificMeasureSolution12.add(new SpecificMeasure("for", 4));
//      specificMeasureSolution12.add(new SpecificMeasure("foreach", 6));
//      specificMeasureSolution12.add(new SpecificMeasure("while", 1));
//
//      List<Measure> someMeasureSolution = new ArrayList<>();
//      someMeasureSolution.add(new Measure("org.metrics.cyclomatic", specificMeasureSolution1));
//      someMeasureSolution.add(new Measure("no.hal.javalang", specificMeasureSolution12));
//
//
//      repository.save(new MeasureSummary("002", true,"Account-oppgave", "stateandbehavior.Account", someMeasureSolution));
//      repository.save(new MeasureSummary("002", true,"Location-oppgave", "stateandbehavior.Location", someMeasure2));
//      repository.save(new MeasureSummary("002", true, "Digit-oppgave", "stateandbehavior.Digit", someMeasure3));
//





//      FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
//		featureList.getFeatures().put("f1",  1.0);
//		featureList.getFeatures().put("f2",  2.0);
//
//       System.out.println("------------------------------------");
//       System.out.println("------------------------------------");
//       System.out.println("------------------------------------");
//       System.out.println(featureList.toString());
//       System.out.println(featureList.getFeatureNames());
//       featureList.set(FeatureListImpl.valueOf("f1", 3.0, "f3", 4.0), false);
//       System.out.println(featureList.toString());
//       featureList.set(FeatureListImpl.valueOf("f1", 9.0, "f5", 4.0), true);
//       System.out.println(featureList.toString());
//       System.out.println("************************************");
//       featureList.apply(Op1.NEG);
//       System.out.println(featureList.toString());
//       FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
//
//
//		FeatureList f = FvFactory.eINSTANCE.createFeatureList();
//		f.getFeatures().put("m_s",  1.0);
//		f.getFeatures().put("n",  2.0);
//		ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
//		//expressionFeatures.getContained().add(f);
//		expressionFeatures.setOther(f);
//		expressionFeatures.getFeatures().put("sum", "m_s+n");
//		expressionFeatures.getFeatures().put("product and one", "m_s * n + 1");
//
//		//expressionFeatures.getFeatures().put("average", "");
//
//
//		System.out.println(expressionFeatures.getFeatureNames());
//		System.out.println(expressionFeatures.getFeatures().toString());
//
//		System.out.println(expressionFeatures.getFeatureValue("sum"));
//		System.out.println(expressionFeatures.getFeatureValue("product and one"));

        File tempScript = File.createTempFile("script", "sh");

        Writer streamWriter = new OutputStreamWriter(new FileOutputStream(
          tempScript));
        PrintWriter printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        printWriter.println("export PATH=$PATH:/home/moe/.nvm/versions/node/v8.12.0/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin:/usr/lib/jvm/java-8-oracle/bin:/usr/lib/jvm/java-8-oracle/db/bin:/usr/lib/jvm/java-8-oracle/jre/bin");
        printWriter.println("newman run ~/Documents/Archive/metrics-consumer.postman_collection.json --folder 000_populate_db");

        printWriter.close();

        try {
          ProcessBuilder pb = new ProcessBuilder("bash", tempScript.toString());
          pb.inheritIO();
          Process process = pb.start();
          process.waitFor();
        } finally {
          tempScript.delete();
        }

      ConfigCreator configCreator = new ConfigCreator();
      configCreator.createAchievementConfig();
      configCreator.createAchievementConfig2();


      fvConfigurationRepository.save(new FvConfiguration("stateandbehavior.Account",new Binary(configCreator.createFvConfig())));

      fvConfigurationRepository.save(new FvConfiguration("stateandbehavior.Location",new Binary(configCreator.createFvConfig2())));

      fvConfigurationRepository.save(new FvConfiguration("stateandbehavior.Digit",new Binary(configCreator.createFvConfig3())));
      fvConfigurationRepository.save(new FvConfiguration("files",new Binary(configCreator.createFvConfig4())));

      ArrayList<byte[]> temp = configCreator.createAchievementConfig4();
      byte[] dataResourceByteArray5 = temp.get(0);
      byte[] configResourceByteArray5 = temp.get(1);

      ArrayList<byte[]> temp3 = configCreator.createAchievementConfig3();
      byte[] dataResourceByteArray3 = temp3.get(0);
      byte[] configResourceByteArray3 = temp3.get(1);

      ArrayList<byte[]> temp2 = configCreator.createAchievementConfig2();
      byte[] dataResourceByteArray2 = temp2.get(0);
      byte[] configResourceByteArray2 = temp2.get(1);
     // Resource resource =
      // TODO: Change these according to notes!

      String taskIdRefAccount = "stateandbehavior.Account";
      Achievement achievement1 = new Achievement(
        11,
        false,
        taskIdRefAccount,
        new Binary(configResourceByteArray5),
        new Binary(dataResourceByteArray5),
        //new Binary(BsonBinarySubType.BINARY, uploadedFile.getBytes()),
        "For expert For for",
        "Award for exceptional work on for loops"
        );

//      achievementConfig2
      Achievement achievement2 = new Achievement(9,
        false,
        taskIdRefAccount,
        new Binary(configResourceByteArray3),
        new Binary(dataResourceByteArray3),
        "While expert super",
        "Award for exceptional work on while nr1"
        );

      Achievement achievement3 = new Achievement(3,
        true,
        taskIdRefAccount,
        new Binary(configResourceByteArray2),
        new Binary(dataResourceByteArray2),
        "While expert easy",
        "Award for exceptional work on while next"
      );

      achievementRepository.save(achievement1);
      achievementRepository.save(achievement2);
      achievementRepository.save(achievement3);

//      List<Achievement> achievementList = achievementRepository.findAll();
//
//      UserAchievement userAchievement = new UserAchievement("001", achievementList.get(0).getId(), AchievementState.REVEALED, null, null);
//      userAchievementRepository.save(userAchievement);
//      UserAchievement userAchievement2 = new UserAchievement("001", achievementList.get(1).getId(), AchievementState.REVEALED, null, null);
//      userAchievementRepository.save(userAchievement2);



    }
}
