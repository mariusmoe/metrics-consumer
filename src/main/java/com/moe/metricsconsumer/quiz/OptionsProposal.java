/**
 */
package com.moe.metricsconsumer.quiz;

import org.eclipse.emf.common.util.EList;
import com.moe.metricsconsumer.exercise.Proposal;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Options Proposal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.quiz.OptionsProposal#getIndices <em>Indices</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.quiz.QuizPackage#getOptionsProposal()
 * @model
 * @generated
 */
public interface OptionsProposal extends Proposal<OptionsAnswer> {
	/**
	 * Returns the value of the '<em><b>Indices</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Indices</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Indices</em>' attribute list.
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getOptionsProposal_Indices()
	 * @model
	 * @generated
	 */
	EList<Integer> getIndices();

} // OptionsProposal
