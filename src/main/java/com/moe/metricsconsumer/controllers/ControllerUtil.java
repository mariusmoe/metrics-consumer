package com.moe.metricsconsumer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moe.metricsconsumer.models.measureSummary.Measure;
import com.moe.metricsconsumer.models.measureSummary.MeasureSummary;
import com.moe.metricsconsumer.models.measureSummary.SpecificMeasure;
import no.hal.learning.fv.FeatureList;
import no.hal.learning.fv.FeatureValuedContainer;
import no.hal.learning.fv.FvFactory;
import no.hal.learning.fv.MetaDataFeatureValued;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Iterator;


public class ControllerUtil {



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
          return (MetaDataFeatureValued) containerIt;
        }
      }
    }
    // Throw error instead?
    return null;
  }

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


}
