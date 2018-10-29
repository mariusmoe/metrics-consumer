/**
 */
package com.moe.metricsconsumer.quiz;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>String Answer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.quiz.StringAnswer#getValue <em>Value</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.StringAnswer#getFormat <em>Format</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.StringAnswer#isRegexp <em>Regexp</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.StringAnswer#isIgnoreCase <em>Ignore Case</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStringAnswer()
 * @model superTypes="com.moe.metricsconsumer.quiz.SimpleAnswer<org.eclipse.emf.ecore.EString>"
 * @generated
 */
public interface StringAnswer extends SimpleAnswer<String> {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStringAnswer_Value()
	 * @model
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StringAnswer#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Format</em>' attribute.
	 * @see #setFormat(String)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStringAnswer_Format()
	 * @model
	 * @generated
	 */
	String getFormat();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StringAnswer#getFormat <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format</em>' attribute.
	 * @see #getFormat()
	 * @generated
	 */
	void setFormat(String value);

	/**
	 * Returns the value of the '<em><b>Regexp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regexp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regexp</em>' attribute.
	 * @see #setRegexp(boolean)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStringAnswer_Regexp()
	 * @model
	 * @generated
	 */
	boolean isRegexp();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StringAnswer#isRegexp <em>Regexp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regexp</em>' attribute.
	 * @see #isRegexp()
	 * @generated
	 */
	void setRegexp(boolean value);

	/**
	 * Returns the value of the '<em><b>Ignore Case</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ignore Case</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ignore Case</em>' attribute.
	 * @see #setIgnoreCase(boolean)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStringAnswer_IgnoreCase()
	 * @model
	 * @generated
	 */
	boolean isIgnoreCase();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StringAnswer#isIgnoreCase <em>Ignore Case</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ignore Case</em>' attribute.
	 * @see #isIgnoreCase()
	 * @generated
	 */
	void setIgnoreCase(boolean value);

} // StringAnswer
