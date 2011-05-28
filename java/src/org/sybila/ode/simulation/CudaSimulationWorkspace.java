package org.sybila.ode.simulation;

import jcuda.CudaException;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUdeviceptr;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaError;
import jcuda.runtime.cudaMemcpyKind;
import org.sybila.ode.system.EquationSystem;

public class CudaSimulationWorkspace {

	private boolean initialized = false;
	private CudaEquationSystem system;
	private int maxNumberOfTrajectories;
	private int maxBlockLength;
	private CUdeviceptr functionCoefficients;
	private CUdeviceptr functionCoefficientIndexes;
	private CUdeviceptr functionFactors;
	private CUdeviceptr functionFactorIndexes;
	private CUdeviceptr times;
	private CUdeviceptr seeds;
	private CUdeviceptr steps;
	private CUdeviceptr resultPoints;
	private CUdeviceptr resultTimes;
	private CUdeviceptr resultLengths;
	private CUdeviceptr returnCodes;

	public CudaSimulationWorkspace(EquationSystem system, int maxNumberOfTrajectories, int maxBlockLength) {
		if (maxNumberOfTrajectories <= 0) {
			throw new IllegalArgumentException("The maximum number of trajectories has be a positive number.");
		}
		if (maxBlockLength <= 0) {
			throw new IllegalArgumentException("The maximum block length has be a positive number.");
		}
		this.maxBlockLength = maxBlockLength;
		this.maxNumberOfTrajectories = maxNumberOfTrajectories;
		this.system = new CudaEquationSystem(system);
	}

	public Pointer getDeviceResultLengths() {
		initPointers();
		return resultLengths;
	}

	public Pointer getDeviceFunctionCoefficients() {
		initPointers();
		return functionCoefficients;
	}

	public Pointer getDeviceFunctionCoefficientIndexes() {
		initPointers();
		return functionCoefficientIndexes;
	}

	public Pointer getDeviceFunctionFactors() {
		initPointers();
		return functionFactors;
	}

	public Pointer getDeviceFunctionFactorIndexes() {
		initPointers();
		return functionFactorIndexes;
	}

	public Pointer getDeviceResultPoints() {
		initPointers();
		return resultPoints;
	}

	public Pointer getDeviceResultTimes() {
		initPointers();
		return resultTimes;
	}

	public Pointer getDeviceReturnCodes() {
		initPointers();
		return returnCodes;
	}

	public Pointer getDeviceSeeds() {
		initPointers();
		return seeds;
	}

	public Pointer getDeviceSteps() {
		initPointers();
		return steps;
	}

	public EquationSystem getEquationSystem() {
		return system;
	}

	public int getMaxNumberOfTrajectories() {
		return maxNumberOfTrajectories;
	}

	public int getMaxBlockLength() {
		return maxBlockLength;
	}

	public CudaSimulationResult getResult(int numberOfTrajectories) {
		if (numberOfTrajectories <= 0 || numberOfTrajectories > getMaxNumberOfTrajectories()) {
			throw new IllegalArgumentException("The number of trajectories [" + numberOfTrajectories + "] is out of the range [1, " + getMaxNumberOfTrajectories() + "].");
		}
		initPointers();
		int[] lengthsHost = new int[numberOfTrajectories];
		int[] returnCodesHost = new int[numberOfTrajectories];
		float[] timesHost = new float[numberOfTrajectories * getMaxBlockLength()];
		float[] pointsHost = new float[numberOfTrajectories * system.getDimension() * getMaxBlockLength()];

		copyDeviceToHost(Pointer.to(lengthsHost), resultLengths, numberOfTrajectories * Sizeof.INT);
		copyDeviceToHost(Pointer.to(returnCodesHost), returnCodes, numberOfTrajectories * Sizeof.INT);
		copyDeviceToHost(Pointer.to(timesHost), resultTimes, lengthsHost.length * Sizeof.FLOAT);
		copyDeviceToHost(Pointer.to(pointsHost), resultPoints, numberOfTrajectories * getMaxBlockLength() * system.getDimension() * Sizeof.FLOAT);

		return new CudaSimulationResult(numberOfTrajectories, lengthsHost, returnCodesHost, timesHost, pointsHost);
	}

