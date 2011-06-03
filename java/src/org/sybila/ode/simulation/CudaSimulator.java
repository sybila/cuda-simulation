package org.sybila.ode.simulation;

import java.util.List;
import jcuda.utils.KernelLauncher;
import org.sybila.ode.Trajectory;
import org.sybila.ode.system.EquationSystem;

abstract public class CudaSimulator<S extends Simulation> implements Simulator<S> {

	private CudaSimulationWorkspace workspace;
	private int maxNumberOfTrajectories;
	private int maxBlockLength;
	private EquationSystem system;

	public CudaSimulator(EquationSystem system, int maxNumberOfTrajectories, int maxBlockLength) {
		if (system == null) {
			throw new IllegalArgumentException("The parameter [system] is NULL.");
		}
		if (maxNumberOfTrajectories <= 0) {
			throw new IllegalArgumentException("The maximum number of trajectories has to be a positive number.");
		}
		if (maxBlockLength <= 0) {
			throw new IllegalArgumentException("The maximum block length has to be a positive number.");
		}
		this.system = system;
		this.maxNumberOfTrajectories = maxNumberOfTrajectories;
		this.maxBlockLength = maxBlockLength;
	}
	
	public void destroy() {
		getWorkspace().destroy();
	}

	public SimulationResult simulate(S simulation) {
		if (simulation.getDimension() != system.getDimension()) {
			throw new IllegalArgumentException("The dimension of the simulation doesn't correspond to the dimension of the equation system.");
		}
		// prepare data
		List<Trajectory> trajectories = simulation.getTrajectories();
		float[] seeds = new float[trajectories.size() * simulation.getDimension()];
		float[] times = new float[trajectories.size()];
		int index = 0;
		for (Trajectory trajectory : trajectories) {
			System.arraycopy(trajectory.getLastPoint().toArray(), 0, seeds, index, trajectory.getDimension());
			times[index] = trajectory.getLastPoint().getTime();
			index += trajectory.getDimension();
		}
		getWorkspace().bindNewComputation(seeds, times, simulation.getSteps());

		// prepare computation
		KernelLauncher launcher = KernelLauncher.load(getKernelFile(), getKernelName());
		int blockDim = 512 / simulation.getDimension();
		int gridDim = (int) Math.ceil(Math.sqrt((float) trajectories.size() / blockDim));
		launcher.setGridSize(gridDim, gridDim);
		launcher.setBlockSize(blockDim, simulation.getDimension(), 1);

		// execute the kernel
		callLauncher(launcher, simulation, getWorkspace());

		return getWorkspace().getResult(trajectories.size());
	}

	private CudaSimulationWorkspace getWorkspace() {
		if (workspace == null) {
			workspace = new CudaSimulationWorkspace(system, maxNumberOfTrajectories, maxBlockLength);
		}
		return workspace;
	}
	
	abstract protected String getKernelFile();

	abstract protected String getKernelName();

	abstract protected void callLauncher(KernelLauncher launcher, S simulation, CudaSimulationWorkspace workspace);
}