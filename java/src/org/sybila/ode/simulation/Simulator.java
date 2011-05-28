package org.sybila.ode.simulation;

import java.util.List;
import org.sybila.ode.Point;

/**
 * The classes implementing this interface generate trajectories.
 * 
 * @author Jan Papousek
 */
public interface Simulator
{

	/**
	 * It returns a set of trajectories genereted order to the given parameters.
	 * @param simulation 
	 */
	public Simulation simulate(Simulation simulation);

	public Simulation createNewSimulation(List<Point> seeds, float targetTime, float[] steps, float maxRelError);

	public void destroy();

}
