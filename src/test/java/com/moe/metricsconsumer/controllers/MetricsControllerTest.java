package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.repositories.FvConfigurationRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import com.moe.metricsconsumer.repositories.XmlRepository;
import org.junit.After;
import org.junit.Before;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MetricsController.class, secure = false)
@ComponentScan
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class MetricsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private MetricsController metricsController;

  @Autowired
  ObjectMapper mapper;

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
  public void getAllMeasureSummaries() throws Exception {
    given(this.measureRepository.findAllByUserId("001"))
      .willReturn(
        measureSummaryList
      );
    this.mockMvc.perform(get("/api/"))
      .andExpect(status().isOk())
      .andExpect(content().json("[{\"id\":null,\"userId\":\"001\",\"taskName\":\"Account-oppgave\",\"taskId\":\"stateandbehavior.Account\",\"measures\":[{\"measureProvider\":\"org.metrics.cyclomatic\",\"specificMeasures\":[{\"name\":\"cyclomatic Complexity\",\"value\":7.0}]},{\"measureProvider\":\"no.hal.javalang\",\"specificMeasures\":[{\"name\":\"for\",\"value\":7.0},{\"name\":\"foreach\",\"value\":4.0},{\"name\":\"while\",\"value\":1.0}]}],\"solutionManual\":false}]"))
      .andDo(MockMvcResultHandlers.print());
  }

  @Test
  public void getMeasureSummary() throws Exception {
    given(this.measureRepository.findFirstByUserIdAndTaskId("001", "stateandbehavior.Account"))
      .willReturn(
        measureSummary
      );
    this.mockMvc.perform(get("/api/stateandbehavior.Account"))
      .andExpect(status().isOk())
      .andExpect(content().json("{\"id\":null,\"userId\":\"001\",\"taskName\":\"Account-oppgave\",\"taskId\":\"stateandbehavior.Account\",\"measures\":[{\"measureProvider\":\"org.metrics.cyclomatic\",\"specificMeasures\":[{\"name\":\"cyclomatic Complexity\",\"value\":7.0}]},{\"measureProvider\":\"no.hal.javalang\",\"specificMeasures\":[{\"name\":\"for\",\"value\":7.0},{\"name\":\"foreach\",\"value\":4.0},{\"name\":\"while\",\"value\":1.0}]}],\"solutionManual\":false}"))
      .andDo(MockMvcResultHandlers.print());

  }

  @Test
  public void getSolutionMeasureSummary() throws Exception {
    Query query = new Query();
    query.addCriteria(Criteria.where("isSolutionManual").is(true));
    query.addCriteria(Criteria.where("taskId").is("stateandbehavior.Account"));


    given(this.mongoTemplate.findOne(query, MeasureSummary.class))
      .willReturn(
        measureSummarySolution
      );
    this.mockMvc.perform(get("/api/solution/stateandbehavior.Account"))
      .andExpect(status().isOk())
      .andExpect(content().json("{\"id\":null,\"userId\":\"001\",\"taskName\":\"Account-oppgave\",\"taskId\":\"stateandbehavior.Account\",\"measures\":[{\"measureProvider\":\"org.metrics.cyclomatic\",\"specificMeasures\":[{\"name\":\"cyclomatic Complexity\",\"value\":7.0}]},{\"measureProvider\":\"no.hal.javalang\",\"specificMeasures\":[{\"name\":\"for\",\"value\":7.0},{\"name\":\"foreach\",\"value\":4.0},{\"name\":\"while\",\"value\":1.0}]}],\"solutionManual\":true}"))
      .andDo(MockMvcResultHandlers.print());
  }

  @Test
  public void newMeasureSummary() throws Exception  {
    given(this.measureRepository.findFirstByUserIdAndTaskId("001", "stateandbehavior.Account"))
      .willReturn(
        measureSummary
      );
    this.mockMvc.perform(post("/api/"))
      .andExpect(status().isOk())
      .andExpect(content().json("{\"id\":null,\"userId\":\"001\",\"taskName\":\"Account-oppgave\",\"taskId\":\"stateandbehavior.Account\",\"measures\":[{\"measureProvider\":\"org.metrics.cyclomatic\",\"specificMeasures\":[{\"name\":\"cyclomatic Complexity\",\"value\":7.0}]},{\"measureProvider\":\"no.hal.javalang\",\"specificMeasures\":[{\"name\":\"for\",\"value\":7.0},{\"name\":\"foreach\",\"value\":4.0},{\"name\":\"while\",\"value\":1.0}]}],\"solutionManual\":false}"))
      .andDo(MockMvcResultHandlers.print());
  }


  @Test
  public void newSolutionMeasureSummary() throws Exception {
    // OMG the POST method expect a valid object in the post request! How can you be so stupid moe?
    ObjectNode res = mapper.createObjectNode();
    res.put("measureSummaryRef", measureSummary.getId());
    res.put("measureSummary", measureSummary.toString());
    res.put("status", "2000");
    given(this.metricsController.saveMeasureSummary(measureSummary))
     .willReturn(
      res
     );
    this.mockMvc.perform(post("/api/"))
      .andExpect(status().isOk())
     .andExpect(content().json(":\"foreach\",\"value\":4.0},{\"name\":\"while\",\"value\":1.0}]}],\"solutionManual\":false}"))
     .andDo(MockMvcResultHandlers.print());
  }


  @Test
  public void deleteMeasureSummary() throws Exception {
  }

  @Test
  public void deleteMeasureSummary1() throws Exception {
  }
}
