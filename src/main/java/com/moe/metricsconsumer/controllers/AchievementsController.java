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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  Logger logger = LoggerFactory.getLogger(AchievementsController.class);

  @Autowired
  ObjectMapper mapper;

  @Value("${mom.staticResource}")
  private String staticPath;

  @GetMapping("/user")
  @ResponseBody
  public List<UserAchievement> getAllAchievedAchievements(Principal principal) {
    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    return this.userAchievementRepository.findAllByUserRef(principalMap.get("userid"));
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
    logger.debug("Try to add new achievement!   " + newAchievement.toString());
    return achievementRepository.save(newAchievement);
  }


  /**
   * Save achievement files -> config, data, and image
   * @param achievementId reference to the achivement metadata object
   * @param uploadingFiles  the files to upload
   * @return a success status on file save success
   * @throws CouldNotSaveException
   */
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
//      for loop over uploaded files
//      (?i)data, (?i)config regex and png/jpg/jpeg file ending
      Pattern dataPattern = Pattern.compile("(?i)data");
      Pattern configPattern = Pattern.compile("(?i)config");

      for (MultipartFile uploadingFile: uploadingFiles) {
        Matcher dataMatcher = dataPattern.matcher(uploadingFile.getOriginalFilename());
        Matcher configMatcher = configPattern.matcher(uploadingFile.getOriginalFilename());
        if (configMatcher.find()){
          // The filename contains config
          achievement.setExpression( new Binary(BsonBinarySubType.BINARY, uploadingFile.getBytes()));
          logger.info("Found CONFIG xmi file - saving");
        }
        if (dataMatcher.find()){
          // The filename contains data
          achievement.setDummyData( new Binary(BsonBinarySubType.BINARY, uploadingFile.getBytes()));
          logger.info("Found DATA xmi file - saving");
        }

        if (uploadingFile.getContentType().split("/")[0].equals("image")) {
          saveAchievementImage(achievement, uploadingFile);
          logger.info("Saved image");
        }
      }
      mongoTemplate.save(achievement);
      logger.debug("achievementDocument: " + achievement + "\nSave success");
    } catch (Exception e) {
      throw new CouldNotSaveException(achievement.getClass(), achievement.toString());
    }

    return controllerUtil.jsonResponse(2000, "Success - document has been saved");
  }

  private void saveAchievementImage(Achievement achievement, MultipartFile uploadingFile) {
    // the uploaded MIME type is an image
    // save the image with; static path, the document id, and file extension
    String filename = achievement.getId()+ "." + uploadingFile.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1");
    File file = new File(this.staticPath + filename);
    logger.info("Filename: " + filename);
    try (InputStream inputStream = uploadingFile.getInputStream()) {
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream outputStream = new FileOutputStream(file);
      int read = 0;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file " + filename, e);
    }
  }

}
