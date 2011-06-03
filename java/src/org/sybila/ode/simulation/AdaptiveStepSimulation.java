package org.sybila.ode.simulation;

public interface AdaptiveStepSimulation extends Simulation
{

	float getMaxAbsoluteError();

	float getMaxRelativeError();

}
