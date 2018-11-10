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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
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
import java.security.Principal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;


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
    return getOneGenericMeasureFv(taskId,  true);
  }

	private FeatureList getOneGenericMeasureFv(String taskId, boolean isSolution, String... userId) throws EntityNotFoundException {
    String _userId = (userId.length >= 1) ? userId[0] : "";


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    MeasureSummary measureSummary;


    if (isSolution) {
      Query query = new Query();
      query.addCriteria(Criteria.where("isSolutionManual").is(true));
      query.addCriteria(Criteria.where("taskId").is(taskId));
      measureSummary = this.mongoTemplate.findOne(query, MeasureSummary.class);
    } else {
      measureSummary = this.measureRepository.findFirstByUserIdAndTaskId("001", taskId);
    }

   if (measureSummary == null) {
     throw new EntityNotFoundException(MeasureSummary.class, taskId);

   }

    for (Measure measure : measureSummary.getMeasures()) {
      for (SpecificMeasure specificMeasure : measure.getSpecificMeasures()) {
        featureList.getFeatures().put(measure.getMeasureProvider()
          .replace(".", "_") + "__" +specificMeasure.getName()
          .replace(".", "_"), (double) specificMeasure.getValue());
//        System.out.println(measure.getMeasureProvider() + "_" +specificMeasure.getName());
      }
    }
    System.out.println("----------------------------------------------");
    System.out.println("----------------------------------------------");
    // load config from XMI
    ConfigCreator configCreator = new ConfigCreator();
    try {
      configCreator.create();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();
    Resource resource = resSet.getResource(URI.createURI("config.xmi"), true);

    Resource newDataResource = resSet.createResource(URI.createURI("newData.xmi"));
    newDataResource.getContents().add(featureList);

    // Replace all references to data.xmi with newData.xmi
    for (EObject eObject : resource.getContents()) {
      if (eObject instanceof FeatureValued) {
        System.out.println(eObject.eClass().getEAllStructuralFeatures());
        for (EStructuralFeature eStructuralFeature : eObject.eClass().getEAllStructuralFeatures()) {
          if (eStructuralFeature.getName().equals("other")) {
            System.out.println("YAY");
            eObject.eSet(eStructuralFeature,featureList);
          }
        }
        System.out.println("*******");
        System.out.println(eObject.eContents());
        System.out.println(eObject.eClass().getEAllStructuralFeatures());

//        eObject.eSet(eObject.eContents(), featureList);


      }
      System.out.println(eObject);
    }
    System.out.println("------");

    System.out.println(resource.getAllContents());

    System.out.println("----------------------------------------------");
    System.out.println("----------------------------------------------");
    // link config to real data, i.e. featureList

    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(featureList);

    System.out.println(expressionFeatures.getOther());


    FvConfiguration fvConfiguration = fvConfigurationRepository.findFirstByTaskId(taskId);
    System.out.println(taskId);
    System.out.println(fvConfiguration.toString());
    for (Map.Entry<String, String> entry : fvConfiguration.getExpressionFeature().entrySet()) {
      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      expressionFeatures.getFeatures().put(entry.getKey(), entry.getValue());
    }

    FeatureList finalFeatureList = FvFactory.eINSTANCE.createFeatureList();

    for (String featureName : expressionFeatures.getFeatureNames()) {
      finalFeatureList.getFeatures().put(featureName, expressionFeatures.getFeatureValue(featureName));
    }

	  return finalFeatureList;
  }
	
	
	
		
		
		
}
