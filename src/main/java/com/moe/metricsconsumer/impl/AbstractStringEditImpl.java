/**
 */
package com.moe.metricsconsumer.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.moe.metricsconsumer.exercise.AbstractStringEdit;
import com.moe.metricsconsumer.exercise.ExercisePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract String Edit</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class AbstractStringEditImpl extends MinimalEObjectImpl.Container implements AbstractStringEdit {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractStringEditImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExercisePackage.Literals.ABSTRACT_STRING_EDIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getString() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Boolean initStringEdit(String string, AbstractStringEdit lastEdit) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
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
			case ExercisePackage.ABSTRACT_STRING_EDIT___GET_STRING:
				return getString();
			case ExercisePackage.ABSTRACT_STRING_EDIT___INIT_STRING_EDIT__STRING_ABSTRACTSTRINGEDIT:
				return initStringEdit((String)arguments.get(0), (AbstractStringEdit)arguments.get(1));
		}
		return super.eInvoke(operationID, arguments);
	}

} //AbstractStringEditImpl
