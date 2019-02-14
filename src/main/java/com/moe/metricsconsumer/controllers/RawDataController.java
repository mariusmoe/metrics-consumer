package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.ExerciseDocument;

import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.ExerciseDocumentRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import com.moe.metricsconsumer.repositories.XmlRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Should handle everything related to storing student raw data
 */
@RequestMapping("/api/xml")
@Controller
public class RawDataController {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  XmlRepository xmlRepository;

  @Autowired
  private MeasureRepository measureRepository;

  @Autowired
  private AchievementRepository achievementRepository;

  @Autowired
    private ExerciseDocumentRepository exerciseDocumentRepository;

  ControllerUtil controllerUtil = new ControllerUtil();

  Logger logger = LoggerFactory.getLogger(RawDataController.class);

  private static final String approvedContentType = "text/xml";



  // Possible extensions - print a warning if some metrics has not been calculated
  // Flow of information - see activity diagram -> need to be sent with a code
  @PostMapping("/")
  @ResponseBody
  public ObjectNode newStudentCode(@Nullable @RequestHeader(value="measureSummaryRef") String measureSummaryRef,
                                   @RequestParam("files") MultipartFile[] uploadingFiles){
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }


  @PostMapping("/solution")
  @ResponseBody
  public ObjectNode newSolutionCode(@RequestHeader(value="measureSummaryRef") String measureSummaryRef,
                                    @RequestParam("files") MultipartFile[] uploadingFiles){
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }

  private ObjectNode saveExFiles(String measureSummaryRefParam, MultipartFile[] uploadingFiles) {
    logger.info("saveExFiles called initiating savings...");
    String measureSummaryRef = measureSummaryRefParam;
    if (measureSummaryRef == null) {
      MeasureSummary measureSummary = new MeasureSummary();
      // TODO: calculate measureSummary from MultipartFile[]
      // call calculateMeasureSummaryFromExFiles(uploadingFiles, null)
      MeasureSummary savedMeasureSummary = this.measureRepository.save(measureSummary);
      measureSummaryRef = savedMeasureSummary.getId();
    } else {
      // TODO: recalculate measure summary from MultipartFile[]
      // call calculateMeasureSummaryFromExFiles(uploadingFiles, measureSummaryRef)
      try {
        xmlRepository.removeByMeasureSummaryRef(measureSummaryRef);
      } catch (Exception e) {
        logger.error(e.toString());
      }
    }

    logger.debug("measureSummaryRef: " + measureSummaryRef);


    List<ExerciseDocument> exerciseDocumentList = new ArrayList<>();
    for(MultipartFile uploadedFile : uploadingFiles) {
      logger.info("Trying to save");
      try {
//        if (uploadedFile.getContentType() != approvedContentType) {
//          logger.warn("NOT an XML file - aborting...");
//          throw new Exception("Not an XML file");
//        }
        ExerciseDocument exerciseDocument = new ExerciseDocument(
          measureSummaryRef,
          "ex",
          uploadedFile.getOriginalFilename(),
          new Binary(BsonBinarySubType.BINARY, uploadedFile.getBytes())
        );
        exerciseDocumentList.add(exerciseDocument);
        logger.debug(exerciseDocument.toString() + " - ContentType: " +uploadedFile.getContentType());
      } catch (Exception e) {
        logger.warn("ERROR: "+ e.toString());
        e.printStackTrace();
        return controllerUtil.jsonResponse(4000, "ERROR" + e.toString());
      }
      exerciseDocumentRepository.saveAll(exerciseDocumentList);
    }

    return controllerUtil.jsonResponse(2000, "Success - document has been saved");
  }


}
