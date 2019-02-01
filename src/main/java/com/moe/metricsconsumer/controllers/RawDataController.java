package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.ExerciseDocument;

import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.XmlRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
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

  ControllerUtil controllerUtil = new ControllerUtil();


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
      try {
        ExerciseDocument exerciseDocument = new ExerciseDocument(measureSummaryRef,
          "ex",
          new Binary(BsonBinarySubType.BINARY, uploadedFile.getBytes()) );
        mongoTemplate.save(exerciseDocument);
        System.out.println(exerciseDocument);
      } catch (Exception e) {
        e.printStackTrace();
        return controllerUtil.jsonResponse(4000, "ERROR");
      }
    }

    return controllerUtil.jsonResponse(2000, "Success - document has been saved");
  }


}
