package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import com.moe.metricsconsumer.models.rewards.Achievement;
import com.moe.metricsconsumer.models.rewards.AchievementState;
import com.moe.metricsconsumer.models.rewards.UserAchievement;
import no.hal.learning.fv.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


public class ControllerUtil {


  /**
   * Create a featureValuedContainer from a measureSummary
   * @param measureSummary  the measure summary to use in the conversion
   * @return  the converted FeatureValuedContainer
   */
  public FeatureValuedContainer createContainerFromMeasures(MeasureSummary measureSummary) {
    FeatureValuedContainer featureValuedContainer = FvFactory.eINSTANCE.createFeatureValuedContainer();
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
      // Add metadata to metaDataFeatureValued
      MetaDataFeatureValued metaDataFeatureValued = FvFactory.eINSTANCE.createMetaDataFeatureValued();
      metaDataFeatureValued.setFeatureValuedId(measure.getMeasureProvider().replace(".", "_"));
      // Add featureList to the metadata
      metaDataFeatureValued.setDelegatedFeatureValued(fvList);
      // Add the metadata to the root container
      featureValuedContainer.getFeatureValuedGroups().add(metaDataFeatureValued);
    }
    return featureValuedContainer;
  }

  /**
   * Create measureSummary from featureValuedContainer
   * @param fContainer the container to transform
   * @return  measures that can be used in a measureSummary
   */
  public List<Measure> createMeasuresFromContainer(FeatureValuedContainer fContainer) {
    List<Measure> measures = new ArrayList<>();
    for (FeatureValued o: fContainer.getFeatureValuedGroups()) {
      if (o instanceof MetaDataFeatureValued) {
        MetaDataFeatureValued metaDataFeatureValued = (MetaDataFeatureValued) o;
        FeatureList featureList = metaDataFeatureValued.getDelegatedFeatureValued().toFeatureList();
        List<SpecificMeasure> specificMeasures = new ArrayList<SpecificMeasure>();
        for (Map.Entry<String, Double> map: featureList.getFeatures()) {
          SpecificMeasure specificMeasure = new SpecificMeasure(map.getKey(), map.getValue().floatValue());
          specificMeasures.add(specificMeasure);
        }
        Measure measure = new Measure(metaDataFeatureValued.getFeatureValuedId(), specificMeasures);
        measures.add(measure);
      }
    }
    return measures;
  }

  /**
   * Find the reference or the references that is not the same as the eObject's eResource and replace them
   *
   * @param eObject   The eObject to replace references in
   */
  @SuppressWarnings("unchecked")
  public EObject replaceReference(EObject eObject, FeatureValuedContainer featureValuedContainer) {
    for (EReference eStructuralFeature : eObject.eClass().getEAllReferences()) {
      // Skip if its a container, isContainment, or is not changeable
      if (eStructuralFeature.isContainer() || !eStructuralFeature.isChangeable()) {
        continue;
      }
      // If there is more than one eStructuralFeature
      if (eStructuralFeature.isMany()) {
        EList<? extends EObject> references = (EList<? extends EObject>) eObject.eGet(eStructuralFeature);
        for (int i = 0; i < references.size(); i++) {
          EObject referenced = references.get(i);
          if (referenced != null && referenced.eResource() != eObject.eResource()) {
            MetaDataFeatureValued metaDataFeatureValuedReferenced = findMetaDataFeatureValuedReferenced(featureValuedContainer, referenced);
            eObject.eSet(eStructuralFeature, metaDataFeatureValuedReferenced);
          } else if (eStructuralFeature.isContainment()) {
            replaceReference(referenced, featureValuedContainer);
          }
        }
      } else {
        EObject referenced = (EObject) eObject.eGet(eStructuralFeature);
        if (referenced != null && referenced.eResource() != eObject.eResource()) {
          MetaDataFeatureValued metaDataFeatureValuedReferenced = findMetaDataFeatureValuedReferenced(featureValuedContainer, referenced);
          eObject.eSet(eStructuralFeature, metaDataFeatureValuedReferenced);
        } else if (eStructuralFeature.isContainment()) {
          replaceReference(referenced, featureValuedContainer);
        }
      }
    }
    return eObject;
  }

  /**
   * Find the correct data in the user data to replace with
   * @param featureValuedContainer  the user data
   * @param referenced  the reference that shall be changed
   * @return the MetaDataFeatureValued
   */
  private MetaDataFeatureValued findMetaDataFeatureValuedReferenced(FeatureValuedContainer featureValuedContainer,
                                                                    EObject referenced) {
    // Now find the featureValued with the correct id in the retrieving user's data.
    Iterator<EObject> iterator = featureValuedContainer.eAllContents();
    while (iterator.hasNext()) {
      EObject containerIt = iterator.next();
      if (containerIt instanceof MetaDataFeatureValued && referenced instanceof MetaDataFeatureValued) {
        MetaDataFeatureValued referencedMetaDataFeatureValued = (MetaDataFeatureValued) referenced;
        if (((MetaDataFeatureValued) containerIt).getFeatureValuedId().equals(referencedMetaDataFeatureValued.getFeatureValuedId())){
          // Replace reference to data in config xmi
          // Ensure consistency between the referenced and the referee (Only needed one way though "containerIt need all metrics in referenced")
          referencedMetaDataFeatureValued.getFeatures().map().forEach((k,v) -> {
            if (!((MetaDataFeatureValued) containerIt).getFeatures().containsKey(k)) {
              ((MetaDataFeatureValued) containerIt).getFeatures().put(k, (double) 0);
            }
          });
          return (MetaDataFeatureValued) containerIt;
        }
      }
    }
    // Throw error instead?
    return null;
  }

  /**
   * Simplify and standardize responses for easier parsing
   * @param status  an internal status code
   * @param message a custom message
   * @param additionalParams  optional object to include in the response
   * @return  the constructed objectNode
   */
  public ObjectNode jsonResponse(int status, String message, Object ...additionalParams) {
    Object _additionalParams = (additionalParams.length >= 1) ? additionalParams : null;
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode res = mapper.createObjectNode();
    res.put("status", ""+status);
    res.put("message", message);
    if (_additionalParams != null) {
      res.put("details", mapper.valueToTree(additionalParams));
    }
    return res;
  }

  public Map<String, String> getPrincipalAsMap(Principal principal) {
    String[] s = principal.getName().replace("{", "").replace("}","" ).split(",");
    Map<String, String> map = new HashMap<>();
    for (String s1: s) {
      map.put(s1.substring(0,s1.indexOf("=")).replace(" ", ""),  s1.substring(s1.indexOf("=")+1));
    }
    return map;
  }


  /**
   * Check if the provided measureSummary warrants any new achievements
   * @param measureSummary  the new measureSummary
   * @param relevantAchievements  all relevant achievements for this user
   * @param userAchievements  The achievements the user already have
   * @return a list of updated achievements, ready for being saved to db
   */
  public List<UserAchievement> checkAchievements(MeasureSummary measureSummary,
                                                 List<Achievement> relevantAchievements,
                                                 List<UserAchievement> userAchievements) {

    FeatureValuedContainer featureValuedContainer = createContainerFromMeasures(measureSummary);
    // Loop over -> add achieved achievements to list as a list of UserAchievement
    List<UserAchievement> userAchievementList = new ArrayList<>();
    for (Achievement achievement : relevantAchievements){
      // Get the expression as a resource for the provided achievement
      Resource resource = getResource(achievement);
      // If it is cumulative we should replace/add the value for this task or create it
      if (achievement.isCumulative()) {
        UserAchievement userAchievement = userAchievements.stream()
          .filter(object -> achievement.getId().equals(object.getAchievementRef())).findAny().orElse(null);
        FeatureList calculatedFeatureList = getCalculatedFeatureList(featureValuedContainer, resource);
        if (userAchievement != null) {
          // It exists already -> just update or add to history
          userAchievement.getHistory().put(measureSummary.getTaskName(),calculatedFeatureList.getFeatures().values().stream().mapToInt(o -> o.intValue()).sum());
          userAchievementList.add(userAchievement);
        } else {
          // It ain't here -> create a new UserAchievement
          Map<String, Integer> newHistory = new HashMap<>();
          newHistory.put(measureSummary.getTaskName(),calculatedFeatureList.getFeatures().values().stream().mapToInt(o -> o.intValue()).sum());
          UserAchievement newUserAchievement = new UserAchievement(measureSummary.getUserId(),
            achievement.getId(),
            AchievementState.REVEALED, LocalDateTime.now(), newHistory);
          userAchievementList.add(newUserAchievement);
        }
      }
      else if (achievement.getTaskIdRef().equals(measureSummary.getTaskId())) {
        // The achievement belong to this task -> Create/overwrite userAchievement
        ConfigCreator configCreator = new ConfigCreator();
        // Load config from file system
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
        FeatureList calculatedFeatureList = getCalculatedFeatureList(featureValuedContainer, resource);
        // Printed too many times due to all achivements has the same config atm
        // Eval method for if achievement should be given
        if (calculatedFeatureList.getFeatures().size() > 0) {
          UserAchievement newUserAchievement = new UserAchievement(measureSummary.getUserId(),
            achievement.getId(),
            AchievementState.UNLOCKED, LocalDateTime.now(), null);
          userAchievementList.add(newUserAchievement);
        }
      }
    }
    // Batch save the achieved achievements
    return userAchievementList;
  }

  /**
   * Get the expression as a resource for the provided achievement
   * @param achievement to extract the expression from
   * @return Resource resource for the expression of the achievement
   */
  private Resource getResource(Achievement achievement) {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
    ResourceSet resSet = new ResourceSetImpl();

    // load config from XMI
    Resource configResource = resSet.createResource(URI.createURI("achievementConfig3.xmi"));
    Resource dataResource = resSet.createResource(URI.createURI("achievementData3.xmi"));
    try {
      configResource.load(new ByteArrayInputStream(achievement.getExpression().getData()),null );
      dataResource.load(new ByteArrayInputStream(achievement.getDummyData().getData()),null );
    } catch (IOException e) {
      e.printStackTrace();
    }
    return configResource;
  }

  /**
   * Converts the eObject with the replaced references to a featureList
   * @param featureValuedContainer  The container with the userdata
   * @param resource  the resource loaded from the achievement
   * @return a featureList representation of the fconstructed fv model
   */
  private FeatureList getCalculatedFeatureList(FeatureValuedContainer featureValuedContainer, Resource resource) {
    FeatureList calculatedFeatureList = FvFactory.eINSTANCE.createFeatureList();
    // Replace all references to 'other' in config.xmi with 'featureList'
    for (EObject eObject : resource.getContents()) {
      eObject = replaceReference(eObject, featureValuedContainer);
      if (eObject instanceof FeatureValuedContainer){
        calculatedFeatureList.set(featureValuedContainerToFeatureList((FeatureValuedContainer) eObject), false);
      } else {
        calculatedFeatureList.set(((FeatureValued) eObject).toFeatureList(), false);
      }
    }
    return calculatedFeatureList;
  }

  /**
   * Helper method for converting containers to featureLists
   * @param featureValuedContainer
   * @return
   */
  private FeatureList featureValuedContainerToFeatureList(FeatureValuedContainer featureValuedContainer) {
    FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
    for (FeatureValued featureValued : featureValuedContainer.getFeatureValuedGroups()){
      featureList.set(featureValued.toFeatureList(), false);
    }
    return featureList;
  }

}
