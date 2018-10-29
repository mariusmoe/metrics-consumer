/**
 */
package com.moe.metricsconsumer.exercise;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Proposals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.exercise.ExerciseProposals#getExercise <em>Exercise</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.exercise.ExerciseProposals#getProposals <em>Proposals</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.exercise.ExerciseProposals#getAllProposals <em>All Proposals</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getExerciseProposals()
 * @model
 * @generated
 */
public interface ExerciseProposals extends EObject {
	/**
	 * Returns the value of the '<em><b>Exercise</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exercise</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exercise</em>' reference.
	 * @see #setExercise(Exercise)
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getExerciseProposals_Exercise()
	 * @model
	 * @generated
	 */
	Exercise getExercise();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.exercise.ExerciseProposals#getExercise <em>Exercise</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exercise</em>' reference.
	 * @see #getExercise()
	 * @generated
	 */
	void setExercise(Exercise value);

	/**
	 * Returns the value of the '<em><b>Proposals</b></em>' containment reference list.
	 * The list contents are of type {@link com.moe.metricsconsumer.exercise.ExercisePartProposals}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proposals</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proposals</em>' containment reference list.
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getExerciseProposals_Proposals()
	 * @model containment="true"
	 * @generated
	 */
	EList<ExercisePartProposals> getProposals();

	/**
	 * Returns the value of the '<em><b>All Proposals</b></em>' reference list.
	 * The list contents are of type {@link com.moe.metricsconsumer.exercise.Proposal}&lt;?>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>All Proposals</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>All Proposals</em>' reference list.
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getExerciseProposals_AllProposals()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	EList<Proposal<?>> getAllProposals();

} // QuizProposals
