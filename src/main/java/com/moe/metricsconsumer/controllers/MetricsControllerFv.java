package com.moe.metricsconsumer.controllers;

import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;

import com.moe.metricsconsumer.models.FvConfiguration;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.repositories.FvConfigurationRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import no.hal.learning.fv.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.*;


@RequestMapping("/api/fv")
@Controller
public class MetricsControllerFv {

  @Autowired
  private MeasureRepository measureRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private FvConfigurationRepository fvConfigurationRepository;

  ControllerUtil controllerUtil = new ControllerUtil();


  /**
   * Get the feature vector for the currently logged in user for the provided task
   * @param taskId  the task one want to get the fv for
   * @return
   * @throws EntityNotFoundException
   */
  @GetMapping("/{taskId}")
  @ResponseBody
  public FeatureList getOneMeasureFv(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    return getOneGenericMeasureFv(taskId, false, "001");
  }

  /**
   * Retrieve the feature value for the solution of the provided task
   * @param taskId  the task one want to get the solution for
   * @return
   * @throws EntityNotFoundException
   */
  @GetMapping("/solution/{taskId}")
  @ResponseBody
  public FeatureList getOneSolutionMeasureFv(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    return getOneGenericMeasureFv(taskId, true);
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
  private FeatureList getOneGenericMeasureFv(String taskId, boolean isSolution, String... userId) throws EntityNotFoundException {

    // TODO: set userId based on the principal
    // The userId is optional for now
    String _userId = (userId.length >= 1) ? userId[0] : "";

    // featureList is the featureList of the student or solution
    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    MeasureSummary measureSummary;


    // Based on whether this is a solution -> get the correct metrics
    if (isSolution) {
      Query query = new Query();
      query.addCriteria(Criteria.where("isSolutionManual").is(true));
      query.addCriteria(Criteria.where("taskId").is(taskId));
      measureSummary = this.mongoTemplate.findOne(query, MeasureSummary.class);
      measureSummary = this.measureRepository.getFirstByIsSolutionManualAndTaskId(true,taskId);
    } else {
      measureSummary = this.measureRepository.findFirstByUserIdAndTaskId("001", taskId);
    }

    // Check if measureSummary was found -> return error if not found
    if (measureSummary == null) {
      throw new EntityNotFoundException(MeasureSummary.class, taskId);

    }

    FeatureValuedContainer featureValuedContainer = controllerUtil.createContainerFromMeasures(measureSummary);

    // TODO: Retrieve this config based on task
    // Call class that create dummy fv configuration
    ConfigCreator configCreator = new ConfigCreator();
    try {
      configCreator.create();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Load config from file system
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();
    Resource resource = resSet.getResource(URI.createURI("config.xmi"), true);

    FeatureList calculatedFeatureList = FvFactory.eINSTANCE.createFeatureList();
    // Replace all references to 'other' in config.xmi with 'featureList'
    for (EObject eObject : resource.getContents()) {

      eObject = controllerUtil.replaceReference(eObject, featureValuedContainer);
      calculatedFeatureList = ((FeatureValued) eObject).toFeatureList();

      Iterator<EObject> iterator = eObject.eAllContents();
      System.out.println(eObject.eAllContents());
      while (iterator.hasNext()) {
        controllerUtil.replaceReference(iterator.next(), featureValuedContainer);
      }
    }

    return calculatedFeatureList;
  }

}
