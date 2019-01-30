package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.apiErrorHandling.CouldNotSaveException;
import com.moe.metricsconsumer.models.ExerciseDocument;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;

import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.XmlRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;


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


  // Possible extensions - print a warning if some metrics has not been calculated
  // Flow of information - see activity diagram -> need to be sent with a code
  @PostMapping("/")
  @ResponseBody
  public ObjectNode newStudentCode(@RequestHeader(value="measureSummaryRef") String measureSummaryRef, @RequestParam("uploadingFiles") MultipartFile[] uploadingFiles){
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }


  @PostMapping("/solution")
  @ResponseBody
  public ObjectNode newSolutionCode(@RequestHeader(value="measureSummaryRef") String measureSummaryRef, @RequestParam("uploadingFiles") MultipartFile[] uploadingFiles){
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }

  private ObjectNode saveExFiles(@NonNull String measureSummaryRef, MultipartFile[] uploadingFiles) {

    // TODO: lookup all ex files for this task with 'measureSummaryRef' and delete them.
    // Reason we only want to keep one copy of the ex file.
    // See: https://stackoverflow.com/a/33164008/5489113
    // for how to delete multiple documents in an efficient manner


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
        return returnSimpleError(e);
      }
    }

    ObjectNode res;
    // Check for rewards! its done on retrieval

    res = mapper.createObjectNode();
    res.put("message", "Success - document has been saved" );
    res.put("status", "2000");

    return res;
  }

  @PostMapping("/achievement")
  @ResponseBody
  public Achievement newAchievement(@Valid @RequestBody Achievement newAchievement ){
    return achievementRepository.save(newAchievement);
  }


  @PostMapping("/achievement/file")
  @ResponseBody
  public ObjectNode newAchievementFile(
    @RequestHeader(value="achievementId") String achievementId,
    @RequestParam("uploadingFiles") MultipartFile[] uploadingFiles) throws CouldNotSaveException {

    Achievement achievement = null;
    Optional<Achievement> optionalAchievement= achievementRepository.findById(achievementId);
    if(optionalAchievement.isPresent()){
      achievement = optionalAchievement.get();
    }

    try {
      achievement.setExpression( new Binary(BsonBinarySubType.BINARY, uploadingFiles[0].getBytes()));
      achievement.setDummyData( new Binary(BsonBinarySubType.BINARY, uploadingFiles[1].getBytes()));
      mongoTemplate.save(achievement);
      System.out.println("achievementDocument: " + achievement);
    } catch (Exception e) {
      throw new CouldNotSaveException(achievement.getClass(), achievement.toString());
    }

    
    ObjectNode res;
    res = mapper.createObjectNode();
    res.put("message", "Success - document has been saved" );
    res.put("status", "2000");

    return res;
  }

  private ObjectNode returnSimpleError(Exception e) {
    ObjectNode res;
    e.printStackTrace();
    res = mapper.createObjectNode();
    res.put("message", "ERROR: " + e.toString());
    res.put("status", "4000");
    return res;
  }
}
