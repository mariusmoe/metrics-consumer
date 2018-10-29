/**
 */
package com.moe.metricsconsumer.quiz;

import com.moe.metricsconsumer.exercise.util.RGB;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Char Style</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.quiz.CharStyle#getItalic <em>Italic</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.CharStyle#getBold <em>Bold</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.CharStyle#getSize <em>Size</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.CharStyle#getFamily <em>Family</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.CharStyle#getForeground <em>Foreground</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.quiz.CharStyle#getBackground <em>Background</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.quiz.QuizPackage#getCharStyle()
 * @model
 * @generated
 */
public interface CharStyle extends EObject {
	/**
	 * Returns the value of the '<em><b>Italic</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Italic</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Italic</em>' attribute.
	 * @see #setItalic(Boolean)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getCharStyle_Italic()
	 * @model
	 * @generated
	 */
	Boolean getItalic();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.CharStyle#getItalic <em>Italic</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Italic</em>' attribute.
	 * @see #getItalic()
	 * @generated
	 */
	void setItalic(Boolean value);

	/**
	 * Returns the value of the '<em><b>Bold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bold</em>' attribute.
	 * @see #setBold(Boolean)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getCharStyle_Bold()
	 * @model
	 * @generated
	 */
	Boolean getBold();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.CharStyle#getBold <em>Bold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bold</em>' attribute.
	 * @see #getBold()
	 * @generated
	 */
	void setBold(Boolean value);

	/**
	 * Returns the value of the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Size</em>' attribute.
	 * @see #setSize(float)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getCharStyle_Size()
	 * @model
	 * @generated
	 */
	float getSize();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.CharStyle#getSize <em>Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Size</em>' attribute.
	 * @see #getSize()
	 * @generated
	 */
	void setSize(float value);

	/**
	 * Returns the value of the '<em><b>Family</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Family</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Family</em>' attribute.
	 * @see #setFamily(String)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getCharStyle_Family()
	 * @model
	 * @generated
	 */
	String getFamily();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.CharStyle#getFamily <em>Family</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Family</em>' attribute.
	 * @see #getFamily()
	 * @generated
	 */
	void setFamily(String value);

	/**
	 * Returns the value of the '<em><b>Foreground</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Foreground</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Foreground</em>' attribute.
	 * @see #setForeground(RGB)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getCharStyle_Foreground()
	 * @model dataType="com.moe.metricsconsumer.quiz.RGB"
	 * @generated
	 */
	RGB getForeground();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.CharStyle#getForeground <em>Foreground</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Foreground</em>' attribute.
	 * @see #getForeground()
	 * @generated
	 */
	void setForeground(RGB value);

	/**
	 * Returns the value of the '<em><b>Background</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Background</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Background</em>' attribute.
	 * @see #setBackground(RGB)
	 * @see com.moe.metricsconsumer.quiz.QuizPackage#getCharStyle_Background()
	 * @model dataType="com.moe.metricsconsumer.quiz.RGB"
	 * @generated
	 */
	RGB getBackground();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.quiz.CharStyle#getBackground <em>Background</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Background</em>' attribute.
	 * @see #getBackground()
	 * @generated
	 */
	void setBackground(RGB value);

} // CharStyle
