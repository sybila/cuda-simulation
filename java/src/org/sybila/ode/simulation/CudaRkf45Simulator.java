package org.sybila.ode.simulation;

import java.io.File;
import jcuda.utils.KernelLauncher;
import org.sybila.ode.system.EquationSystem;

public class CudaRkf45Simulator extends CudaSimulator<AdaptiveStepSimulation>
{

	public CudaRkf45Simulator(EquationSystem system, int maxNumberOfTrajectories, int maxBlockLength) {
		super(system, maxNumberOfTrajectories, maxBlockLength);
	}

	public AdaptiveStepSimulation createSimulation(int dimension, float timeStep, float targetTime, float[] minBounds, float[] maxBounds, float maxAbsoluteError, float maxRelativeError, float[] steps) {
		return new SimpleAdaptiveStepSimulation(this, dimension, timeStep, targetTime, minBounds, maxBounds, maxAbsoluteError, maxRelativeError, steps);
	}

	@Override
	protected String getKernelFile() {
		return "c" + File.separator + "build" + File.separator + "num_sim_kernel.cubin";
	}

	@Override
	protected String getKernelName() {
		return "rkf45_kernel";
	}

	@Override
	protected void callLauncher(KernelLauncher launcher, AdaptiveStepSimulation simulation, CudaSimulationWorkspace workspace) {
		launcher.call(
			simulation.getTargetTime(),
			workspace.getDeviceSeeds(),
			workspace.getDeviceSteps(),
			simulation.getTrajectories().size(),
			workspace.getMaxNumberOfTrajectories(),
			simulation.getDimension(),
			simulation.getMaxRelativeError(),
			simulation.getMaxNumberOfIterations(),
			workspace.getMaxBlockLength(),
			workspace.getDeviceFunctionCoefficients(),
			workspace.getDeviceFunctionCoefficientIndexes(),
			workspace.getDeviceFunctionFactors(),
			workspace.getDeviceFunctionFactorIndexes(),
			workspace.getDeviceResultPoints(),
			workspace.getDeviceResultTimes(),
			workspace.getDeviceResultLengths(),
			workspace.getDeviceReturnCodes()
		);
	}

	

}
