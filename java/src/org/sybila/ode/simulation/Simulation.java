package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Trajectory;

public interface Simulation
{

	List<SimulationStatus> getStatuses();

	List<Trajectory> getTrajectories();

	float getMaxRelativeError();

	float[] getSteps();

	int getDimension();

	float getTargetTime();

	int getMaxNumberOfIterations();

}
