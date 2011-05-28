package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Trajectory;

public class CudaSimulationResult {

	private int dimension;
	private int numberOfTrajectories;
	private int[] lengths;
	private int[] returnCodes;
	private float[] times;
	private float[] points;

	public CudaSimulationResult(int numberOfTrajectories, int[] lengths, int[] returnCodes, float[] times, float[] points) {
		if (numberOfTrajectories <= 0) {
			throw new IllegalArgumentException("The number of trejectories has to be a positive number.");
		}
		if (lengths == null) {
			throw new IllegalArgumentException("The parameter [lengths] is NULL.");
		}
		if (lengths.length != numberOfTrajectories) {
			throw new IllegalArgumentException("The length of the array containg lengths of trajectoris doesn't correspond to the number of trajectories.");
		}
		if (returnCodes == null) {
			throw new IllegalArgumentException("The parameter [returnCodes] is NULL.");
		}
		if (returnCodes.length != numberOfTrajectories) {
			throw new IllegalArgumentException("The length of the array containg return codes doesn't correspond to the number of trajectories.");
		}
		if (times == null) {
			throw new IllegalArgumentException("The parameter [times] is NULL.");
		}
		if (points == null) {
			throw new IllegalArgumentException("The parameter [points] is NULL.");
		}
		if (points.length % numberOfTrajectories != 0) {
			throw new IllegalArgumentException("The dimension can not be determied from the size of array [points] and number of trajectories.");
		}
		this.numberOfTrajectories = numberOfTrajectories;
		this.lengths = lengths;
		this.returnCodes = returnCodes;
		this.times = times;
		this.points = points;
		this.dimension = points.length / numberOfTrajectories;
	}

	public Simulation apply(Simulation simulation) {
		List<Trajectory> trajectories = simulation.getTrajectories();
		for (Trajectory trajectory : trajectories) {
			// TODO
		}
		// TODO
		return null;
	}
}
