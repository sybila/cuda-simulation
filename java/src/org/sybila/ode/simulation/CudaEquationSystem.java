package org.sybila.ode.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.sybila.ode.system.Equation;
import org.sybila.ode.system.EquationSystem;
import org.sybila.ode.system.Multiplication;
import org.sybila.ode.system.Variable;

public class CudaEquationSystem implements EquationSystem
{

	private EquationSystem system;

	private boolean initialized = false;

	private int[] coefficientIndexes;

	private float[] coefficients;

	private int[] factorIndexes;

	private int[] factors;

	public CudaEquationSystem(EquationSystem system) {
		if (system == null) {
			throw new IllegalArgumentException("The parameter [system] is NULL.");
		}
		this.system = system;
	}

	public int[] getCoefficientIndexes() {
		initialize();
		return coefficientIndexes;
	}

	public float[] getCoefficients() {
		initialize();
		return coefficients;
	}

	public int[] getFactorIndexes() {
		initialize();
		return factorIndexes;
	}

	public int[] getFactors() {
		initialize();
		return factors;
	}

	private void initialize() {
		if (initialized) {
			return;
		}
		initialized = true;

		coefficientIndexes				= new int[system.getEquations().size()+1];
		List<Float> fCoefficients		= new ArrayList<Float>();
		List<Integer> fFactorIndexes	= new ArrayList<Integer>();
		List<Integer> fFactors			= new ArrayList<Integer>();

		coefficientIndexes[0]		= 0;
		int lastCoefficientIndex	= 0;
		int lastFactorIndex			= 0;
		for (Equation eq : system.getEquations()) {
			for (Multiplication mult : eq.getRightSide().getAddends()) {
				lastCoefficientIndex++;
				fCoefficients.add(mult.getCoefficient());
				fFactorIndexes.add(lastFactorIndex);
				for(Variable var : mult.getVariables()) {
					fFactors.add(var.getIndex());
					lastFactorIndex++;
				}
			}
		}

		Integer[] aFI = new Integer[fFactorIndexes.size()];
		fFactorIndexes.toArray(aFI);
		Integer[] aF = new Integer[fFactors.size()];
		fFactors.toArray(aF);
		Float[] aC = new Float[fCoefficients.size()];
		fCoefficients.toArray(aC);

		factorIndexes	= ArrayUtils.toPrimitive(aFI);
		factors			= ArrayUtils.toPrimitive(aF);
		coefficients	= ArrayUtils.toPrimitive(aC);
	}

	public Collection<Equation> getEquations() {
		return system.getEquations();
	}

	public int getDimension() {
		return system.getDimension();
	}

}
