package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import no.hal.learning.fv.*;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
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



}
