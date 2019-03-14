package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.apiErrorHandling.CouldNotSaveException;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.repositories.*;
import com.moe.metricsconsumer.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MetricsControllerFvTest {


  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MetricsController metricsController;

  @Autowired
  ObjectMapper mapper;

  @MockBean
  private MeasureRepository measureRepository;

  @MockBean
  private AchievementRepository achievementRepository;

  @MockBean
  private UserAchievementRepository userAchievementRepository;

  @MockBean
  private FvConfigurationRepository fvConfigurationRepository;

  @MockBean
  private XmlRepository xmlRepository;

  @MockBean
  private MongoTemplate mongoTemplate;

  @MockBean
  private ResourceServerProperties resourceServerProperties;

  List<MeasureSummary> measureSummaryList;
  MeasureSummary measureSummary;
  MeasureSummary measureSummarySolution;

  @Before
  public void setUp() throws Exception {

    List<SpecificMeasure> someSpecificMeasure = new ArrayList<>();
    someSpecificMeasure.add(new SpecificMeasure("cyclomatic Complexity", 7));
    List<SpecificMeasure> someSpecificMeasure21 = new ArrayList<>();
    someSpecificMeasure21.add(new SpecificMeasure("for", 7));
    someSpecificMeasure21.add(new SpecificMeasure("foreach", 4));
    someSpecificMeasure21.add(new SpecificMeasure("while", 1));

    List<Measure> someMeasure = new ArrayList<>();
    someMeasure.add(new Measure("org.metrics.cyclomatic", someSpecificMeasure));
    someMeasure.add(new Measure("no.hal.javalang", someSpecificMeasure21));
    measureSummaryList = new ArrayList<>();
    measureSummaryList.add(new MeasureSummary("001","Account-oppgave", "stateandbehavior.Account", someMeasure));

    measureSummary = new MeasureSummary("001","Account-oppgave", "stateandbehavior.Account", someMeasure);
    measureSummarySolution = new MeasureSummary("001", true,"Account-oppgave", "stateandbehavior.Account", someMeasure);

  }

  @After
  public void tearDown() throws Exception {
    measureSummaryList.clear();
  }

  @Test
  public void getOneMeasureFv() throws Exception, CouldNotSaveException {
    // NYI
    ObjectNode res = mapper.createObjectNode();
    res.put("measureSummaryRef", measureSummary.getId());
    res.put("measureSummary", measureSummary.toString());
    res.put("status", "2000");
    given(this.metricsController.saveMeasureSummary(measureSummary, null))
      .willReturn(
        res
      );
    this.mockMvc.perform(get("/api/"))
      .andExpect(status().isOk())
      .andExpect(content().json("{\"measureSummaryRef\":\"lyHhaVtqDwXCI5Yo2by/Cf+YxKF1+go1/OM5J7PvC7s=\",\"measureSummary\":\"MeasureSummary(id=lyHhaVtqDwXCI5Yo2by/Cf+YxKF1+go1/OM5J7PvC7s=, userId=001, isSolutionManual=true, taskName=Account-oppgave, taskId=stateandbehavior.Account, measures=[Measure{measureProvider='org.metrics.cyclomatic', specificMeasures=[SpecificMeasure{name='cyclomatic Complexity', value=7.0}]}, Measure{measureProvider='no.hal.javalang', specificMeasures=[SpecificMeasure{name='for', value=7.0}, SpecificMeasure{name='foreach', value=4.0}, SpecificMeasure{name='while', value=1.0}]}])\",\"status\":\"2000\"}"))
      .andDo(MockMvcResultHandlers.print());
  }

  @Test
  public void getOneSolutionMeasureFv() {
  }
}
