package com.moe.metricsconsumer.controllers;


import no.hal.learning.fv.ExpressionFeatures;
import no.hal.learning.fv.FeatureList;
import no.hal.learning.fv.FvFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.io.IOException;

public class ConfigCreator {

  public void create() throws IOException {

    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("no_hal_javalang__foreach", 4.0);
    featureList.getFeatures().put("no_hal_javalang__while", 5.0);
    featureList.getFeatures().put("no_hal_javalang__for", 2.0);

    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(featureList);

    System.out.println(expressionFeatures.getOther());

    // TODO: This should be configurable
    expressionFeatures.getFeatures().put("sum", "no_hal_javalang__foreach + no_hal_javalang__while");
    expressionFeatures.getFeatures().put("for-while", "no_hal_javalang__for + no_hal_javalang__while");
    expressionFeatures.getFeatures().put("for", "no_hal_javalang__for");

    //expressionFeatures.getFeatures().put("one", "m * n + 1");

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(expressionFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(featureList);

    dataResource.save(null);
    configResource.save(null);
  }
}
