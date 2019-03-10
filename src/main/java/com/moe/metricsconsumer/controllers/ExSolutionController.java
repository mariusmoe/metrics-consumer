package com.moe.metricsconsumer.controllers;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.ExSolution;
import com.moe.metricsconsumer.repositories.ExSolutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class hold logic for adding exSolutions
 */
@RequestMapping("/api/sol")
@Controller
public class ExSolutionController {


  @Autowired
  private ExSolutionRepository exSolutionRepository;

  ControllerUtil controllerUtil = new ControllerUtil();

  Logger logger = LoggerFactory.getLogger(ExSolutionController.class);

  /**
   * Find all exSolutions
   * @return all exSolutions
   */
  @GetMapping("/")
  @ResponseBody
  public List<ExSolution> getAllExSolutions() {
    return exSolutionRepository.findAll();
  }

  /**
   * Delete all exSolutions
   * @return response body with delete status message
   */
  @GetMapping("/delete")
  @ResponseBody
  public ResponseEntity deleteAllExSolutions() {
    exSolutionRepository.deleteAll();
    return new ResponseEntity<>(controllerUtil.jsonResponse(
      2000,
      "All exSolutions deleted"
    ), HttpStatus.OK);
  }

//  /**
//   * Add a new exSolution
//   * @param uploadingFiles  Source files for this task
//   * @param exTitle the ExercisePart title
//   * @param exClassName the classname for the ExercisePart
//   * @return
//   * @throws IOException
//   */
//  @PostMapping("/")
//  @ResponseBody
//  public ResponseEntity newExSolution(@NonNull @RequestParam("files") MultipartFile uploadingFile,
//                                      @NonNull @RequestParam("exTitle") String exTitle,
//                                      @NonNull @RequestParam("exClassName") String exClassName ) throws IOException {
//
//    String fileAsString = null;
//      if (!uploadingFile.isEmpty()) {
//       byte[] bytes = uploadingFile.getBytes();
//       fileAsString = new String(bytes);
//      }
//    }
//
//  private String exClassName;
//  ExSolution savedSolution = exSolutionRepository.save(new ExSolution(exClassName,exTitle , 1, fileAsString));
//    return new ResponseEntity<>(controllerUtil.jsonResponse(
//      2000,
//      "Saved exSolution",
//      savedSolution), HttpStatus.OK);
//  }




}
