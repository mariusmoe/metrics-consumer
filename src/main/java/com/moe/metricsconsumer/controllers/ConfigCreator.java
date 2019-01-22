package com.moe.metricsconsumer.controllers;



import javafx.util.Pair;
import no.hal.learning.fv.*;
import no.hal.learning.fv.impl.MetaDataFeatureValuedImpl;
import org.bson.types.Binary;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigCreator {

  public void create() throws IOException {





/*
    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setDelegation(FvFactory.eINSTANCE.createFeatureList());
    metaDataFeatureValued.setFeatureValuedId("lang");
    metaDataFeatureValued.setTags(Arrays.asList("some", "tag"));

    metaDataFeatureValued.getFeatures().put("no_hal_javalang__foreach", 4.0);
    metaDataFeatureValued.getFeatures().put("no_hal_javalang__while", 5.0);
    metaDataFeatureValued.getFeatures().put("no_hal_javalang__for", 2.0);

    FeatureValuedContainer featureValuedContainer = FvFactory.eINSTANCE.createFeatureValuedContainer();
    featureValuedContainer.getFeatureValuedGroups().add(metaDataFeatureValued);


*/

    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();


    featureList.getFeatures().put("foreach", 99.0);
    featureList.getFeatures().put("while", 99.0);
    featureList.getFeatures().put("for", 99.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);


    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(metaDataFeatureValued);




    System.out.println(expressionFeatures.getOther());

    // TODO: This should be configurable
    expressionFeatures.getFeatures().put("sum-for-while", "foreach + while");
    expressionFeatures.getFeatures().put("add-for-while", "for + while");
    expressionFeatures.getFeatures().put("just-for", "for");

    //expressionFeatures.getFeatures().put("one", "m * n + 1");

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(expressionFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    dataResource.save(null);
    configResource.save(null);
  }

  public void create2() throws IOException {

    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("foreach", 4.0);
    featureList.getFeatures().put("while", 5.0);
    featureList.getFeatures().put("for", 2.0);


    DerivedFeatures1 derivedFeatures = FvFactory.eINSTANCE.createDerivedFeatures1();
    derivedFeatures.setOp(Op2Kind.PLUS);
    derivedFeatures.setOp1(Op1Kind.NEG);
    derivedFeatures.setVal(3.0);
    derivedFeatures.setOther(featureList);





    //expressionFeatures.getFeatures().put("one", "m * n + 1");

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(derivedFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(featureList);

    dataResource.save(null);
    configResource.save(null);
  }

  public void create3() throws IOException {

    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("no_hal_javalang__foreach", 4.0);
    featureList.getFeatures().put("no_hal_javalang__while", 5.0);
    featureList.getFeatures().put("no_hal_javalang__for", 2.0);


    FilteredFeatures1 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures1();
    filteredFeatures.setNameFilter("no_hal_javalang__foreach");
    filteredFeatures.setPred(Pred1Kind.GT0);
    filteredFeatures.setOther(featureList);


    //expressionFeatures.getFeatures().put("one", "m * n + 1");

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(filteredFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(featureList);

    dataResource.save(null);
    configResource.save(null);
  }

  public void create4() throws IOException {

    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("no_hal_javalang__foreach", 4.0);
    featureList.getFeatures().put("no_hal_javalang__while", 5.0);
    featureList.getFeatures().put("no_hal_javalang__for", 2.0);


    FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures.setPred(Pred2Kind.GT);
    filteredFeatures.setVal(900.0);
    filteredFeatures.setOther(featureList);


    //expressionFeatures.getFeatures().put("one", "m * n + 1");

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(filteredFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(featureList);

    dataResource.save(null);
    configResource.save(null);
  }

  public void createAchievementConfig() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("foreach", 4.0);
    featureList.getFeatures().put("while", 5.0);
    featureList.getFeatures().put("for", 2.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);

    FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures.setPred(Pred2Kind.GT);
    filteredFeatures.setVal(4);
    filteredFeatures.setOther(metaDataFeatureValued);

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("achievementConfig.xmi"));
    configResource.getContents().add(filteredFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("achievementData.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    dataResource.save(null);
    configResource.save(null);
  }

  public void createAchievementConfig2() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("foreach", 4.0);
    featureList.getFeatures().put("while", 5.0);
    featureList.getFeatures().put("for", 2.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);

    FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures.setPred(Pred2Kind.GT);
    filteredFeatures.setVal(7);
    filteredFeatures.setOther(metaDataFeatureValued);

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("achievementConfig2.xmi"));
    configResource.getContents().add(filteredFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("achievementData2.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    dataResource.save(null);
    configResource.save(null);


  }

  public ArrayList createAchievementConfig3() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("foreach", 4.0);
    featureList.getFeatures().put("while", 5.0);
    featureList.getFeatures().put("for", 2.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);

    FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures.setPred(Pred2Kind.GT);
    filteredFeatures.setVal(2);
    filteredFeatures.setOther(metaDataFeatureValued);

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("achievementConfig3.xmi"));
    configResource.getContents().add(filteredFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("achievementData3.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    ByteArrayOutputStream dataResourceOutputStream = new ByteArrayOutputStream();
    dataResource.save(dataResourceOutputStream ,null);

    ByteArrayOutputStream configResourceOutputStream = new ByteArrayOutputStream();
    configResource.save(configResourceOutputStream ,null);


    ArrayList<byte[]> list = new ArrayList<>();
    list.add(dataResourceOutputStream.toByteArray());
    list.add(configResourceOutputStream.toByteArray());

    return list;
  }

}
