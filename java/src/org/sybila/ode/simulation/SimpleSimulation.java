package org.sybila.ode.simulation;

public class SimpleSimulation extends AbstractSimulation implements Simulation
{

	public static final int DEFAULT_MAX_NUMBER_OF_ITERATIONS = 100000;

	private Simulator<Simulation> simulator;

	public SimpleSimulation(Simulator<Simulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds) {
		this(simulator, dimension, timeStep, targetTime, minBounds, maxBounds, new float[dimension], DEFAULT_MAX_NUMBER_OF_ITERATIONS);
	}

	public SimpleSimulation(Simulator<Simulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float[] steps) {
		this(simulator, dimension, timeStep, targetTime, minBounds, maxBounds, steps, DEFAULT_MAX_NUMBER_OF_ITERATIONS);
	}

	public SimpleSimulation(Simulator<Simulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, int maxNumberOfIterations) {
		this(simulator, dimension, timeStep, targetTime, minBounds, maxBounds, new float[dimension], maxNumberOfIterations);
	}

	public SimpleSimulation(Simulator<Simulation> simulator, int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float[] steps, int maxNumberOfIterations) {
		super(dimension, timeStep, targetTime, minBounds, maxBounds, steps, maxNumberOfIterations);
		if (simulator == null) {
			throw new IllegalArgumentException("The parameter [simulator] is NULL.");
		}
		this.simulator = simulator;
	}

	public SimulationResult simulate() {
		return simulator.simulate(this);
	}

}