/**
 */
package com.moe.metricsconsumer.fv.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import com.moe.metricsconsumer.fv.FeatureList;
import com.moe.metricsconsumer.fv.FeatureValued;
import com.moe.metricsconsumer.fv.FvFactory;
import com.moe.metricsconsumer.fv.FvPackage;
import com.moe.metricsconsumer.fv.util.Op1;
import com.moe.metricsconsumer.fv.util.Op2;
import com.moe.metricsconsumer.fv.util.Pred1;
import com.moe.metricsconsumer.fv.util.Pred2;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.moe.metricsconsumer.fv.impl.FeatureListImpl#getFeatures <em>Features</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureListImpl extends MinimalEObjectImpl.Container implements FeatureList {
	/**
	 * The cached value of the '{@link #getFeatures() <em>Features</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatures()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Double> features;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FeatureListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FvPackage.Literals.FEATURE_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, Double> getFeatures() {
		if (features == null) {
			features = new EcoreEMap<String,Double>(FvPackage.Literals.STRING_TO_DOUBLE, StringToDoubleImpl.class, this, FvPackage.FEATURE_LIST__FEATURES);
		}
		return features;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void set(FeatureValued features, boolean clear) {
		EMap<String, Double> map = this.getFeatures();
		if (clear) {
			map.clear();
		}
		for (String featureName : features.getFeatureNames()) {
			map.put(featureName, features.getFeatureValue(featureName));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void apply(Op1 op) {
		for (String featureName : getFeatureNames()) {
			double val = getFeatureValue(featureName);
			this.features.put(featureName, op.apply(val));
		}
	}

	static double apply(double val1, Op2 op, double val2, boolean swap) {
		if (swap) {
			double val = val1;
			val1 = val2;
			val2 = val;
		}
		return op.apply(val1, val2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void apply(Op2 op, FeatureValued features, boolean swap) {
		for (String featureName : getFeatureNames()) {
			double val1 = getFeatureValue(featureName);
			double val2 = (features.hasFeature(featureName) ? features.getFeatureValue(featureName) : 0.0);
			this.features.put(featureName, apply(val1, op, val2, swap));
		}
		for (String featureName : features.getFeatureNames()) {
			if (! hasFeature(featureName)) {
				double val1 = 0.0;
				double val2 = features.getFeatureValue(featureName);				
				this.features.put(featureName, apply(val1, op, val2, swap));
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void apply(Op2 op, double val, boolean swap) {
		for (String featureName : getFeatureNames()) {
			double val1 = getFeatureValue(featureName);
			double val2 = val;
			this.features.put(featureName, apply(val1, op, val2, swap));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double reduce(Op2 op, double val, boolean swap) {
		for (Map.Entry<String, Double> entry: getFeatures()) {
			double val2 = entry.getValue();
			val = reduce(val, op, val2, swap);
		}
		return val;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int count(Pred1 pred) {
		int count = 0;
		for (Map.Entry<String, Double> entry: getFeatures()) {
			double val = entry.getValue();
			if (pred.test(val)) {
				count++;
			}
		}
		return count;
	}

	static double reduce(double val1, Op2 op, double val2, boolean swap) {
		if (swap) {
			double val = val1;
			val1 = val2;
			val2 = val;
		}
		return op.apply(val1, val2);
	}

	static boolean test(double val1, Pred2 pred, double val2, boolean swap) {
		if (swap) {
			double val = val1;
			val1 = val2;
			val2 = val;
		}
		return pred.test(val1, val2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int count(Pred2 pred, FeatureValued features, boolean swap) {
		int count = 0;
		for (String featureName : getFeatureNames()) {
			double val1 = getFeatureValue(featureName);
			double val2 = (features.hasFeature(featureName) ? features.getFeatureValue(featureName) : 0.0);
			if (test(val1, pred, val2, swap)) {
				count++;
			}
		}
		for (String featureName : features.getFeatureNames()) {
			if (! hasFeature(featureName)) {
				double val1 = 0.0;
				double val2 = features.getFeatureValue(featureName);				
				if (test(val1, pred, val2, swap)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public int count(FeatureValued features) {
		int count = getFeatureNames().size();
		for (String featureName : features.getFeatureNames()) {
			if (! hasFeature(featureName)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public FeatureList toFeatureList() {
		return toFeatureList(this);
	}

	static FeatureList toFeatureList(FeatureValued features) {
		FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
		featureList.set(features, true);
		return featureList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void zero(double threshold) {
		for (String featureName : getFeatureNames()) {
			double val = getFeatureValue(featureName);
			if (val <= threshold) {
				this.features.put(featureName, 0.0);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<String> getFeatureNames() {
		return new BasicEList<String>(getFeatures().keySet());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public double getFeatureValue(String featureName) {
		return features.get(featureName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean hasFeature(String featureName) {
		return features.containsKey(featureName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FvPackage.FEATURE_LIST__FEATURES:
				return ((InternalEList<?>)getFeatures()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FvPackage.FEATURE_LIST__FEATURES:
				if (coreType) return getFeatures();
				else return getFeatures().map();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FvPackage.FEATURE_LIST__FEATURES:
				((EStructuralFeature.Setting)getFeatures()).set(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case FvPackage.FEATURE_LIST__FEATURES:
				getFeatures().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FvPackage.FEATURE_LIST__FEATURES:
				return features != null && !features.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case FvPackage.FEATURE_LIST___SET__FEATUREVALUED_BOOLEAN:
				set((FeatureValued)arguments.get(0), (Boolean)arguments.get(1));
				return null;
			case FvPackage.FEATURE_LIST___APPLY__OP1:
				apply((Op1)arguments.get(0));
				return null;
			case FvPackage.FEATURE_LIST___APPLY__OP2_FEATUREVALUED_BOOLEAN:
				apply((Op2)arguments.get(0), (FeatureValued)arguments.get(1), (Boolean)arguments.get(2));
				return null;
			case FvPackage.FEATURE_LIST___APPLY__OP2_DOUBLE_BOOLEAN:
				apply((Op2)arguments.get(0), (Double)arguments.get(1), (Boolean)arguments.get(2));
				return null;
			case FvPackage.FEATURE_LIST___REDUCE__OP2_DOUBLE_BOOLEAN:
				return reduce((Op2)arguments.get(0), (Double)arguments.get(1), (Boolean)arguments.get(2));
			case FvPackage.FEATURE_LIST___COUNT__PRED1:
				return count((Pred1)arguments.get(0));
			case FvPackage.FEATURE_LIST___COUNT__PRED2_FEATUREVALUED_BOOLEAN:
				return count((Pred2)arguments.get(0), (FeatureValued)arguments.get(1), (Boolean)arguments.get(2));
			case FvPackage.FEATURE_LIST___COUNT__FEATUREVALUED:
				return count((FeatureValued)arguments.get(0));
			case FvPackage.FEATURE_LIST___ZERO__DOUBLE:
				zero((Double)arguments.get(0));
				return null;
			case FvPackage.FEATURE_LIST___GET_FEATURE_NAMES:
				return getFeatureNames();
			case FvPackage.FEATURE_LIST___GET_FEATURE_VALUE__STRING:
				return getFeatureValue((String)arguments.get(0));
			case FvPackage.FEATURE_LIST___HAS_FEATURE__STRING:
				return hasFeature((String)arguments.get(0));
			case FvPackage.FEATURE_LIST___TO_FEATURE_LIST:
				return toFeatureList();
		}
		return super.eInvoke(operationID, arguments);
	}

	//
	
	public static Collection<Double> getFeatureValues(FeatureValued fv) {
		if (fv instanceof FeatureList) {
			return ((FeatureList) fv).getFeatures().values();
		}
		EList<String> featureNames = fv.getFeatureNames();
		Double[] values = new Double[featureNames.size()];
		int num = 0;
		for (String featureName : featureNames) {
			values[num++] = fv.getFeatureValue(featureName);
		}
		return Arrays.asList(values);
	}

	public static double[] getFeatureDoubles(FeatureValued fv) {
		EList<String> featureNames = fv.getFeatureNames();
		double[] values = new double[featureNames.size()];
		int num = 0;
		for (String featureName : featureNames) {
			values[num++] = fv.getFeatureValue(featureName);
		}
		return values;
	}

	public static FeatureList valueOf(Object... namesValues) {
		FeatureList featureList = FvFactory.eINSTANCE.createFeatureList();
		for (int i = 0; i < namesValues.length; i += 2) {
			Object value = namesValues[i + 1];
			if (! (value instanceof Number)) {
				value = Double.valueOf(value.toString());
			}
			String featureName = namesValues[i].toString();
			int nameStart = -1;
			for (int pos = 0; pos < featureName.length(); pos++) {
				if (Character.isLetter(featureName.charAt(pos))) {
					nameStart = pos;
					break;
				}
			}
			if (nameStart < 0) {
				throw new IllegalArgumentException("Feature name must contain a letter: " + featureName);
			}
			if (nameStart > 0) {
				String quote = featureName.substring(0, nameStart);
				if (! featureName.endsWith(quote)) {
					throw new IllegalArgumentException("Feature name must start and end with the same quotes (non-letter characters)");
				}
				featureName = featureName.substring(nameStart, featureName.length() - quote.length());
			}
			featureList.getFeatures().put(featureName, ((Number) value).doubleValue());
		}
		return featureList;
	}
	
	public static FeatureList valueOf(String namesValues, String prefix, String nameQuote, String nameValueSeparator, String featureSeparator, String suffix) {
		if (prefix != null && namesValues.startsWith(prefix)) {
			namesValues = namesValues.substring(prefix.length());
		}
		if (suffix != null && namesValues.endsWith(suffix)) {
			namesValues = namesValues.substring(0, namesValues.length() - suffix.length());
		}
		String[] features = namesValues.split(Pattern.quote(featureSeparator));
		Collection<Object> args = new ArrayList<Object>(features.length * 2);
		for (int i = 0; i < features.length; i++) {
			String feature = features[i];
			int pos = feature.indexOf(nameValueSeparator);
			if (pos > 0) {
				String featureName = feature.substring(0, pos);
				if (nameQuote != null) {
					if (featureName.startsWith(nameQuote)) {
						featureName = featureName.substring(nameQuote.length());
					}
					if (featureName.endsWith(nameQuote)) {
						featureName = featureName.substring(0, featureName.length() - nameQuote.length());
					}
				}
				try {
					Double value = Double.valueOf(feature.substring(pos + nameValueSeparator.length()));
					args.add(featureName);
					args.add(value);
				} catch (NumberFormatException e) {
				}
			}
		}
		return valueOf(args.toArray());
	}
	
	public static FeatureList valueOf(String namesValues) {
		return valueOf(namesValues, "[", null, ":", " ", "]");
	}

	public static String toString(FeatureValued features, String prefix, String nameQuote, String nameValueSeparator, String featureSeparator, String suffix) {
		StringBuilder buffer = new StringBuilder();
		if (prefix != null) {
			buffer.append(prefix);
		}
		for (String name : features.getFeatureNames()) {
			if (buffer.length() > (prefix != null ? prefix.length() : 0) && featureSeparator != null) {
				buffer.append(featureSeparator);
			}
			if (nameQuote != null) {
				buffer.append(nameQuote);
				buffer.append(name);
				buffer.append(nameQuote);
			} else {
				buffer.append(name);
			}
			if (nameValueSeparator != null) {
				buffer.append(nameValueSeparator);
			}
			buffer.append(features.getFeatureValue(name));
		}
		if (suffix != null) {
			buffer.append(suffix);
		}
		return buffer.toString();
	}
	
	public static String toString(FeatureValued features) {
		return toString(features, "[", null, ":", " ", "]");
	}

	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();
		return toString(this);
	}
} //FeatureListImpl
