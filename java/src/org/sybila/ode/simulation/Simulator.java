package org.sybila.ode.simulation;

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

	public void destroy();

}
