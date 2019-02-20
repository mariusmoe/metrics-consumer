package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moe.metricsconsumer.models.ExerciseDocument;

import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.repositories.AchievementRepository;
import com.moe.metricsconsumer.repositories.ExerciseDocumentRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import com.moe.metricsconsumer.repositories.XmlRepository;
import no.hal.learning.exercise.AbstractStringEdit;
import no.hal.learning.exercise.Exercise;
import no.hal.learning.exercise.ExercisePackage;
import no.hal.learning.exercise.ExerciseProposals;
import no.hal.learning.exercise.jdt.JdtPackage;
import no.hal.learning.exercise.jdt.JdtSourceEditEvent;
import no.hal.learning.exercise.jdt.JdtSourceEditProposal;
import no.hal.learning.exercise.junit.JunitPackage;
import no.hal.learning.exercise.workbench.WorkbenchPackage;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

  private static final String approvedContentType = "application/octet-stream";



  // Possible extensions - print a warning if some metrics has not been calculated
  // Flow of information - see activity diagram -> need to be sent with a code
  @PostMapping("/")
  @ResponseBody
  public ResponseEntity<ObjectNode> newStudentCode(@Nullable @RequestHeader(value="measureSummaryRef") String measureSummaryRef,
                                       @RequestParam("files") MultipartFile[] uploadingFiles) {
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }


  @PostMapping("/solution")
  @ResponseBody
  public ResponseEntity newSolutionCode(@RequestHeader(value="measureSummaryRef") String measureSummaryRef,
                                        @RequestParam("files") MultipartFile[] uploadingFiles) {
    return saveExFiles(measureSummaryRef, uploadingFiles);
  }

  private ResponseEntity<ObjectNode> saveExFiles(String measureSummaryRefParam, MultipartFile[] uploadingFiles) {
    logger.info("saveExFiles called initiating savings...");
    String measureSummaryRef = measureSummaryRefParam;
    if (measureSummaryRef == null) {
      // This is a new exercise being added
      MeasureSummary measureSummary = new MeasureSummary();
      // TODO: calculate measureSummary from MultipartFile[]
      // call calculateMeasureSummaryFromExFiles(uploadingFiles, null)
      MeasureSummary savedMeasureSummary = calculateMeasureSummaryFromExFiles(uploadingFiles, null);
      this.measureRepository.save(measureSummary);
      measureSummaryRef = savedMeasureSummary.getId();
    } else {
      // TODO: recalculate measure summary from MultipartFile[]
      MeasureSummary recalculatedMeasureSummary = calculateMeasureSummaryFromExFiles(uploadingFiles, measureSummaryRef);
      this.measureRepository.save(recalculatedMeasureSummary);
      try {
        xmlRepository.removeByMeasureSummaryRef(measureSummaryRef);
      } catch (Exception e) {
        logger.error(e.toString());
      }
    }
    logger.debug("measureSummaryRef: " + measureSummaryRef);

    // Start saving files in DB
    List<ExerciseDocument> exerciseDocumentList = new ArrayList<>();
    for(MultipartFile uploadedFile : uploadingFiles) {
      logger.info("Trying to save: " + uploadedFile.getContentType());

        if (!uploadedFile.getContentType().equals( approvedContentType)) {
          logger.warn("NOT an XML file - aborting...");
          return new ResponseEntity<>(controllerUtil.jsonResponse(4000, "Failure - wrong media"),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
      ExerciseDocument exerciseDocument = null;
      try {
        exerciseDocument = new ExerciseDocument(
          measureSummaryRef,
          "ex",
          uploadedFile.getOriginalFilename(),
          new Binary(BsonBinarySubType.BINARY, uploadedFile.getBytes())
        );
      } catch (IOException e) {
        e.printStackTrace();
        return new ResponseEntity<>(controllerUtil.jsonResponse(4000, "Failure - cant read bytes"),
          HttpStatus.NOT_ACCEPTABLE);
      }
      exerciseDocumentList.add(exerciseDocument);
        logger.debug(exerciseDocument.toString() + " - ContentType: " +uploadedFile.getContentType());

      exerciseDocumentRepository.saveAll(exerciseDocumentList);
    }

//    controllerUtil.jsonResponse(2000, "Success - document has been saved");
    return new ResponseEntity<>( controllerUtil.jsonResponse(2000, "Success - document has been saved"),
      HttpStatus.OK);
  }

  private MeasureSummary calculateMeasureSummaryFromExFiles(MultipartFile[] uploadingFiles, String measureSummaryRef) {

    // These packages are not yet in memory or loaded by a manifest file and has to be loaded in memory to be found
    // See: https://wiki.eclipse.org/EMF/FAQ#I_get_a_PackageNotFoundException:_e.g..2C_.22Package_with_uri_.27http:.2F.2Fcom.example.company.ecore.27_not_found..22_What_do_I_need_to_do.3F
    ExercisePackage exercisePackage = ExercisePackage.eINSTANCE;
    JdtPackage jdtPackage = JdtPackage.eINSTANCE;
    JunitPackage junitPackage = JunitPackage.eINSTANCE;
    WorkbenchPackage workbenchPackage = WorkbenchPackage.eINSTANCE;


    for(MultipartFile uploadedFile : uploadingFiles) {
      Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ex", new XMIResourceFactoryImpl());
      ResourceSet resSet = new ResourceSetImpl();

      // load config from XMI
      Resource exFileResource = resSet.createResource(URI.createURI("exFileResource.ex"));
      try {
        exFileResource.load(new ByteArrayInputStream(uploadedFile.getBytes()),null );
//        resSet.getResource(URI.createURI("TwitterComparison.ex"), true );
      } catch (Exception e) {
        e.printStackTrace();
      }


//      Kun en tanke -> hva med å traversere exFileResource som et tre og heller ta aksjon hvis objektet er av typen
//      JdtSourceEditProposal, da skal en hente attempts og gjøre getString() på siste forsøk.
//      ?

      // can exFileResource be an instance of Exercise? or
      Exercise exercise;
      ExerciseProposals exerciseProposals;
      for (EObject eObject : exFileResource.getContents()) {
        if (eObject instanceof ExerciseProposals) {
          exerciseProposals = (ExerciseProposals) eObject;
          logger.info("Title: " + exerciseProposals.getExercise().getTitle());
          // Can something more specific than EObject be used in the following 'for loop'
          for (EObject eObject1:  exerciseProposals.getAllProposals()){
            // Is it important which stringEdit is used? The last?
            if (eObject1 instanceof JdtSourceEditProposal) {
              JdtSourceEditProposal jdtSourceEditProposal = (JdtSourceEditProposal) eObject1;
              for (EObject eObject2: jdtSourceEditProposal.getAttempts()){
                if (eObject2 instanceof JdtSourceEditEvent) {
                  JdtSourceEditEvent jdtSourceEditEvent = (JdtSourceEditEvent) eObject2;
                  String s = jdtSourceEditEvent.getEdit().getString();
                  String s2 = jdtSourceEditEvent.getEdit().getString();
                }
              }

//              jdtSourceEditProposal.getAnswer()
              // Get the exercise source code as a string
              String s = ((AbstractStringEdit) eObject1).getString();
              // TODO: generate java AST
              // TODO: pass to an IMetricProvider

            }
          }
        }
      }
      // TODO: create AST
      // Give AST to all IMetricProviders
      // gather results in one MeasureSummary object and return this.

    }

    return null;
  }


}
