package com.moe.metricsconsumer.controllers;

import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;

import com.moe.metricsconsumer.models.FvConfiguration;
import com.moe.metricsconsumer.models.FvResponse;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.repositories.FvConfigurationRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import no.hal.learning.fv.*;
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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;


@RequestMapping("/api/fv")
@Controller
public class MetricsControllerFv {

  @Autowired
  private MeasureRepository measureRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private FvConfigurationRepository fvConfigRepository;

  ControllerUtil controllerUtil = new ControllerUtil();

  Logger logger = LoggerFactory.getLogger(FvConfigurationRepository.class);


  /**
   * Get the feature vector for the currently logged in user for the provided task
   * @param taskId  the task one want to get the fv for
   * @return
   * @throws EntityNotFoundException
   */
  @GetMapping("/{taskId}")
  @ResponseBody
  public FvResponse getOneMeasureFv(@NonNull @PathVariable("taskId") String taskId, Principal principal) throws EntityNotFoundException {

    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    return getOneGenericMeasureFv(taskId, false, principalMap.get("userid"));
  }

  /**
   * Retrieve the feature value for the solution of the provided task
   * @param taskId  the task one want to get the solution for
   * @return
   * @throws EntityNotFoundException
   */
  @GetMapping("/solution/{taskId}")
  @ResponseBody
  public FvResponse getOneSolutionMeasureFv(@NonNull @PathVariable("taskId") String taskId, Principal principal) throws EntityNotFoundException {

    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    return getOneGenericMeasureFv(taskId, true, principalMap.get("userid"));
  }

  /**
   * Will create a fv based on the fvConfig for the requested task
   *
   * @param taskId     id of task
   * @param isSolution is this the solution manual fv
   * @param userId     the user to retreive the fv for
   * @return a feature list
   * @throws EntityNotFoundException
   */
  private FvResponse getOneGenericMeasureFv(String taskId, boolean isSolution, String... userId) throws EntityNotFoundException {

    // The userId is optional for now
    String _userId = (userId.length >= 1) ? userId[0] : "";
    logger.info("--------- UserID: " + _userId);

    MeasureSummary measureSummary;

    // Based on whether this is a solution -> get the correct metrics
    if (isSolution) {
      measureSummary = this.measureRepository.findFirstByUserIdAndTaskIdAndIsSolutionManual(_userId, taskId, true)
        .orElseThrow(() -> new EntityNotFoundException(MeasureSummary.class, taskId));
      logger.debug(measureSummary.toString());
    } else {
      measureSummary = this.measureRepository.findFirstByUserIdAndTaskIdAndIsSolutionManual(_userId, taskId, false)
        .orElseThrow(() -> new EntityNotFoundException(MeasureSummary.class, taskId));
    }

    FeatureValuedContainer featureValuedContainer = controllerUtil.createContainerFromMeasures(measureSummary);

    FvConfiguration fvConfig = this.fvConfigRepository.findFirstByTaskId(taskId).orElseThrow(() ->
      new EntityNotFoundException(FvConfiguration.class, taskId));

    Resource resource = getFvResource(fvConfig);

    FeatureList calculatedFeatureList = FvFactory.eINSTANCE.createFeatureList();
    // Replace all references to 'other' in config.xmi with 'featureList'
    // Not all measures will be calculated if they are not found! (I think)
    for (EObject eObject : resource.getContents()) {
      eObject = controllerUtil.replaceReference(eObject, featureValuedContainer);

      // CAn the error be caught? and then patch itself and try again?
      calculatedFeatureList = ((FeatureValued) eObject).toFeatureList();
    }
    return new FvResponse(measureSummary,calculatedFeatureList);
  }


  /**
   * Get the config as a resource for the provided measureSummary
   * @param fvConfiguration to extract the expression from
   * @return Resource resource for the expression of the achievement
   */
  private Resource getFvResource(FvConfiguration fvConfiguration) {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    // load config from XMI
    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    try {
      configResource.load(new ByteArrayInputStream(fvConfiguration.getExpressionFeature().getData()),null );
      dataResource.load(new ByteArrayInputStream(fvConfiguration.getDataFeature().getData()), null );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return configResource;
  }

}
