/**
 */
package com.moe.metricsconsumer.quiz.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import com.moe.metricsconsumer.quiz.QuizFactory;
import com.moe.metricsconsumer.quiz.QuizPackage;
import com.moe.metricsconsumer.quiz.SimpleAnswer;
import com.moe.metricsconsumer.quiz.SimpleProposal;

import com.moe.metricsconsumer.exercise.Proposal;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Answer</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class SimpleAnswerImpl<T> extends OptionAnswerImpl implements SimpleAnswer<T> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleAnswerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QuizPackage.Literals.SIMPLE_ANSWER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public T getValue() {
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case QuizPackage.SIMPLE_ANSWER___GET_VALUE:
				return getValue();
		}
		return super.eInvoke(operationID, arguments);
	}

	//
	
	@Override
	public Double accept(Object proposal) {
		return accept(getValue().equals(proposal));
	}

	@Override
	public Proposal<?> createProposal() {
		SimpleProposal<SimpleAnswer<?>> proposal = QuizFactory.eINSTANCE.createSimpleProposal();
		proposal.setAnswer(this);
		return proposal;
	}

} //SimpleAnswerImpl
