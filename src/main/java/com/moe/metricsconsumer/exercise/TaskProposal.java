/**
 */
package com.moe.metricsconsumer.exercise;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task Proposal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.exercise.TaskProposal#getCompletion <em>Completion</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.exercise.TaskProposal#getProof <em>Proof</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.exercise.TaskProposal#getAttemptCount <em>Attempt Count</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.exercise.TaskProposal#getAttempts <em>Attempts</em>}</li>
 *   <li>{@link com.moe.metricsconsumer.exercise.TaskProposal#getPerformedCount <em>Performed Count</em>}</li>
 * </ul>
 *
 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getTaskProposal()
 * @model
 * @generated
 */
public interface TaskProposal<T extends TaskAnswer> extends Proposal<T> {
	/**
	 * Returns the value of the '<em><b>Completion</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Completion</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Completion</em>' attribute.
	 * @see #setCompletion(double)
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getTaskProposal_Completion()
	 * @model default="-1"
	 * @generated
	 */
	double getCompletion();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.exercise.TaskProposal#getCompletion <em>Completion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Completion</em>' attribute.
	 * @see #getCompletion()
	 * @generated
	 */
	void setCompletion(double value);

	/**
	 * Returns the value of the '<em><b>Proof</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Proof</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Proof</em>' attribute.
	 * @see #setProof(String)
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getTaskProposal_Proof()
	 * @model
	 * @generated
	 */
	String getProof();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.exercise.TaskProposal#getProof <em>Proof</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Proof</em>' attribute.
	 * @see #getProof()
	 * @generated
	 */
	void setProof(String value);

	/**
	 * Returns the value of the '<em><b>Attempt Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attempt Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attempt Count</em>' attribute.
	 * @see #setAttemptCount(int)
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getTaskProposal_AttemptCount()
	 * @model
	 * @generated
	 */
	int getAttemptCount();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.exercise.TaskProposal#getAttemptCount <em>Attempt Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attempt Count</em>' attribute.
	 * @see #getAttemptCount()
	 * @generated
	 */
	void setAttemptCount(int value);

	/**
	 * Returns the value of the '<em><b>Attempts</b></em>' containment reference list.
	 * The list contents are of type {@link com.moe.metricsconsumer.exercise.TaskEvent}.
	 * It is bidirectional and its opposite is '{@link com.moe.metricsconsumer.exercise.TaskEvent#getProposal <em>Proposal</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attempts</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attempts</em>' containment reference list.
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getTaskProposal_Attempts()
	 * @see com.moe.metricsconsumer.exercise.TaskEvent#getProposal
	 * @model opposite="proposal" containment="true"
	 * @generated
	 */
	EList<TaskEvent> getAttempts();

	/**
	 * Returns the value of the '<em><b>Performed Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Performed Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Performed Count</em>' attribute.
	 * @see #setPerformedCount(int)
	 * @see com.moe.metricsconsumer.exercise.ExercisePackage#getTaskProposal_PerformedCount()
	 * @model
	 * @generated
	 */
	int getPerformedCount();

	/**
	 * Sets the value of the '{@link com.moe.metricsconsumer.exercise.TaskProposal#getPerformedCount <em>Performed Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Performed Count</em>' attribute.
	 * @see #getPerformedCount()
	 * @generated
	 */
	void setPerformedCount(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getText();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	boolean addTaskEvent(TaskEvent taskEvent);

} // TaskProposal
