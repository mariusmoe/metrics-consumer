package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Should handle everything related to storing student raw data
 */
@RequestMapping("/api/raw")
@Controller
public class RawDataController {

  @Autowired
  ObjectMapper mapper;


  // TODO: make this enpoint able to receive a .ex file and store it
  // Possible extensions - print a warning if some metrics has not been calculated
  // Flow of information - see activity diagram -> need to be sent with a code
  @PostMapping("/{{exToken}}")
  @ResponseBody
  public ObjectNode newStudentCode(@NonNull @PathVariable("exToken") String exToken, @Valid @RequestBody MeasureSummary newMeasureSummary){


    ObjectNode res = mapper.createObjectNode();
    res.put("message", "The raw ex file has been saved");
    res.put("status", "2000");

    return res;
  }
}
