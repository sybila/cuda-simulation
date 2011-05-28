package org.sybila.ode.simulation;

import java.util.ArrayList;
import java.util.List;
import org.sybila.ode.ArrayTrajectory;
import org.sybila.ode.LinkedTrajectory;
import org.sybila.ode.Trajectory;

public class CudaSimulationResult {

	private int dimension;
	private int numberOfTrajectories;
	private int[] lengths;
	private int[] returnCodes;
	private float[] times;
	private float[] points;
	private int maxBlockLength;

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
		if (times.length % numberOfTrajectories != 0) {
			throw new IllegalArgumentException("The maximum block length can not be determied from the size of array [times] and number of trajectories.");
		}
		if (points.length % numberOfTrajectories != 0) {
			throw new IllegalArgumentException("The dimension can not be determied from the size of array [points] and number of trajectories.");
		}
		this.numberOfTrajectories = numberOfTrajectories;
		this.lengths = lengths;
		this.returnCodes = returnCodes;
		this.times = times;
		this.points = points;
		this.maxBlockLength = times.length / numberOfTrajectories;
		this.dimension = points.length / (numberOfTrajectories * this.maxBlockLength);
	}

	public Simulation apply(Simulation simulation) {
		List<Trajectory> trajectories = simulation.getTrajectories();
		List<Trajectory> newTrajectories = new ArrayList<Trajectory>(numberOfTrajectories);
		if (trajectories.size() != numberOfTrajectories) {
			throw new IllegalArgumentException("The number of the given trajectories doesn't correspond to the number of simulated trajectories.");
		}
		for(int i=0; i<numberOfTrajectories; i++) {
			Trajectory oldTrajectory = trajectories.get(i);
			Trajectory newTrajectory = createTrajectory(i);
			if (oldTrajectory instanceof LinkedTrajectory) {
				((LinkedTrajectory) oldTrajectory).append(newTrajectory);
				newTrajectories.add(oldTrajectory);
			}
			else {
				LinkedTrajectory auxTrajectory = new LinkedTrajectory(oldTrajectory);
				auxTrajectory.append(newTrajectory);
				newTrajectories.add(oldTrajectory);
			}
		}
		throw new UnsupportedOperationException();
	}

	private SimulationStatus createStatus(int index) {
		return SimulationStatus.fromInt(returnCodes[index]);
	}

	private Trajectory createTrajectory(int index) {
		float[] trajectoryPoints = new float[lengths[index] * dimension];
		System.arraycopy(points, dimension * maxBlockLength * index, trajectoryPoints, 0, dimension * lengths[index]);
		float[] trajectoryTimes = new float[lengths[index]];
		System.arraycopy(times, maxBlockLength * index, trajectoryTimes, 0, lengths[index]);
		return new ArrayTrajectory(points, times, dimension);
	}
}
