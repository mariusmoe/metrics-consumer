package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moe.metricsconsumer.apiErrorHandling.EntityNotFoundException;
import com.moe.metricsconsumer.config.IMetricsProviderConfig;
import com.moe.metricsconsumer.models.ExSolution;
import com.moe.metricsconsumer.models.ExerciseDocument;

import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.models.rewards.UserAchievement;
import com.moe.metricsconsumer.repositories.*;
import no.hal.learning.exercise.*;
import no.hal.learning.exercise.jdt.JdtLaunchProposal;
import no.hal.learning.exercise.jdt.JdtPackage;
import no.hal.learning.exercise.jdt.JdtSourceEditEvent;
import no.hal.learning.exercise.jdt.JdtSourceEditProposal;
import no.hal.learning.exercise.jdt.metrics.IMetricsProvider;
import no.hal.learning.exercise.junit.JunitPackage;
import no.hal.learning.exercise.junit.JunitTestProposal;
import no.hal.learning.exercise.workbench.CommandExecutionProposal;
import no.hal.learning.exercise.workbench.WorkbenchPackage;
import no.hal.learning.fv.*;
import no.hal.learning.fv.util.Op2;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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

  @Autowired
  private ExSolutionRepository exSolutionRepository;

  @Autowired
  private UserAchievementRepository userAchievementRepository;

  ControllerUtil controllerUtil = new ControllerUtil();

  Logger logger = LoggerFactory.getLogger(RawDataController.class);

  private static final String approvedContentType = "application/octet-stream";

  private static Map<String, String> exerciseNames;
  static {
    exerciseNames = new HashMap<>();
    exerciseNames.put("1", "Øving 01: Objekter og klasser, til⁣stand og oppførsel");
    exerciseNames.put("2", "Øving 02: Innkapsling og vali⁣dering");
    exerciseNames.put("3", "Øving 03: Klasser og testing");
    exerciseNames.put("4", "Øving 04: Objektstrukturer med app");
    exerciseNames.put("5", "Øving 05: Objektstrukturer");
    exerciseNames.put("6", "Øving 06: Grensesnitt");
    exerciseNames.put("7", "Øving 07: Filbehandling med app");
    exerciseNames.put("8", "Øving 08: Observatør-Observert og Delegering");
    exerciseNames.put("9", "Øving 09: Arv og abstrakte klasser");
  }





  // Possible extensions - print a warning if some metrics has not been calculated
  // Flow of information - see activity diagram -> need to be sent with a code
  @PostMapping("/")
  @ResponseBody
  public ResponseEntity<ObjectNode> newStudentCode(@Nullable @RequestHeader(value="measureSummaryRef") String measureSummaryRef,
                                                   @RequestHeader(value = "exNumber") String exNumber,
                                                   @RequestParam("files") MultipartFile[] uploadingFiles,
                                                   Principal principal) throws IOException {

    Map<String, String> principalMap = this.controllerUtil.getPrincipalAsMap(principal);
    return saveExFiles(measureSummaryRef, uploadingFiles, principalMap.get("userid"),
      false,  exNumber);
  }


