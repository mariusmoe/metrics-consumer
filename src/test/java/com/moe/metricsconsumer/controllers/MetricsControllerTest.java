package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.apiErrorHandling.CouldNotSaveException;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.repositories.*;
import com.moe.metricsconsumer.util.TestUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

  /**
   * Create some objects the will be used in the following tests
   * @throws Exception
   */
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
    measureSummaryList.add(new MeasureSummary("001","Account-oppgave", "stateandbehavior.Account", someMeasure, Arrays.asList("test1", "test2")));

    measureSummary = new MeasureSummary("001",
      "Account-oppgave",
      "stateandbehavior.Account",
      someMeasure, Arrays.asList("test1", "test2"));
    measureSummarySolution = new MeasureSummary("001", true,"Account-oppgave", "stateandbehavior.Account", someMeasure, Arrays.asList("test1", "test2"));


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
        Optional.of(measureSummary)
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
        Optional.of(measureSummary)
      );
    this.mockMvc.perform(post("/api/")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(measureSummary))
    )
      .andExpect(status().isOk())
      .andExpect(content().json("{\"measureSummaryRef\":\"lyHhaVtqDwXCI5Yo2by/Cf+YxKF1+go1/OM5J7PvC7s=\",\"measureSummary\":\"MeasureSummary(id=lyHhaVtqDwXCI5Yo2by/Cf+YxKF1+go1/OM5J7PvC7s=, userId=001, isSolutionManual=false, taskName=Account-oppgave, taskId=stateandbehavior.Account, measures=[Measure{measureProvider='org.metrics.cyclomatic', specificMeasures=[SpecificMeasure{name='cyclomatic Complexity', value=7.0}]}, Measure{measureProvider='no.hal.javalang', specificMeasures=[SpecificMeasure{name='for', value=7.0}, SpecificMeasure{name='foreach', value=4.0}, SpecificMeasure{name='while', value=1.0}]}])\",\"status\":\"2000\"}"))
      .andDo(MockMvcResultHandlers.print());
  }


  @Test
  public void newSolutionMeasureSummary() throws Exception, CouldNotSaveException {
    // OMG the POST method expect a valid object in the post request! How can you be so stupid moe?
    ObjectNode res = mapper.createObjectNode();
    res.put("measureSummaryRef", measureSummary.getId());
    res.put("measureSummary", measureSummary.toString());
    res.put("status", "2000");
    given(this.metricsController.saveMeasureSummary(measureSummary, null))
     .willReturn(
      res
     );
    this.mockMvc.perform(post("/api/")
      .contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(measureSummarySolution))
    )
      .andExpect(status().isOk())
      .andExpect(content().json("{\"measureSummaryRef\":\"lyHhaVtqDwXCI5Yo2by/Cf+YxKF1+go1/OM5J7PvC7s=\",\"measureSummary\":\"MeasureSummary(id=lyHhaVtqDwXCI5Yo2by/Cf+YxKF1+go1/OM5J7PvC7s=, userId=001, isSolutionManual=true, taskName=Account-oppgave, taskId=stateandbehavior.Account, measures=[Measure{measureProvider='org.metrics.cyclomatic', specificMeasures=[SpecificMeasure{name='cyclomatic Complexity', value=7.0}]}, Measure{measureProvider='no.hal.javalang', specificMeasures=[SpecificMeasure{name='for', value=7.0}, SpecificMeasure{name='foreach', value=4.0}, SpecificMeasure{name='while', value=1.0}]}])\",\"status\":\"2000\"}"))
      .andDo(MockMvcResultHandlers.print());
  }

  /*@Test
  public void getAllAchievements() throws Exception {
    String taskIdRefAccount = "stateandbehavior.Account";
    Achievement achievement1 = new Achievement(200,
      false,
      taskIdRefAccount,
      "for * 3",
      "For expert For for",
      "Award for exceptional work on for"
    );

    Achievement achievement2 = new Achievement(600,
      false,
      taskIdRefAccount,
      "while * 5",
      "While expert super",
      "Award for exceptional work on while nr1"
    );

    Achievement achievement3 = new Achievement(1200,
      false,
      taskIdRefAccount,
      "while * 1",
      "While expert easy",
      "Award for exceptional work on while next"
    );
    List<Achievement> achievements = new ArrayList<>();
    achievements.add(achievement1);
    achievements.add(achievement2);
    achievements.add(achievement3);

    given(this.achievementRepository.findAll())
      .willReturn(
        achievements
      );
    this.mockMvc.perform(get("/api/achievements"))
      .andExpect(status().isOk())
      .andExpect(content().json("[{\"id\":null,\"threshold\":200,\"taskIdRef\":\"stateandbehavior.Account\",\"expression\":\"for * 3\",\"name\":\"For expert For for\",\"description\":\"Award for exceptional work on for\",\"cumulative\":false},{\"id\":null,\"threshold\":600,\"taskIdRef\":\"stateandbehavior.Account\",\"expression\":\"while * 5\",\"name\":\"While expert super\",\"description\":\"Award for exceptional work on while nr1\",\"cumulative\":false},{\"id\":null,\"threshold\":1200,\"taskIdRef\":\"stateandbehavior.Account\",\"expression\":\"while * 1\",\"name\":\"While expert easy\",\"description\":\"Award for exceptional work on while next\",\"cumulative\":false}]"))
      .andDo(MockMvcResultHandlers.print());
  }
*/

  @Test
  public void deleteMeasureSummary() throws Exception {
  }

  @Test
  public void deleteMeasureSummary1() throws Exception {
  }
}
