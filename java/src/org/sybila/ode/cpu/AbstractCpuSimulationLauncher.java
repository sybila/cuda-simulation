package org.sybila.ode.cpu;

import org.sybila.ode.AbstractSimulationLauncher;
import org.sybila.ode.MultiAffineFunction;

abstract public class AbstractCpuSimulationLauncher extends AbstractSimulationLauncher
{

	public AbstractCpuSimulationLauncher(int dimension, int maxNumberOfSimulations, int maxSimulationSize, MultiAffineFunction function) {
		super(dimension, maxNumberOfSimulations, maxSimulationSize, function);
	}

	public AbstractCpuSimulationLauncher(int dimension, int maxNumberOfSimulations, int maxSimulationSize, MultiAffineFunction function, float minAbsDivergency, float maxAbsDivergency, float minRelDivergency, float maxRelDivergency) {
		super(dimension, maxNumberOfSimulations, maxSimulationSize, function, minAbsDivergency, maxAbsDivergency, minRelDivergency, maxRelDivergency);
	}

	public SimulationResult launch(
			float time,
			float timeStep,
			float targetTime,
			float[] vectors,
			int numberOfSimulations) {

		org.sybila.ode.Simulation[] simulations = new Simulation[numberOfSimulations];
		for (int i = 0; i < numberOfSimulations; i++) {
			simulations[i] = simulate(i, time, timeStep, targetTime, vectors, numberOfSimulations);
		}
		return new org.sybila.ode.cpu.SimulationResult(simulations);
	}

	abstract protected org.sybila.ode.Simulation simulate(
			int simulationId,
			float time,
			float timeStep,
			float targetTime,
			float[] vectors,
			int numberOfSimulations);
}
