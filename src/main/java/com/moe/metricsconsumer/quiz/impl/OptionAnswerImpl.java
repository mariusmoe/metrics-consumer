/**
 */
package com.moe.metricsconsumer.quiz.impl;

import org.eclipse.emf.ecore.EClass;

import com.moe.metricsconsumer.quiz.OptionAnswer;
import com.moe.metricsconsumer.quiz.QuizPackage;

import com.moe.metricsconsumer.exercise.impl.AnswerImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Option Answer</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class OptionAnswerImpl extends AnswerImpl implements OptionAnswer {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptionAnswerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QuizPackage.Literals.OPTION_ANSWER;
	}

} //OptionAnswerImpl
