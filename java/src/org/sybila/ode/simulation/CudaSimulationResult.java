package org.sybila.ode.simulation;

import java.util.ArrayList;
import java.util.List;
import org.sybila.ode.ArrayTrajectory;
import org.sybila.ode.Trajectory;

public class CudaSimulationResult implements SimulationResult
{

	private int dimension;
	private int numberOfTrajectories;
	private int[] lengths;
	private int[] returnCodes;
	private float[] times;
	private float[] points;
	private int maxBlockLength;

	private List<Trajectory> trajectories;
	private List<SimulationStatus> statuses;

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

	public List<Trajectory> getTrajectories() {
		if (trajectories == null) {
			trajectories = createTrajectories();
		}
		return trajectories;
	}

	public List<SimulationStatus> getStatuses() {
		if (statuses == null) {
			statuses = createStatuses();
		}
		return statuses;
	}

	private List<SimulationStatus> createStatuses() {
		List<SimulationStatus> result = new ArrayList<SimulationStatus>(returnCodes.length);
		for(int i=0; i<returnCodes.length; i++) {
			result.add(SimulationStatus.fromInt(returnCodes[i]));
		}
		return result;
	}

	private List<Trajectory> createTrajectories() {
		List<Trajectory> result = new ArrayList<Trajectory>(numberOfTrajectories);
		for(int i=0; i<numberOfTrajectories; i++) {
			float[] trajectoryPoints = new float[lengths[i] * dimension];
			System.arraycopy(points, dimension * maxBlockLength * i, trajectoryPoints, 0, dimension * lengths[i]);
			float[] trajectoryTimes = new float[lengths[i]];
			System.arraycopy(times, maxBlockLength * i, trajectoryTimes, 0, lengths[i]);
			result.add(new ArrayTrajectory(trajectoryPoints, trajectoryTimes, dimension));
		}
		return result;
	}

}
