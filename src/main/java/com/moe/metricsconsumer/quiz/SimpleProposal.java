/**
 */
package com.moe.metricsconsumer.quiz;

import com.moe.metricsconsumer.exercise.Proposal;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Proposal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.quiz.SimpleProposal#getProposal <em>Proposal</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.quiz.QuizPackage#getSimpleProposal()
 * @model
 * @generated
 */
public interface SimpleProposal<A extends SimpleAnswer<?>> extends Proposal<A> {

	/**
	 * Returns the value of the '<em><b>Proposal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proposal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proposal</em>' attribute.
	 * @see #setProposal(Object)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getSimpleProposal_Proposal()
	 * @model
	 * @generated
	 */
	Object getProposal();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.SimpleProposal#getProposal <em>Proposal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Proposal</em>' attribute.
	 * @see #getProposal()
	 * @generated
	 */
	void setProposal(Object value);
} // SimpleProposal
