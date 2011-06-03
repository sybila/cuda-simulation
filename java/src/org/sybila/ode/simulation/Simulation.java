package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Trajectory;

public interface Simulation
{

	List<Trajectory> getTrajectories();
	
	void setTrajectories(List<Trajectory> trajectories);

	float[] getSteps();

	int getDimension();

	float getTimeStep();

	float getTargetTime();

	int getMaxNumberOfIterations();

	SimulationResult simulate();

}
