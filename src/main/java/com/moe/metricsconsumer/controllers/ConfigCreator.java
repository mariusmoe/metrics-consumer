package com.moe.metricsconsumer.controllers;




import no.hal.learning.exercise.jdt.metrics.IMetricsProvider;
import no.hal.learning.fv.*;
import no.hal.learning.fv.impl.MetaDataFeatureValuedImpl;
import org.bson.types.Binary;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is used for debugging purposes
 */
public class ConfigCreator {

  Logger logger = LoggerFactory.getLogger(ConfigCreator.class);

  public void create() throws IOException {

//    IMetricsProvider iMetricsProvider =

      FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();


    featureList.getFeatures().put("foreach", 99.0);
    featureList.getFeatures().put("while", 99.0);
    featureList.getFeatures().put("for", 99.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);


    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(metaDataFeatureValued);




    logger.debug(expressionFeatures.getOther().toString());

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

  public ArrayList createAchievementConfig() throws IOException {


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

    return getByteArrayList(configResource, dataResource);
  }

  public ArrayList createAchievementConfig2() throws IOException {


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

    return getByteArrayList(configResource, dataResource);


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
    filteredFeatures.setVal(7);
    filteredFeatures.setOther(metaDataFeatureValued);

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("achievementConfig3.xmi"));
    configResource.getContents().add(filteredFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("achievementData3.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    return getByteArrayList(configResource, dataResource);
  }


  public ArrayList createAchievementConfig4() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("foreach", 4.0);
    featureList.getFeatures().put("while", 5.0);
    featureList.getFeatures().put("for", 2.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);

    FeatureList featureList2 = FvFactory.eINSTANCE.createFeatureList();
    featureList2.getFeatures().put("cyclomaticComplexity", 8.0);
    featureList2.getFeatures().put("CLOC", 23.0);
    featureList2.getFeatures().put("NCLOC", 15.0);

    MetaDataFeatureValued metaDataFeatureValued2 = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued2.setFeatureValuedId("org_metrics_cyclomatic");
    metaDataFeatureValued2.setDelegatedFeatureValued(featureList2);


    FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures.setPred(Pred2Kind.GT);
    filteredFeatures.setVal(2);
    filteredFeatures.setOther(metaDataFeatureValued);

    FilteredFeatures2 filteredFeatures2 = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures2.setPred(Pred2Kind.GT);
    filteredFeatures2.setVal(2);
    filteredFeatures2.setOther(metaDataFeatureValued2);

    // TODO: does not work because FeatureValuedContainer contains featureValuedGroups
    FeatureValuedContainer featureValuedContainer = FvFactory.eINSTANCE.createFeatureValuedContainer();
    featureValuedContainer.getFeatureValuedGroups().add(filteredFeatures);
    featureValuedContainer.getFeatureValuedGroups().add(filteredFeatures2);

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("achievementConfig3.xmi"));
    configResource.getContents().add(featureValuedContainer);

    Resource dataResource = resSet.createResource(URI.createURI("achievementData3.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);
    dataResource.getContents().add(metaDataFeatureValued2);

    ByteArrayOutputStream dataResourceOutputStream = new ByteArrayOutputStream();
    dataResource.save(dataResourceOutputStream ,null);
    dataResource.save(null);

    ByteArrayOutputStream configResourceOutputStream = new ByteArrayOutputStream();
    configResource.save(configResourceOutputStream ,null);
    configResource.save(null);


    ArrayList<byte[]> list = new ArrayList<>();
    list.add(dataResourceOutputStream.toByteArray());
    list.add(configResourceOutputStream.toByteArray());

    return list;
  }



  public ArrayList createAchievementConfig5() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    featureList.getFeatures().put("foreach", 4.0);
    featureList.getFeatures().put("while", 5.0);
    featureList.getFeatures().put("for", 2.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);

    FeatureList featureList2 = FvFactory.eINSTANCE.createFeatureList();
    featureList2.getFeatures().put("cyclomatic", 8.0);
    featureList2.getFeatures().put("CLOC", 23.0);
    featureList2.getFeatures().put("NCLOC", 15.0);

    MetaDataFeatureValued metaDataFeatureValued2 = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued2.setFeatureValuedId("org_metrics_cyclomatic");
    metaDataFeatureValued2.setDelegatedFeatureValued(featureList2);


    FilteredFeatures2 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures.setPred(Pred2Kind.GT);
    filteredFeatures.setVal(2);
    filteredFeatures.setOther(metaDataFeatureValued);

    FilteredFeatures2 filteredFeatures2 = FvFactory.eINSTANCE.createFilteredFeatures2();
    filteredFeatures2.setPred(Pred2Kind.GT);
    filteredFeatures2.setVal(2);
    filteredFeatures2.setOther(metaDataFeatureValued2);


    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("achievementConfig3.xmi"));
    configResource.getContents().add(filteredFeatures);
    configResource.getContents().add(filteredFeatures2);

    Resource dataResource = resSet.createResource(URI.createURI("achievementData3.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);
    dataResource.getContents().add(metaDataFeatureValued2);

    return getByteArrayList(configResource, dataResource);
  }

  private ArrayList getByteArrayList(Resource configResource, Resource dataResource) throws IOException {
    ByteArrayOutputStream dataResourceOutputStream = new ByteArrayOutputStream();
    dataResource.save(dataResourceOutputStream, null);

    ByteArrayOutputStream configResourceOutputStream = new ByteArrayOutputStream();
    configResource.save(configResourceOutputStream, null);


    ArrayList<byte[]> list = new ArrayList<>();
    list.add(dataResourceOutputStream.toByteArray());
    list.add(configResourceOutputStream.toByteArray());

    return list;
  }


  public ArrayList createFvConfig() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();


    featureList.getFeatures().put("foreach", 99.0);
    featureList.getFeatures().put("while", 99.0);
    featureList.getFeatures().put("for", 99.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);


    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(metaDataFeatureValued);


    logger.debug(expressionFeatures.getOther().toString());

    // TODO: This should be configurable


    expressionFeatures.getFeatures().put("sum", "foreach");
    expressionFeatures.getFeatures().put("for-while", "for + while");
    expressionFeatures.getFeatures().put("while", "while");
    expressionFeatures.getFeatures().put("while-3", "while * 3");
    expressionFeatures.getFeatures().put("for", "for");

    //expressionFeatures.getFeatures().put("one", "m * n + 1");

    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(expressionFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);


//    return configResourceOutputStream.toByteArray();
    return getByteArrayList(configResource,dataResource);
  }

  public ArrayList createFvConfig2() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();


    featureList.getFeatures().put("foreach", 99.0);
    featureList.getFeatures().put("while", 99.0);
    featureList.getFeatures().put("for", 99.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);


    DerivedFeatures1 derivedFeatures = FvFactory.eINSTANCE.createDerivedFeatures1();
    derivedFeatures.setOp(Op2Kind.PLUS);
    derivedFeatures.setOp1(Op1Kind.NEG);
    derivedFeatures.setVal(3.0);
    derivedFeatures.setOther(metaDataFeatureValued);




    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(derivedFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    return getByteArrayList(configResource,dataResource);
  }


  public ArrayList createFvConfig3() throws IOException {


    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();


    featureList.getFeatures().put("foreach", 99.0);
    featureList.getFeatures().put("while", 99.0);
    featureList.getFeatures().put("for", 99.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_javalang");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);


    FilteredFeatures1 filteredFeatures = FvFactory.eINSTANCE.createFilteredFeatures1();
    filteredFeatures.setNameFilter("f");
    filteredFeatures.setPred(Pred1Kind.GT0);
    filteredFeatures.setOther(metaDataFeatureValued);


    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(filteredFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    ByteArrayOutputStream configResourceOutputStream = new ByteArrayOutputStream();
    configResource.save(configResourceOutputStream, null);

    return getByteArrayList(configResource,dataResource);
  }

  public ArrayList createFvConfigFiles() throws IOException {



    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();


    featureList.getFeatures().put("PrimitiveType", 99.0);
    featureList.getFeatures().put("MarkerAnnotation", 99.0);
//    featureList.getFeatures().put("PrefixExpression.-", 99.0);
//    featureList.getFeatures().put("MethodDeclaration.public", 99.0);
//    featureList.getFeatures().put("InfixExpression.<", 99.0);
//    featureList.getFeatures().put("InfixExpression.>", 99.0);
    featureList.getFeatures().put("VariableDeclarationStatement", 99.0);
    featureList.getFeatures().put("Block", 99.0);
    featureList.getFeatures().put("VariableDeclarationFragment", 99.0);
    featureList.getFeatures().put("SimpleType", 99.0);
    featureList.getFeatures().put("SingleVariableDeclaration", 99.0);
    featureList.getFeatures().put("IfStatement", 99.0);
    featureList.getFeatures().put("ReturnStatement", 99.0);
    featureList.getFeatures().put("MethodInvocation", 99.0);
    featureList.getFeatures().put("PrefixExpression", 99.0);
    featureList.getFeatures().put("InfixExpression", 99.0);
    featureList.getFeatures().put("conditionalCount", 99.0);
    featureList.getFeatures().put("ParameterizedType", 99.0);
    featureList.getFeatures().put("Modifier", 99.0);
    featureList.getFeatures().put("SimpleName", 99.0);
    featureList.getFeatures().put("NumberLiteral", 99.0);
    featureList.getFeatures().put("QualifiedName", 99.0);
    featureList.getFeatures().put("MethodDeclaration", 99.0);
    featureList.getFeatures().put("TypeDeclaration.public", 99.0);
    featureList.getFeatures().put("lines", 99.0);

    MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
    metaDataFeatureValued.setFeatureValuedId("no_hal_learning_exercise_jdt_metrics");
    metaDataFeatureValued.setDelegatedFeatureValued(featureList);

    ExpressionFeatures expressionFeatures = FvFactory.eINSTANCE.createExpressionFeatures();
    expressionFeatures.setOther(metaDataFeatureValued);


    expressionFeatures.getFeatures().put("Primitive types", "PrimitiveType");
    expressionFeatures.getFeatures().put("cyclic", "IfStatement + 1");
    expressionFeatures.getFeatures().put("qualified names", "QualifiedName");
    expressionFeatures.getFeatures().put("Sum expression", "PrefixExpression + InfixExpression");
    expressionFeatures.getFeatures().put("Number of PrimitiveType", "PrimitiveType");




    // load config from XMI
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    Resource configResource = resSet.createResource(URI.createURI("config.xmi"));
    configResource.getContents().add(expressionFeatures);

    Resource dataResource = resSet.createResource(URI.createURI("data.xmi"));
    dataResource.getContents().add(metaDataFeatureValued);

    return getByteArrayList(configResource,dataResource);
  }
}
