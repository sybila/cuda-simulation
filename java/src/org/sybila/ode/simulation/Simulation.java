package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Trajectory;

public interface Simulation
{

	int getDimension();

	float[] getMaxBounds();

	int getMaxNumberOfIterations();

	float[] getMinBounds();

	float[] getSteps();

	float getTargetTime();

	float getTimeStep();

	List<Trajectory> getTrajectories();

	void setTrajectories(List<Trajectory> trajectories);

	SimulationResult simulate();

}
