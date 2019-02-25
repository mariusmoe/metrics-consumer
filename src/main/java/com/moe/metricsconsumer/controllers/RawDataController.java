package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moe.metricsconsumer.config.IMetricsProviderConfig;
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
import no.hal.learning.exercise.jdt.metrics.AbstractMetricsProvider;
import no.hal.learning.exercise.jdt.metrics.Activator;
import no.hal.learning.exercise.jdt.metrics.IMetricsProvider;
import no.hal.learning.exercise.jdt.metrics.impl.LineCountMetric;
import no.hal.learning.exercise.junit.JunitPackage;
import no.hal.learning.exercise.workbench.WorkbenchPackage;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;


import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

  @Autowired
  private IMetricsProviderConfig iMetricsProviderConfig;

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
    ExercisePackage   exercisePackage  = ExercisePackage.eINSTANCE;
    JdtPackage        jdtPackage       = JdtPackage.eINSTANCE;
    JunitPackage      junitPackage     = JunitPackage.eINSTANCE;
    WorkbenchPackage  workbenchPackage = WorkbenchPackage.eINSTANCE;

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

      // Holds a list of source code parts from the incoming .ex file
      ArrayList<String> sourceCodeList = new ArrayList<>();;

      TreeIterator<EObject> iterator = exFileResource.getAllContents();
      while (iterator.hasNext()) {
        EObject exEObject = iterator.next();
        // TODO: can there be done more aggressive pruning?
        if (exEObject instanceof Exercise) {
          iterator.prune();
        }
        if (exEObject instanceof JdtSourceEditProposal) {
          // getString() on the last attempt
          JdtSourceEditProposal jdtSourceEditProposal = (JdtSourceEditProposal) exEObject;
          EList eList = jdtSourceEditProposal.getAttempts();
          if (eList != null && !eList.isEmpty()) {
            JdtSourceEditEvent lastAttemptOnTask = (JdtSourceEditEvent) eList.get(eList.size()-1);
            String sourceCode = lastAttemptOnTask.getEdit().getString();
            sourceCodeList.add(sourceCode);
          }
        }
      }

      StringBuilder sb = new StringBuilder();
      for (String s : sourceCodeList)
      {
        sb.append(s);
        sb.append("\n\n");
      }

      // OK, so now we have a bunch of these source code parts in sourceCodeList that answers different parts of the
      // exercise. Should they all be combined and used to generate an AST or should they all get their own AST?

      IMetricsProvider lineCountMetric = new LineCountMetric();
      Object o = lineCountMetric.computeMetrics(sb.toString());
      logger.info("iMetricsProvidersList: "  + iMetricsProviderConfig.getIMetricsProvidersList().toString());
      try {
        logger.info("iMetricsProvidersList: "  + findMyTypes("no.hal.learning.exercise.jdt.metrics").toString());
//        AbstractMetricsProvider
//        IMetricsProvider
//        AbstractASTMetricsProvider
//        Activator
//        LineCountMetric
//        QNameCountersMetric
//        TokenCountersMetric
//        ConditionalCountersMetric
//        DefaultMetricsViewProvider
//        IMetricsViewProvider
//        Activator
//        Loop over these^ and filter based on which
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

      System.out.println(sb.toString() + "\n\n----------------------\n" + lineCountMetric.computeMetrics(sb.toString()) +
        "\n----------------------------------------------------\n\n");



//      https://stackoverflow.com/a/33755879/5489113  or
//      https://stackoverflow.com/a/45274409/5489113

      // OSGI stuff -> can this be used in any way? if not: How can this be solved instead?
//      https://stackoverflow.com/questions/8518837/using-osgi-in-a-desktop-standalone-application
//      public static void main() {
//
//        1. get a FrameworkFactory using java.util.ServiceLoader.
//        2. create an OSGi framework using the FrameworkFactory
//        3. start the OSGi framework
//        4. Install your bundle(s).
//        5. Start all the bundles you installed.
//        6. Wait for the OSGi framework to shutdown.
//
//      }
//      Activator activator = new Activator();
//      try {
//        activator.start(FrameworkUtil.getBundle(IMetricsProvider.class).getBundleContext());
//      } catch (Exception e) {
//        e.printStackTrace();
//      }

//      Kun en tanke -> hva med å traversere exFileResource som et tre og heller ta aksjon hvis objektet er av typen
//      JdtSourceEditProposal, da skal en hente attempts og gjøre getString() på siste forsøk.
//      ?
//
//       can exFileResource be an instance of Exercise? or
//      Exercise exercise;
//      ExerciseProposals exerciseProposals;
//       The exFileResource comes with two eObjects one Exercise and one ExerciseProposals
//      for (EObject eObject : exFileResource.getContents()) {
//        if (eObject instanceof ExerciseProposals) {
//          exerciseProposals = (ExerciseProposals) eObject;
//          logger.info("Title: " + exerciseProposals.getExercise().getTitle());
//
//          Iterator<EObject> iterator = exFileResource.getAllContents();
//          while (iterator.hasNext()) {
//            EObject exEObject = iterator.next();
//
//            if (exEObject instanceof JdtSourceEditProposal) {
//              // getString() on the last attempt
//              JdtSourceEditProposal jdtSourceEditProposal = (JdtSourceEditProposal) exEObject;
//              EList eList = jdtSourceEditProposal.getAttempts();
//              if (eList != null && !eList.isEmpty()) {
//                T item = eList.get(eList.size()-1);
//              }
//            }
//          }
//
//
//          // Can something more specific than EObject be used in the following 'for loop'
//          for (EObject eObject1:  exerciseProposals.getAllProposals()){
//            // Is it important which stringEdit is used? The last?
//            if (eObject1 instanceof JdtSourceEditProposal) {
//              JdtSourceEditProposal jdtSourceEditProposal = (JdtSourceEditProposal) eObject1;
//              for (EObject eObject2: jdtSourceEditProposal.getAttempts()){
//                if (eObject2 instanceof JdtSourceEditEvent) {
//                  JdtSourceEditEvent jdtSourceEditEvent = (JdtSourceEditEvent) eObject2;
//                  String s = jdtSourceEditEvent.getEdit().getString();
//                  String s2 = jdtSourceEditEvent.getEdit().getString();
//                }
//              }
//
////              jdtSourceEditProposal.getAnswer()
//              // Get the exercise source code as a string
//              String s = ((AbstractStringEdit) eObject1).getString();
//
//
//            }
//          }
//        }
//      }

      // Give AST to all IMetricProviders
      // gather results in one MeasureSummary object and return this.

    }

    return null;
  }

  private List<Class> findMyTypes(String basePackage) throws IOException, ClassNotFoundException
  {
    ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

    List<Class> candidates = new ArrayList<Class>();
    String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
      resolveBasePackage(basePackage) + "/" + "**/*.class";
    org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
    for (org.springframework.core.io.Resource resource : resources) {
      if (resource.isReadable()) {
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
        if (isCandidate(metadataReader)) {
          candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
        }
      }
    }
    return candidates;
  }

  private String resolveBasePackage(String basePackage) {
    return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
  }

  private boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException
  {
    try {
      Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
      System.out.println(c.getSimpleName());
      if (c.getAnnotation(XmlRootElement.class) != null) {
        return true;
      }
    }
    catch(Throwable e){
    }
    return false;
  }


}
