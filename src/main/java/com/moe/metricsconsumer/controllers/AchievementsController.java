package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.apiErrorHandling.CouldNotSaveException;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.models.rewards.UserAchievement;
import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.UserAchievementRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Should handle everything related to achievements
 */
@RequestMapping("/api/achievement")
@Controller
public class AchievementsController {

  @Autowired
  private UserAchievementRepository userAchievementRepository;

  @Autowired
  private AchievementRepository achievementRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  ControllerUtil controllerUtil = new ControllerUtil();

  @Autowired
  ObjectMapper mapper;

  @GetMapping("/user")
  @ResponseBody
  public List<UserAchievement> getAllAchievedAchievements() {
    return this.userAchievementRepository.findAllByUserRef("001");
  }


  @GetMapping("/")
  @ResponseBody
  public List<Achievement> getAllAchievements() {
    return this.achievementRepository.findAllWithoutBinary();
  }


  @GetMapping("/with-binary")
  @ResponseBody
  public List<Achievement> getAllAchievementsWithBinary() {
    return this.achievementRepository.findAll();
  }


  @PostMapping("/")
  @ResponseBody
  public Achievement newAchievement(@Valid @RequestBody Achievement newAchievement ){
    System.out.println("Try to add new achievement!   " + newAchievement.toString());
    return achievementRepository.save(newAchievement);
  }


  @PostMapping("/file")
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
      // TODO: The ordering of the two files are not guaranteed
      achievement.setExpression( new Binary(BsonBinarySubType.BINARY, uploadingFiles[0].getBytes()));
      achievement.setDummyData( new Binary(BsonBinarySubType.BINARY, uploadingFiles[1].getBytes()));
      // TODO: Store image file somewhere and add image path
      mongoTemplate.save(achievement);
      System.out.println("achievementDocument: " + achievement);
    } catch (Exception e) {
      throw new CouldNotSaveException(achievement.getClass(), achievement.toString());
    }

    return controllerUtil.jsonResponse(2000, "Success - document has been saved");
  }

}
