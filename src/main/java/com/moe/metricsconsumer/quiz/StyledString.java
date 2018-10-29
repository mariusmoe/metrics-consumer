/**
 */
package com.moe.metricsconsumer.quiz;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Styled String</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.quiz.StyledString#getCharStyle <em>Char Style</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.StyledString#getEffectiveCharStyle <em>Effective Char Style</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.StyledString#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.StyledString#getStyledString <em>Styled String</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.StyledString#getSuffix <em>Suffix</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStyledString()
 * @model
 * @generated
 */
public interface StyledString extends EObject {
	/**
	 * Returns the value of the '<em><b>Char Style</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Char Style</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Char Style</em>' containment reference.
	 * @see #setCharStyle(CharStyle)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStyledString_CharStyle()
	 * @model containment="true"
	 * @generated
	 */
	CharStyle getCharStyle();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StyledString#getCharStyle <em>Char Style</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Char Style</em>' containment reference.
	 * @see #getCharStyle()
	 * @generated
	 */
	void setCharStyle(CharStyle value);

	/**
	 * Returns the value of the '<em><b>Effective Char Style</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Effective Char Style</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Effective Char Style</em>' reference.
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStyledString_EffectiveCharStyle()
	 * @model transient="true" changeable="false" volatile="true" derived="true"
	 * @generated
	 */
	CharStyle getEffectiveCharStyle();

	/**
	 * Returns the value of the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prefix</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prefix</em>' attribute.
	 * @see #setPrefix(String)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStyledString_Prefix()
	 * @model
	 * @generated
	 */
	String getPrefix();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StyledString#getPrefix <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prefix</em>' attribute.
	 * @see #getPrefix()
	 * @generated
	 */
	void setPrefix(String value);

	/**
	 * Returns the value of the '<em><b>Styled String</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Styled String</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Styled String</em>' containment reference.
	 * @see #setStyledString(StyledString)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStyledString_StyledString()
	 * @model containment="true"
	 * @generated
	 */
	StyledString getStyledString();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StyledString#getStyledString <em>Styled String</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Styled String</em>' containment reference.
	 * @see #getStyledString()
	 * @generated
	 */
	void setStyledString(StyledString value);

	/**
	 * Returns the value of the '<em><b>Suffix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Suffix</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Suffix</em>' attribute.
	 * @see #setSuffix(String)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getStyledString_Suffix()
	 * @model
	 * @generated
	 */
	String getSuffix();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.StyledString#getSuffix <em>Suffix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Suffix</em>' attribute.
	 * @see #getSuffix()
	 * @generated
	 */
	void setSuffix(String value);

} // StyledString
