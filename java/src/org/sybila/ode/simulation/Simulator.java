package org.sybila.ode.simulation;

import org.sybila.ode.system.EquationSystem;

/**
 * The classes implementing this interface generate trajectories.
 * 
 * @author Jan Papousek
 */
public interface Simulator<S extends Simulation>
{

	EquationSystem getEquationSystem();

	int getMaxNumberOfTrajectories();

	int getMaxBlockLength();

	/**
	 * It returns a set of trajectories genereted order to the given parameters.
	 * @param simulation 
	 */
	SimulationResult simulate(S simulation);

	void destroy();

}
