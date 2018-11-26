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

    FeatureValuedContainer featureValuedContainer = FvFactory.eINSTANCE.createFeatureValuedContainer();

    // featureList is the featureList of the student or solution
    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    MeasureSummary measureSummary;


    // Based on whether this is a solution -> get the correct metrics
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



    // Add the meassureSummary to the featureValuedContainer object
    for (Measure measure : measureSummary.getMeasures()) {

      // Create the feature list
      FeatureList fvList = FvFactory.eINSTANCE.createFeatureList();

      // Add all the stuff in the featureList
      for (SpecificMeasure specificMeasure : measure.getSpecificMeasures()) {
        fvList.getFeatures().put(
          specificMeasure.getName().replace(".", "_"),
          (double) specificMeasure.getValue());
      }

      // Deprecated -> legacy code
      for (SpecificMeasure specificMeasure : measure.getSpecificMeasures()) {
        featureList.getFeatures().put(measure.getMeasureProvider()
          .replace(".", "_") + "__" + specificMeasure.getName()
          .replace(".", "_"), (double) specificMeasure.getValue());
      }

      // Add metadata to metaDataFeatureValued
      MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
      metaDataFeatureValued.setFeatureValuedId(measure.getMeasureProvider().replace(".", "_"));

      // Add featureList to the metadata
      metaDataFeatureValued.setDelegatedFeatureValued(fvList);

      // Add the metadata to the root container
      featureValuedContainer.getFeatureValuedGroups().add(metaDataFeatureValued);

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

      eObject = replaceReference(eObject, featureValuedContainer);

      Iterator<EObject> iterator = eObject.eAllContents();
      System.out.println(eObject.eAllContents());
      while (iterator.hasNext()) {
        replaceReference(iterator.next(), featureValuedContainer);
      }
    }

    FeatureList calculatedFeatureList = FvFactory.eINSTANCE.createFeatureList();

    for (EObject eObject : resource.getContents()) {
      System.out.println(eObject);
      if (eObject instanceof FeatureValued) {
        calculatedFeatureList = ((FeatureValued) eObject).toFeatureList();
      }
    }

    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
    System.out.println("New way of only replacing 'other' references");
    System.out.println(calculatedFeatureList);
    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");


    return calculatedFeatureList;
  }

  /**
   * Find the reference or the references that is not the same as the eObject's eResource and replace them
   * (the last part is NYI)
   *
   * @param eObject   The eObject to replace references in
   */
  @SuppressWarnings("unchecked")
  private EObject replaceReference(EObject eObject, FeatureValuedContainer featureValuedContainer) {
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
            MetaDataFeatureValued metaDataFeatureValuedReferenced = findMetaDataFeatureValuedReferenced(featureValuedContainer, referenced);
            eObject.eSet(eStructuralFeature, metaDataFeatureValuedReferenced);
          }
        }
      } else {
        EObject referenced = (EObject) eObject.eGet(eStructuralFeature);
        if (referenced.eResource() != eObject.eResource()) {
          MetaDataFeatureValued metaDataFeatureValuedReferenced = findMetaDataFeatureValuedReferenced(featureValuedContainer, referenced);
          eObject.eSet(eStructuralFeature, metaDataFeatureValuedReferenced);
        }
      }
    }
    return eObject;
  }

  private MetaDataFeatureValued findMetaDataFeatureValuedReferenced(FeatureValuedContainer featureValuedContainer, EObject referenced) {
    // Now find the featureValued with the correct id in the retrieving user's data.
    Iterator<EObject> iterator = featureValuedContainer.eAllContents();
    while (iterator.hasNext()) {
      EObject containerIt = iterator.next();
      if (containerIt instanceof MetaDataFeatureValued && referenced instanceof MetaDataFeatureValued) {
        MetaDataFeatureValued referencedMetaDataFeatureValued = (MetaDataFeatureValued) referenced;
        if (((MetaDataFeatureValued) containerIt).getFeatureValuedId().equals(referencedMetaDataFeatureValued.getFeatureValuedId())){
          // Replace reference to data in config xmi
          return (MetaDataFeatureValued) containerIt;
        }
      }
    }
    // Throw error instead?
    return null;
  }


}
