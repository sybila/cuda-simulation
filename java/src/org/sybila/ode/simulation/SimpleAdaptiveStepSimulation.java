package org.sybila.ode.simulation;

public class SimpleAdaptiveStepSimulation extends AbstractSimulation implements AdaptiveStepSimulation
{

	public static final int DEFAULT_MAX_NUMBER_OF_ITERATIONS = 100000;

	private float maxAbsoluteError;

	private float maxRelativeError;

	private Simulator<AdaptiveStepSimulation> simulator;

	public SimpleAdaptiveStepSimulation(Simulator<AdaptiveStepSimulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float maxAbsoluteError, float maxRelativeError) {
		this(simulator, dimension, timeStep, targetTime, minBounds, maxBounds, maxAbsoluteError, maxRelativeError, new float[dimension], DEFAULT_MAX_NUMBER_OF_ITERATIONS);
	}

	public SimpleAdaptiveStepSimulation(Simulator<AdaptiveStepSimulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float maxAbsoluteError, float maxRelativeError, float[] steps) {
		this(simulator, dimension, timeStep, targetTime, minBounds, maxBounds, maxAbsoluteError, maxRelativeError, steps, DEFAULT_MAX_NUMBER_OF_ITERATIONS);
	}

	public SimpleAdaptiveStepSimulation(Simulator<AdaptiveStepSimulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float maxAbsoluteError, float maxRelativeError, int maxNumberOfIterations) {
		this(simulator, dimension, timeStep, targetTime, minBounds, maxBounds, maxAbsoluteError, maxRelativeError, new float[dimension], maxNumberOfIterations);
	}

	public SimpleAdaptiveStepSimulation(Simulator<AdaptiveStepSimulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float maxAbsoluteError, float maxRelativeError, float[] steps, int maxNumberOfIterations) {
		super(dimension, timeStep, targetTime, minBounds, maxBounds, steps, maxNumberOfIterations);
		if (simulator == null) {
			throw new IllegalArgumentException("The parameter [simulator] is NULL.");
		}
		if (maxAbsoluteError < 0) {
			throw new IllegalArgumentException("The maximum absolute error has to be a non-negative number.");
		}
		if (maxRelativeError < 0) {
			throw new IllegalArgumentException("The maximum relative error has to be a non-negative number.");
		}
		this.simulator = simulator;
		this.maxAbsoluteError = maxAbsoluteError;
		this.maxRelativeError = maxRelativeError;
	}

	public float getMaxAbsoluteError() {
		return maxAbsoluteError;
	}

	public float getMaxRelativeError() {
		return maxRelativeError;
	}

	public SimulationResult simulate() throws SimulationException {
		return simulator.simulate(this);
	}

}
