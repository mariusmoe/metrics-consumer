package com.moe.metricsconsumer.controllers;

import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.fv.FeatureValued;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.repositories.MeasureRepository;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
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

import com.moe.metricsconsumer.fv.ExpressionFeatures;
import com.moe.metricsconsumer.fv.FeatureList;
import com.moe.metricsconsumer.fv.FvFactory;

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
        System.out.println(measure.getMeasureProvider() + "_" +specificMeasure.getName());
      }
    }

    // load config from XMI
//    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
//    ResourceSet resSet = new ResourceSetImpl();
//    Resource resource = resSet.getResource(URI.createURI("config.xmi"), true);
//    for (EObject eObject : resource.getContents()) {
//      if (eObject instanceof FeatureValued) {
//
//      }
//    }
    // link config to real data, i.e. featureList

    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(featureList);

    System.out.println(expressionFeatures.getOther());

    // TODO: This should be configurable
    expressionFeatures.getFeatures().put("sum", "no_hal_javalang__foreach + no_hal_javalang__while");
    expressionFeatures.getFeatures().put("for-while", "no_hal_javalang__for + no_hal_javalang__while");
    expressionFeatures.getFeatures().put("for", "no_hal_javalang__for");

    //expressionFeatures.getFeatures().put("one", "m * n + 1");



    FeatureList finalFeatureList = FvFactory.eINSTANCE.createFeatureList();

    for (String featureName : expressionFeatures.getFeatureNames()) {
      finalFeatureList.getFeatures().put(featureName, expressionFeatures.getFeatureValue(featureName));
    }

	  return finalFeatureList;
  }
	
	
	
		
		
		
}
