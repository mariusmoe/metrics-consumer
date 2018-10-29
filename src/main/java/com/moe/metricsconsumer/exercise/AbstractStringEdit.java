/**
 */
package com.moe.metricsconsumer.exercise;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract String Edit</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getAbstractStringEdit()
 * @model abstract="true"
 * @generated
 */
public interface AbstractStringEdit extends EObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getString();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	Boolean initStringEdit(String string, AbstractStringEdit lastEdit);

} // AbstractStringEdit
