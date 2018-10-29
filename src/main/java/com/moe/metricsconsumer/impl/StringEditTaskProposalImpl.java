/**
 */
package com.moe.metricsconsumer.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.moe.metricsconsumer.exercise.AbstractStringEdit;
import com.moe.metricsconsumer.exercise.AbstractStringEditEvent;
import com.moe.metricsconsumer.exercise.ExercisePackage;
import com.moe.metricsconsumer.exercise.StringEditAnswer;
import com.moe.metricsconsumer.exercise.StringEditTaskProposal;
import com.moe.metricsconsumer.exercise.TaskEvent;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>String Edit Task Proposal</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class StringEditTaskProposalImpl<A extends StringEditAnswer> extends TaskProposalImpl<A> implements StringEditTaskProposal<A> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StringEditTaskProposalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExercisePackage.Literals.STRING_EDIT_TASK_PROPOSAL;
	}
	
	@Override
	protected TaskEvent prepareTaskEvent(TaskEvent taskEvent) {
		taskEvent = (AbstractStringEditEvent) super.prepareTaskEvent(taskEvent);
		if (taskEvent instanceof AbstractStringEditEvent) {
			AbstractStringEditEvent stringEditEvent = (AbstractStringEditEvent) taskEvent; 
			EList<TaskEvent> attempts = getAttempts();
			AbstractStringEditEvent lastEvent = null;
			if (! attempts.isEmpty()) {
				lastEvent = (AbstractStringEditEvent) attempts.get(attempts.size() - 1);
			}
			AbstractStringEdit stringEdit = stringEditEvent.createStringEdit(stringEditEvent.getString(), lastEvent);
			stringEditEvent.setEdit(stringEdit);
		}
		return taskEvent;
	}

} //StringEditTaskProposalImpl
