package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Trajectory;

/**
 * The simulation descriptor
 */
public interface Simulation {

	/**
	 * @return The number of dimensions of points on all trajectories to be
	 * simulated.
	 */
	int getDimension();

	/**
	 * @return The upper bounds on all space-time dimensions.
	 * The simulation of a trajectory stops if this value is reached.
	 */
	float[] getMaxBounds();

	/**
	 * @return The amount of work to do inside the simulate method
	 * during the simulation of each trajectory.
	 */
	int getMaxNumberOfIterations();

	/**
	 * @return The lower bounds on all space-time dimensions.
	 * The simulation of a trajectory stops if this value is reached.
	 */
	float[] getMinBounds();

    /**
     * @return maximum distance of two consecutive points
     * of one trajectory in all dimensions. If some dimension is not
     * to be checked the distance is set to 0.
     */
	float[] getSteps();

	/**
	 * @return The maximum time. The simulation of a trajectory stops if this time is
	 * reached.
	 */
	float getTargetTime();

	/**
	 * @return The maximum time distance of two two consecutive points
     * of one trajectory.
	 */
	float getTimeStep();

	/**
	 * @return Trajectories contained by the simulation
	 */
	List<Trajectory> getTrajectories();

	/**
	 * @param trajectories The trajectories to be prolonged by the
     * simulate() method.
	 */
	void setTrajectories(List<Trajectory> trajectories);

	SimulationResult simulate() throws SimulationException;
}
