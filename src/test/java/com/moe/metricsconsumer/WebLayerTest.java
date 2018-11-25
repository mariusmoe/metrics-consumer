package com.moe.metricsconsumer;


import com.moe.metricsconsumer.controllers.MetricsController;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MetricsController.class)
@ComponentScan
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class WebLayerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MeasureRepository measureRepository;

  @MockBean
  private FvConfigurationRepository fvConfigurationRepository;

  @MockBean
  private XmlRepository xmlRepository;

  @MockBean
  private MongoTemplate mongoTemplate;

  @MockBean
  private ResourceServerProperties resourceServerProperties;

  @MockBean
  private AchievementRepository achievementRepository;

  @MockBean
  private UserAchievementRepository userAchievementRepository;



  @Test
  public void shouldReturnDefaultMessage() throws Exception {
    List<SpecificMeasure> someSpecificMeasure = new ArrayList<>();
    someSpecificMeasure.add(new SpecificMeasure("cyclomatic Complexity", 7));
    List<SpecificMeasure> someSpecificMeasure21 = new ArrayList<>();
    someSpecificMeasure21.add(new SpecificMeasure("for", 7));
    someSpecificMeasure21.add(new SpecificMeasure("foreach", 4));
    someSpecificMeasure21.add(new SpecificMeasure("while", 1));

    List<Measure> someMeasure = new ArrayList<>();
    someMeasure.add(new Measure("org.metrics.cyclomatic", someSpecificMeasure));
    someMeasure.add(new Measure("no.hal.javalang", someSpecificMeasure21));
    List<MeasureSummary> result = new ArrayList<>();
    result.add(new MeasureSummary("001","Account-oppgave", "stateandbehavior.Account", someMeasure));

    given(this.measureRepository.findAllByUserId("001"))
      .willReturn(
        result
      );
    this.mockMvc.perform(get("/api/"))
      .andExpect(status().isOk())
      .andExpect(content().string("[{\"id\":null,\"userId\":\"001\",\"taskName\":\"Account-oppgave\",\"taskId\":\"stateandbehavior.Account\",\"measures\":[{\"measureProvider\":\"org.metrics.cyclomatic\",\"specificMeasures\":[{\"name\":\"cyclomatic Complexity\",\"value\":7.0}]},{\"measureProvider\":\"no.hal.javalang\",\"specificMeasures\":[{\"name\":\"for\",\"value\":7.0},{\"name\":\"foreach\",\"value\":4.0},{\"name\":\"while\",\"value\":1.0}]}],\"solutionManual\":false}]"))
      .andDo(MockMvcResultHandlers.print());
//    .andExpect(content().string(containsString("Hello World")));
//      .andDo(document("home", responseFields(
//        fieldWithPath("message").description("The welcome message for the user.")
//      )));
  }


}
