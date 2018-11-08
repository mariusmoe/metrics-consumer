package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.ExerciseDocument;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;

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
    ObjectNode res;

    // TODO: lookup all ex files for this task with 'measureSummaryRef' and delete them.
    // Reason we only want to keep one copy of the ex file.
    // See: https://stackoverflow.com/a/33164008/5489113
    // for how to delete multiple documents in an efficient manner


    try {
      xmlRepository.removeByMeasureSummaryRef(measureSummaryRef);
    } catch (Exception e) {}

    for(MultipartFile uploadedFile : uploadingFiles) {
      try {
        ExerciseDocument exerciseDocument = new ExerciseDocument();
        exerciseDocument.setMeasureSummaryRef(measureSummaryRef);
        exerciseDocument.setDocType("ex");
        exerciseDocument.setFile(new Binary(BsonBinarySubType.BINARY, uploadedFile.getBytes()));
        mongoTemplate.save(exerciseDocument);
        System.out.println(exerciseDocument);
      } catch (Exception e) {
        e.printStackTrace();
        res = mapper.createObjectNode();
        res.put("message", "ERROR: " + e.toString());
        res.put("status", "4000");
        return res;
      }
    }

    // TODO check for rewards!

    res = mapper.createObjectNode();
    res.put("message", "Success - document has been saved" );
    res.put("status", "2000");

    return res;
  }
}
