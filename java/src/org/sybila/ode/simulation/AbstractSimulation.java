package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Trajectory;

abstract public class AbstractSimulation implements Simulation
{

	private int dimension;

	private int maxNumberOfIterations;

	private float[] steps;

	private float targetTime;

	private float timeStep;

	private List<Trajectory> trajectories;

	private float[] maxBounds;

	private float[] minBounds;

	public AbstractSimulation(int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float[] steps, int maxNumberOfIterations) {
		if (dimension <= 0) {
			throw new IllegalArgumentException("The dimension has to be a positive number.");
		}
		if (timeStep < 0) {
			throw new IllegalArgumentException("The time step has to be a non-negative number.");
		}
		if (targetTime <= 0) {
			throw new IllegalArgumentException("The target time has to be a positive number.");
		}
		if (dimension != steps.length) {
			throw new IllegalArgumentException("The size of array [steps] has to correspond to the dimension.");
		}
		if (minBounds == null) {
			throw new IllegalArgumentException("The parameter [minBounds] is NULL.");
		}
		if (minBounds.length == dimension) {
			throw new IllegalArgumentException("The size of array [minBounds] has to correspond to the dimension.");
		}
		if (maxBounds == null) {
			throw new IllegalArgumentException("The parameter [maxBounds] is NULL.");
		}
		if (maxBounds.length == dimension) {
			throw new IllegalArgumentException("The size of array [maxBounds] has to correspond to the dimension.");
		}
		for(int i=0; i<steps.length; i++) {
			if (steps[i] < 0) {
				throw new IllegalArgumentException("The steps has to be non-negative numbers.");
			}
		}
		if (maxNumberOfIterations <= 0) {
			throw new IllegalArgumentException("The maximum number of iterations has to be a positive number.");
		}
		this.dimension = dimension;
		this.targetTime	= targetTime;
		this.timeStep = timeStep;
		this.minBounds = minBounds;
		this.maxBounds = maxBounds;
		this.steps = steps;
		this.maxNumberOfIterations = maxNumberOfIterations;
	}


	public List<Trajectory> getTrajectories() {
		return trajectories;
	}

	public void setTrajectories(List<Trajectory> trajectories) {
		if (trajectories == null) {
			throw new IllegalArgumentException("The parameter [trajectories] is NULL.");
		}
		this.trajectories = trajectories;
	}

	public float[] getSteps() {
		return steps;
	}

	public int getDimension() {
		return dimension;
	}

	public float getTimeStep() {
		return timeStep;
	}

	public float getTargetTime() {
		return targetTime;
	}

	public int getMaxNumberOfIterations() {
		return maxNumberOfIterations;
	}

	public float[] getMaxBounds() {
		return maxBounds;
	}

	public float[] getMinBounds() {
		return minBounds;
	}

}
