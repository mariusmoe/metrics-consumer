package com.moe.metricsconsumer.controllers;

import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;

import com.moe.metricsconsumer.models.FvConfiguration;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.repositories.FvConfigurationRepository;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import no.hal.learning.fv.ExpressionFeatures;
import no.hal.learning.fv.FeatureList;
import no.hal.learning.fv.FeatureValued;
import no.hal.learning.fv.FvFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.Principal;
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


  @GetMapping("/{taskId}")
  @ResponseBody
  public FeatureList getOneMeasureFv(@NonNull @PathVariable("taskId") String taskId) throws EntityNotFoundException {
    return getOneGenericMeasureFv(taskId, false, "001");
  }

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


    // Based on wheter this is a solution -> get the correct metrics
    if (isSolution) {
      Query query = new Query();
      query.addCriteria(Criteria.where("isSolutionManual").is(true));
      query.addCriteria(Criteria.where("taskId").is(taskId));
      measureSummary = this.mongoTemplate.findOne(query, MeasureSummary.class);
    } else {
      measureSummary = this.measureRepository.findFirstByUserIdAndTaskId("001", taskId);
    }

    // Check if measureSummary was found -> return error if not found
    if (measureSummary == null) {
      throw new EntityNotFoundException(MeasureSummary.class, taskId);

    }

    // Add the meassureSummary to the featureList object
    for (Measure measure : measureSummary.getMeasures()) {
      for (SpecificMeasure specificMeasure : measure.getSpecificMeasures()) {
        featureList.getFeatures().put(measure.getMeasureProvider()
          .replace(".", "_") + "__" + specificMeasure.getName()
          .replace(".", "_"), (double) specificMeasure.getValue());
      }
    }

    // TODO: Retreive this config based on task
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

    // Replace all references to 'other' in config.xmi with 'featureList'
    for (EObject eObject : resource.getContents()) {

      replaceReference(eObject);

      Iterator<EObject> iterator = eObject.eAllContents();
      while (iterator.hasNext()) {
        replaceReference(iterator.next());
      }

      // Legacy code -> too be deprecated
      if (eObject instanceof FeatureValued) {
        for (EStructuralFeature eStructuralFeature : eObject.eClass().getEAllStructuralFeatures()) {
          if (eStructuralFeature.getName().equals("other")) {
            eObject.eSet(eStructuralFeature, featureList);
          }
        }
      }
    }


    // Legacy code -> too be deprecated
    // the calculatedFeatureList that shall be returned to the requester
    FeatureList calculatedFeatureList = FvFactory.eINSTANCE.createFeatureList();

    for (EObject eObject : resource.getContents()) {
      if (eObject instanceof FeatureValued) {
        calculatedFeatureList = ((FeatureValued) eObject).toFeatureList();
      }
    }

    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
    System.out.println("Old way of only replacing 'other' references");
    System.out.println(calculatedFeatureList);
    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");

    // link config to real data, i.e. featureList
    //
    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(featureList);


    FvConfiguration fvConfiguration = fvConfigurationRepository.findFirstByTaskId(taskId);
    for (Map.Entry<String, String> entry : fvConfiguration.getExpressionFeature().entrySet()) {
      expressionFeatures.getFeatures().put(entry.getKey(), entry.getValue());
    }

    FeatureList finalFeatureList = FvFactory.eINSTANCE.createFeatureList();

    for (String featureName : expressionFeatures.getFeatureNames()) {
      finalFeatureList.getFeatures().put(featureName, expressionFeatures.getFeatureValue(featureName));
    }

    return finalFeatureList;
  }

  /**
   * Find the reference or the references that is not the same as the eObject's eResource and replace them
   * (the last part is NYI)
   *
   * @param eObject   The eObject to replace references in
   */
  private void replaceReference(EObject eObject) {
    for (EReference eStructuralFeature : eObject.eClass().getEAllReferences()) {
      // Skip if its a container, isContainment, or is not changeable
      if (eStructuralFeature.isContainer() || eStructuralFeature.isContainment() || !eStructuralFeature.isChangeable()) {
        continue;
      }
      // If there is more than one eStructuralFeature
      if (eStructuralFeature.isMany()) {
        EList<? extends EObject> references = (EList<? extends EObject>) eObject.eGet(eStructuralFeature);
        for (int i = 0; i < references.size(); i++) {
          EObject referenced = references.get(i);
          if (referenced.eResource() != eObject.eResource()) {
            System.out.println(eStructuralFeature.getName() + " -> " + eObject);
            // TODO: replace the reference
            // See below... same problem
          }
        }
      } else {
        EObject referenced = (EObject) eObject.eGet(eStructuralFeature);
        if (referenced.eResource() != eObject.eResource()) {
          System.out.println(eStructuralFeature.getName() + " -> " + eObject);
          // TODO: replace the reference
          // Problem -> we don't know that the reference to replece to is featureList (even thou this is most likely
          // the most common scenario)
          // could be solved by adding an ID in feature valued
//          eObject.eSet(eStructuralFeature, featureList);
        }
      }
      if (eStructuralFeature.getName().equals("other")) {
        System.out.println("It was an 'other' reference");
      }
    }
  }


}
