/**
 */
package com.moe.metricsconsumer.fv;

import com.moe.metricsconsumer.fv.util.Op1;
import com.moe.metricsconsumer.fv.util.Op2;
import com.moe.metricsconsumer.fv.util.Pred1;
import com.moe.metricsconsumer.fv.util.Pred2;
import org.eclipse.emf.common.util.EMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link no.hal.learning.fv.FeatureList#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @see no.hal.learning.fv.FvPackage#getFeatureList()
 * @model
 * @generated
 */
public interface FeatureList extends FeatureValued {
	/**
	 * Returns the value of the '<em><b>Features</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.Double},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Features</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Features</em>' map.
	 * @see no.hal.learning.fv.FvPackage#getFeatureList_Features()
	 * @model mapType="no.hal.learning.fv.StringToDouble<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EDoubleObject>"
	 * @generated
	 */
	EMap<String, Double> getFeatures();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void set(FeatureValued features, boolean clear);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model opDataType="no.hal.learning.fv.Op1"
	 * @generated
	 */
	void apply(Op1 op);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model opDataType="no.hal.learning.fv.Op2"
	 * @generated
	 */
	void apply(Op2 op, FeatureValued features, boolean swap);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model opDataType="no.hal.learning.fv.Op2"
	 * @generated
	 */
	void apply(Op2 op, double val, boolean swap);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model opDataType="no.hal.learning.fv.Op2"
	 * @generated
	 */
	double reduce(Op2 op, double val, boolean swap);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model predDataType="no.hal.learning.fv.Pred1"
	 * @generated
	 */
	int count(Pred1 pred);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model predDataType="no.hal.learning.fv.Pred2"
	 * @generated
	 */
	int count(Pred2 pred, FeatureValued features, boolean swap);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	int count(FeatureValued features);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void zero(double threshold);

} // FeatureList
