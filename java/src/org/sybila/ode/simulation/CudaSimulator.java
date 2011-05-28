package org.sybila.ode.simulation;

import java.util.List;
import jcuda.utils.KernelLauncher;
import org.sybila.ode.Trajectory;
import org.sybila.ode.system.EquationSystem;

abstract public class CudaSimulator implements Simulator {

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

	public Simulation simulate(Simulation simulation) {
		if (simulation.getDimension() != system.getDimension()) {
			throw new IllegalArgumentException("The dimension of the simulation doesn't correspond to the dimension of the equation system.");
		}
		// prepare data
		List<Trajectory> trajectories = simulation.getTrajectories();
		float[] seeds = new float[trajectories.size() * simulation.getDimension()];
		int index = 0;
		for (Trajectory trajectory : trajectories) {
			System.arraycopy(trajectory.getLastPoint().toArray(), 0, seeds, index, trajectory.getDimension());
			index += trajectory.getDimension();
		}
		getWorkspace().bindNewComputation(seeds, simulation.getSteps());

		// prepare computation
		KernelLauncher launcher = KernelLauncher.load(getKernelFile(), getKernelName());
		int blockDim = 512 / simulation.getDimension();
		int gridDim = (int) Math.ceil(Math.sqrt((float) trajectories.size() / blockDim));
		launcher.setGridSize(gridDim, gridDim);
		launcher.setBlockSize(blockDim, simulation.getDimension(), 1);

		// execute the kernel
		launcher.call(
				simulation.getTargetTime(),
				getWorkspace().getDeviceSeeds(),
				getWorkspace().getDeviceSteps(),
				trajectories.size(),
				getWorkspace().getMaxNumberOfTrajectories(),
				simulation.getDimension(),
				simulation.getMaxRelativeError(),
				simulation.getMaxNumberOfIterations(),
				getWorkspace().getMaxBlockLength(),
				getWorkspace().getDeviceFunctionCoefficients(),
				getWorkspace().getDeviceFunctionCoefficientIndexes(),
				getWorkspace().getDeviceFunctionFactors(),
				getWorkspace().getDeviceFunctionFactorIndexes(),
				getWorkspace().getDeviceResultPoints(),
				getWorkspace().getDeviceResultTimes(),
				getWorkspace().getDeviceResultLengths(),
				getWorkspace().getDeviceReturnCodes());

		// return result
		return getWorkspace().getResult(trajectories.size()).apply(simulation);
	}

	protected final CudaSimulationWorkspace getWorkspace() {
		if (workspace == null) {
			workspace = new CudaSimulationWorkspace(system, maxNumberOfTrajectories, maxBlockLength);
		}
		return workspace;
	}

	abstract protected String getKernelFile();

	abstract protected String getKernelName();
}