//  @PostMapping("/solution")
//  @ResponseBody
//  public ResponseEntity newSolutionCode(@RequestHeader(value="measureSummaryRef") String measureSummaryRef,
//                                        @RequestParam("files") MultipartFile[] uploadingFiles) throws IOException {
//    return saveExFiles(measureSummaryRef, uploadingFiles, "001", true);
//  }

  /**
   * Save and calculate measureSummary for the uploaded ex files
   *
   * It is assumed that all .ex files uploaded in a bulk belong together and thus is the answer to one exercise.
   * @param measureSummaryRefParam  reference an old measureSummary
   * @param uploadingFiles  the .ex files for one exercise
   * @param userId  the userId of the logged in user
   * @param isSolutionManual
   * @return
   * @throws IOException
   */
  private ResponseEntity<ObjectNode> saveExFiles(String measureSummaryRefParam,
                                                 MultipartFile[] uploadingFiles,
                                                 String userId,
                                                 boolean isSolutionManual,
                                                 String exNumber) throws IOException {
    logger.info("saveExFiles called initiating savings...");
    String measureSummaryRef = measureSummaryRefParam;
    if (measureSummaryRef == null) {
      Map<String, MeasureSummary> sourceAndSolutionCode = calculateMeasureSummaryFromExFiles(uploadingFiles,
        null, userId, isSolutionManual, exNumber);
      MeasureSummary savedMeasureSummary = sourceAndSolutionCode.get("student");
      MeasureSummary savedMeasureSummarySolution  = sourceAndSolutionCode.get("solution");
      // save the solution as well!!!!

      this.measureRepository.findFirstByUserIdAndTaskIdAndIsSolutionManual(userId,exNumber , false)
        .map((m2) -> {
          savedMeasureSummary.setId(m2.getId());
          return this.measureRepository.save(savedMeasureSummary);
      }).orElse(this.measureRepository.save(savedMeasureSummary));

      this.measureRepository.findFirstByUserIdAndTaskIdAndIsSolutionManual(userId,exNumber , true)
        .map((m2) -> {
          savedMeasureSummarySolution.setId(m2.getId());
          return this.measureRepository.save(savedMeasureSummarySolution);
      }).orElse(this.measureRepository.save(savedMeasureSummarySolution));

      // TODO check if the new measureSummary warrants a reward
      List<Achievement> relevantAchievements = this.achievementRepository.findByTaskIdRefOrIsCumulative(
        savedMeasureSummary.getTaskId(), true);
      // Will find all achievements a user has ever received
      List<UserAchievement> userAchievements = this.userAchievementRepository.findAllByUserRef(userId);
      List<UserAchievement> rewardedUserAchievements = controllerUtil.checkAchievements(
        savedMeasureSummary,relevantAchievements, userAchievements);
      this.userAchievementRepository.saveAll(rewardedUserAchievements);
      logger.debug(rewardedUserAchievements.toString());


      // This is a new exercise being added
      measureSummaryRef = savedMeasureSummary.getId();
    } else {
      // recalculate measure summary from MultipartFile[]
      Map<String, MeasureSummary> sourceAndSolutionCode = calculateMeasureSummaryFromExFiles(uploadingFiles,
        measureSummaryRef, userId, isSolutionManual, exNumber);
      MeasureSummary recalculatedMeasureSummary = sourceAndSolutionCode.get("student");
      MeasureSummary savedMeasureSummarySolution  = sourceAndSolutionCode.get("solution");
      this.measureRepository.save(recalculatedMeasureSummary);
      this.measureRepository.save(savedMeasureSummarySolution);
      try {
        xmlRepository.removeByMeasureSummaryRef(measureSummaryRef);
      } catch (Exception e) {
        logger.error(e.toString());
      }
    }
    logger.debug("measureSummaryRef: " + measureSummaryRef);

    // Should: refactor code below to its own method
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

    return new ResponseEntity<>( controllerUtil.jsonResponse(2000, "Success - document has been saved"),
      HttpStatus.OK);
  }

  @SuppressWarnings("unchecked")
  private Map<String, MeasureSummary> calculateMeasureSummaryFromExFiles(MultipartFile[] uploadingFiles,
                                                                         String measureSummaryRef,
                                                                         String userId,
                                                                         boolean isSolutionManual,
                                                                         String exNumber) throws IOException {

    // These packages are not yet in memory or loaded by a manifest file and has to be loaded in memory to be found
    // See: https://wiki.eclipse.org/EMF/FAQ#I_get_a_PackageNotFoundException:_e.g..2C_.22Package_with_uri_.27http:.2F.
    // 2Fcom.example.company.ecore.27_not_found..22_What_do_I_need_to_do.3F
    ExercisePackage   exercisePackage  = ExercisePackage.eINSTANCE;
    JdtPackage        jdtPackage       = JdtPackage.eINSTANCE;
    JunitPackage      junitPackage     = JunitPackage.eINSTANCE;
    WorkbenchPackage  workbenchPackage = WorkbenchPackage.eINSTANCE;
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ex", new XMIResourceFactoryImpl());

    FeatureValuedContainer fContainer = FvFactory.eINSTANCE.createFeatureValuedContainer();
    FeatureValuedContainer fContainerSolution = FvFactory.eINSTANCE.createFeatureValuedContainer();
    logger.debug("iMetricsProvidersList: "  + iMetricsProviderConfig.getMetricsProviders().toString());
    try {
      for (String provider : iMetricsProviderConfig.getMetricsProviders()) {
        logger.debug("iMetricsProvider classes: " + findMyTypes(provider).toString());
        FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
        FeatureList solutionFeatureList = FvFactory.eINSTANCE.createFeatureList();
        // Create one featureList
        // with all providers and sum up results from all source code snippets
        for (Class c : findMyTypes(provider)) {
          for(MultipartFile uploadedFile : uploadingFiles) {
            Resource exFileResource           = getExResource(uploadedFile);
            Map<String, ArrayList<String>> sourceAndSolutionCode = getSourceCodeList(exFileResource);
            ArrayList<String> sourceCodeList  = sourceAndSolutionCode.get("sourceCode");
            ArrayList<String> solutionCodeList  = sourceAndSolutionCode.get("solutionCode");

            for (String solutionCode : solutionCodeList) {
              IMetricsProvider iMetricsProvider = (IMetricsProvider) Class.forName(c.getName()).newInstance();
              FeatureValued featureValued = iMetricsProvider.computeMetrics(solutionCode);
              solutionFeatureList.apply(Op2.PLUS, featureValued.toFeatureList(), false);
            }

            for (String sourceCode : sourceCodeList) {
              IMetricsProvider iMetricsProvider = (IMetricsProvider) Class.forName(c.getName()).newInstance();
              FeatureValued featureValued = iMetricsProvider.computeMetrics(sourceCode);
              featureList.apply(Op2.PLUS, featureValued.toFeatureList(), false);
            }
          }
        }
        MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
        metaDataFeatureValued.setFeatureValuedId(provider);
        metaDataFeatureValued.setDelegatedFeatureValued(featureList);
        fContainer.getFeatureValuedGroups().add(metaDataFeatureValued);

        MetaDataFeatureValued metaDataFeatureValuedSolution = FvFactory.eINSTANCE.createMetaDataFeatureValued();
        metaDataFeatureValuedSolution.setFeatureValuedId(provider);
        metaDataFeatureValuedSolution.setDelegatedFeatureValued(solutionFeatureList);
        fContainerSolution.getFeatureValuedGroups().add(metaDataFeatureValuedSolution);
      }
    } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
    logger.info(fContainer.toString());

    // Convert to measureSummary

    List<Measure> measures = controllerUtil.createMeasuresFromContainer(fContainer);
    List<Measure> measuresSolution = controllerUtil.createMeasuresFromContainer(fContainerSolution);
    MeasureSummary measureSummary = new MeasureSummary(userId,
      isSolutionManual,
      exerciseNames.get(exNumber),
      exNumber,
      measures);
    MeasureSummary measureSummarySolution = new MeasureSummary(userId,
      true,
      exerciseNames.get(exNumber),
      exNumber,
      measuresSolution);
    Map<String, MeasureSummary> measureSummaryStudentAndSolution = new HashMap<>();
    measureSummaryStudentAndSolution.put("student", measureSummary);
    measureSummaryStudentAndSolution.put("solution", measureSummarySolution);

    return measureSummaryStudentAndSolution;
  }



  /**
   * Uses a tree iterator to go through the exFileResource and finds all source code snippets
   * @param exFileResource  The file resource with the exercise proposals in
   * @return  sourceCodeList - a list with source code snippets at each index
   */
  private Map<String, ArrayList<String>> getSourceCodeList(Resource exFileResource) {
    // Holds a list of source code parts from the incoming .ex file
    ArrayList<String> sourceCodeList = new ArrayList<>();
    ArrayList<String> solutionCodeList = new ArrayList<>();

    // Iterate through the whole ex resource and prune as often as possible
    TreeIterator<EObject> iterator = exFileResource.getAllContents();
    while (iterator.hasNext()) {
      EObject exEObject = iterator.next();
      if (exEObject instanceof JdtSourceEditProposal) {
        // getString() on the last attempt
        JdtSourceEditProposal jdtSourceEditProposal = (JdtSourceEditProposal) exEObject;
        String exClassName = jdtSourceEditProposal.getAnswer().getClassName();
        ExSolution exSolution = null;
        // Find the solution code
        try {
          exSolution = this.exSolutionRepository.findFirstByExClassName(exClassName)
            .orElseThrow(() -> new EntityNotFoundException(ExSolution.class, exClassName));
        } catch (EntityNotFoundException e) {
          e.printStackTrace();
        }
        EList eList = jdtSourceEditProposal.getAttempts();
        if (eList != null && !eList.isEmpty()) {
          // Only add solution if the student has answered the task
          solutionCodeList.add(exSolution.getExContent());
          // add the student code
          JdtSourceEditEvent lastAttemptOnTask = (JdtSourceEditEvent) eList.get(eList.size()-1);
          String sourceCode = lastAttemptOnTask.getEdit().getString();
          sourceCodeList.add(sourceCode);
        }
      } else if (exEObject instanceof Exercise
        || exEObject instanceof JunitTestProposal
        || exEObject instanceof JdtLaunchProposal
        || exEObject instanceof CommandExecutionProposal) {
        iterator.prune();
      }
    }
    Map<String, ArrayList<String>> sourceAndSolutionCode = new HashMap<>();
    sourceAndSolutionCode.put("sourceCode", sourceCodeList);
    sourceAndSolutionCode.put("solutionCode", solutionCodeList);
    return sourceAndSolutionCode;
  }

  /**
   * Loads the file into a emf ecore resource
   * @param uploadedFile  the file that is uploaded (singular)
   * @return  a resource with the exercise and exercise proposal
   * @throws IOException
   */
  private Resource getExResource(MultipartFile uploadedFile) throws IOException {
    ResourceSet resSet = new ResourceSetImpl();
    // load config from XMI
    Resource exFileResource = resSet.createResource(URI.createURI("exFileResource.ex"));
    try {
      exFileResource.load(new ByteArrayInputStream(uploadedFile.getBytes()),null );
    } catch (IOException e) {
      logger.warn("Could not: uploadedFile.getBytes(). Something is probably wrong with the file");
      e.printStackTrace();
      throw e;
    }
    return exFileResource;
  }

  /**
   * Will look through provided package and find classes that ends with "Metric". Modified method from
   * @See <a href="https://stackoverflow.com/a/5343940/5489113">https://stackoverflow.com/a/5343940/5489113</a>
   * @param basePackage The package that will we looked through
   * @return  a list of classes that provides the IMetricProvider interface
   * @throws IOException
   * @throws ClassNotFoundException
   */
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
      if (c.getSimpleName().endsWith("Metric")) {
        return true;
      }
    }
    catch(Throwable e){
    }
    return false;
  }


}
