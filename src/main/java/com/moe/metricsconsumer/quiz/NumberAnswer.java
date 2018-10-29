/**
 */
package com.moe.metricsconsumer.quiz;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Number Answer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.quiz.NumberAnswer#getValue <em>Value</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.NumberAnswer#getErrorMargin <em>Error Margin</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.NumberAnswer#getRange <em>Range</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.quiz.QuizPackage#getNumberAnswer()
 * @model superTypes="com.moe.metricsconsumer.quiz.SimpleAnswer<org.eclipse.emf.ecore.EDoubleObject>"
 * @generated
 */
public interface NumberAnswer extends SimpleAnswer<Double> {

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Double)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getNumberAnswer_Value()
	 * @model
	 * @generated
	 */
	Double getValue();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.NumberAnswer#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Double value);

	/**
	 * Returns the value of the '<em><b>Error Margin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Error Margin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Error Margin</em>' attribute.
	 * @see #setErrorMargin(double)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getNumberAnswer_ErrorMargin()
	 * @model
	 * @generated
	 */
	double getErrorMargin();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.NumberAnswer#getErrorMargin <em>Error Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Error Margin</em>' attribute.
	 * @see #getErrorMargin()
	 * @generated
	 */
	void setErrorMargin(double value);

	/**
	 * Returns the value of the '<em><b>Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Range</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Range</em>' containment reference.
	 * @see #setRange(NumberRange)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getNumberAnswer_Range()
	 * @model containment="true"
	 * @generated
	 */
	NumberRange getRange();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.NumberAnswer#getRange <em>Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Range</em>' containment reference.
	 * @see #getRange()
	 * @generated
	 */
	void setRange(NumberRange value);
} // NumberAnswer