	public void bindNewComputation(float[] seeds, float[] times, float[] steps) {
		if (seeds == null) {
			throw new IllegalArgumentException("The parameter [seeds] is NULL.");
		}
		if (seeds.length > getEquationSystem().getDimension() * maxNumberOfTrajectories) {
			throw new IllegalArgumentException("The size of array [seeds] doesn't correspond to the product of number of trajectories and number of dimensions.");
		}
		if (times == null) {
			throw new IllegalArgumentException("The parameter [null] is NULL.");
		}
		if (times.length * system.getDimension() != seeds.length) {
			throw new IllegalArgumentException("The size of array [times] doesn't correspond to the size of array [seeds].");
		}
		if (steps == null) {
			throw new IllegalArgumentException("The parameter [steps] is NULL.");
		}
		if (steps.length != getEquationSystem().getDimension()) {
			throw new IllegalArgumentException("The number of steps for dimension doesn't correspond to the number of dimensions.");
		}
		initPointers();
		copyHostToDevice(this.seeds, Pointer.to(seeds), seeds.length * Sizeof.FLOAT);
		copyHostToDevice(this.steps, Pointer.to(steps), steps.length * Sizeof.FLOAT);
		copyHostToDevice(this.times, Pointer.to(times), times.length * Sizeof.FLOAT);
	}

	public void destroy() {
		JCuda.cudaFree(resultLengths);
		JCuda.cudaFree(functionCoefficientIndexes);
		JCuda.cudaFree(functionCoefficients);
		JCuda.cudaFree(functionFactorIndexes);
		JCuda.cudaFree(functionFactors);
		JCuda.cudaFree(resultPoints);
		JCuda.cudaFree(resultTimes);
		JCuda.cudaFree(returnCodes);
		JCuda.cudaFree(seeds);
		JCuda.cudaFree(steps);
	}

	private void initPointers() {
		if (initialized) {
			return;
		}
		initialized = true;

		seeds = new CUdeviceptr();
		steps = new CUdeviceptr();
		times = new CUdeviceptr();
		resultLengths = new CUdeviceptr();
		resultPoints = new CUdeviceptr();
		resultTimes = new CUdeviceptr();
		returnCodes = new CUdeviceptr();

		functionCoefficientIndexes = new CUdeviceptr();
		functionCoefficients = new CUdeviceptr();
		functionFactorIndexes = new CUdeviceptr();
		functionFactors = new CUdeviceptr();

		JCuda.cudaMalloc(seeds, maxNumberOfTrajectories * system.getDimension() * Sizeof.FLOAT);
		JCuda.cudaMalloc(steps, system.getDimension() * Sizeof.FLOAT);
		JCuda.cudaMalloc(times, maxNumberOfTrajectories);
		JCuda.cudaMalloc(resultPoints, (maxBlockLength + 1) * maxNumberOfTrajectories * system.getDimension() * Sizeof.FLOAT);
		JCuda.cudaMalloc(resultTimes, maxNumberOfTrajectories * maxBlockLength * Sizeof.FLOAT);
		JCuda.cudaMalloc(returnCodes, maxNumberOfTrajectories * Sizeof.INT);
		JCuda.cudaMalloc(resultLengths, maxNumberOfTrajectories * Sizeof.INT);

		JCuda.cudaMalloc(functionCoefficients, system.getCoefficients().length * Sizeof.FLOAT);
		JCuda.cudaMalloc(functionCoefficientIndexes, system.getCoefficientIndexes().length * Sizeof.INT);
		JCuda.cudaMalloc(functionFactors, system.getFactors().length * Sizeof.INT);
		JCuda.cudaMalloc(functionFactorIndexes, system.getFactorIndexes().length * Sizeof.INT);

		copyHostToDevice(functionCoefficients, Pointer.to(system.getCoefficients()), system.getCoefficients().length * Sizeof.FLOAT);
		copyHostToDevice(functionCoefficientIndexes, Pointer.to(system.getCoefficientIndexes()), system.getCoefficientIndexes().length * Sizeof.INT);
		copyHostToDevice(functionFactors, Pointer.to(system.getFactors()), system.getFactors().length * Sizeof.INT);
		copyHostToDevice(functionFactorIndexes, Pointer.to(system.getFactorIndexes()), system.getFactorIndexes().length * Sizeof.INT);
	}

	private void copyHostToDevice(Pointer devicePointer, Pointer hostPointer, int size) {
		JCuda.cudaMemcpy(devicePointer, hostPointer, size, cudaMemcpyKind.cudaMemcpyHostToDevice);
		int error = JCuda.cudaGetLastError();
		if (error != cudaError.cudaSuccess) {
			throw new CudaException(JCuda.cudaGetErrorString(error));
		}
	}

	private void copyDeviceToHost(Pointer hostPointer, Pointer devicePointer, int size) {
		JCuda.cudaMemcpy(hostPointer, devicePointer, size, cudaMemcpyKind.cudaMemcpyDeviceToHost);
		int error = JCuda.cudaGetLastError();
		if (error != cudaError.cudaSuccess) {
			throw new CudaException(JCuda.cudaGetErrorString(error));
		}
	}
}
