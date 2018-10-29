/**
 */
package com.moe.metricsconsumer.quiz;

import org.eclipse.emf.common.util.EList;

import com.moe.metricsconsumer.exercise.Answer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Options Answer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.quiz.OptionsAnswer#getOptions <em>Options</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.quiz.QuizPackage#getOptionsAnswer()
 * @model
 * @generated
 */
public interface OptionsAnswer extends Answer {
	/**
	 * Returns the value of the '<em><b>Options</b></em>' containment reference list.
	 * The list contents are of type {@link com.moe.metricsconsumer.quiz.Option}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Options</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Options</em>' containment reference list.
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getOptionsAnswer_Options()
	 * @model containment="true"
	 * @generated
	 */
	EList<Option> getOptions();

} // OptionsAnswer
