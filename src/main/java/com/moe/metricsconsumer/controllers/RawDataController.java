package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.ExerciseDocument;

import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.ExerciseDocumentRepository;
import com.moe.metricsconsumer.repositories.XmlRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
  private AchievementRepository achievementRepository;

  @Autowired
    private ExerciseDocumentRepository exerciseDocumentRepository;

  ControllerUtil controllerUtil = new ControllerUtil();

  Logger logger = LoggerFactory.getLogger(RawDataController.class);


  // Possible extensions - print a warning if some metrics has not been calculated
  // Flow of information - see activity diagram -> need to be sent with a code
  @PostMapping("/")
  @ResponseBody
  public ObjectNode newStudentCode(@RequestHeader(value="measureSummaryRef") String measureSummaryRef,
                                   @RequestParam("uploadingFiles") MultipartFile[] uploadingFiles){
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }


  @PostMapping("/solution")
  @ResponseBody
  public ObjectNode newSolutionCode(@RequestHeader(value="measureSummaryRef") String measureSummaryRef,
                                    @RequestParam("uploadingFiles") MultipartFile[] uploadingFiles){
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }

  private ObjectNode saveExFiles(@NonNull String measureSummaryRef, MultipartFile[] uploadingFiles) {

    try {
      xmlRepository.removeByMeasureSummaryRef(measureSummaryRef);
    } catch (Exception e) {}

    for(MultipartFile uploadedFile : uploadingFiles) {
      // TODO: there might be more than one file!!! fix this for bulk upload
      try {
        ExerciseDocument exerciseDocument = new ExerciseDocument(
          measureSummaryRef,
          "ex",
          new Binary(BsonBinarySubType.BINARY, uploadedFile.getBytes())
        );
        exerciseDocumentRepository.save(exerciseDocument);
        logger.debug(exerciseDocument.toString());
      } catch (Exception e) {
        e.printStackTrace();
        return controllerUtil.jsonResponse(4000, "ERROR");
      }
    }

    return controllerUtil.jsonResponse(2000, "Success - document has been saved");
  }


}
