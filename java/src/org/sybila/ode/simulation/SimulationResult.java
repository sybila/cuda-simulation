package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Trajectory;

public interface SimulationResult
{

	List<Trajectory> getTrajectories();

	List<SimulationStatus> getStatuses();

}
