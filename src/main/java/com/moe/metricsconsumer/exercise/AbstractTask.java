/**
 */
package com.moe.metricsconsumer.exercise;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract QA</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getAbstractTask()
 * @model abstract="true"
 * @generated
 */
public interface AbstractTask extends EObject {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Answer getA();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	Question getQ();
} // AbstractQA
