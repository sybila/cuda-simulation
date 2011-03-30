package org.sybila.ode.benchmark;

import jcuda.Sizeof;
import jcuda.runtime.cudaDeviceProp;
import org.sybila.ode.MultiAffineFunction;
import org.sybila.ode.SimulationLauncher;
import org.sybila.ode.cpu.CpuEulerSimulationLauncher;
import org.sybila.ode.cpu.CpuRkf45SimulationLauncher;
import org.sybila.ode.cuda.CudaRkf45SimulationLauncher;

public class Main {

	private static final int DIMENSION = 10;
	private static final int MAX_NUMBER_OF_SIMULATIONS = 500;
	private static final int MIN_NUMBER_OF_SIMULATIONS = 500;
	private static final int NUMBER_OF_SIMULATIONS_STEP = 500;
	private static final int SIMULATION_LENGTH = 10000;

	public static void main(String[] args) {
		header();
		MultiAffineFunction function = SimulationBenchmark.createSimpleFunction(DIMENSION);

		SimulationLauncher[] launchers = new SimulationLauncher[]{
//			new CpuEulerSimulationLauncher(DIMENSION, MAX_NUMBER_OF_SIMULATIONS, SIMULATION_LENGTH, function),
//			new CpuRkf45SimulationLauncher(DIMENSION, MAX_NUMBER_OF_SIMULATIONS, SIMULATION_LENGTH, function),
			new CudaRkf45SimulationLauncher(DIMENSION, MAX_NUMBER_OF_SIMULATIONS,SIMULATION_LENGTH, function),
		};

		for (int numberOfSimulations = MIN_NUMBER_OF_SIMULATIONS; numberOfSimulations <= MAX_NUMBER_OF_SIMULATIONS; numberOfSimulations += NUMBER_OF_SIMULATIONS_STEP) {
			line();
			System.out.println("  NUMBER OF SIMULATIONS: " + numberOfSimulations);
			line();
			SimulationBenchmark benchmark = new SimulationBenchmark(DIMENSION, numberOfSimulations, SIMULATION_LENGTH);
			for (SimulationLauncher launcher : launchers) {
				result(launcher, benchmark.bench(launcher));
				System.gc();
			}
		}
	}

	private static void header() {
		line();
		System.out.println("  DIMENSION:                     " + DIMENSION);
		System.out.println("  SIMULATION LENGTH:             " + SIMULATION_LENGTH);
		System.out.println("  MINIMUM NUMBER OF SIMULATIONS: " + MIN_NUMBER_OF_SIMULATIONS);
		System.out.println("  MAXIMUM NUMBER OF SIMULATIONS: " + MAX_NUMBER_OF_SIMULATIONS);
		System.out.println("  NUMBER OF SIMULATIONS STEP:    " + NUMBER_OF_SIMULATIONS_STEP);
		cudaDeviceProp properties = new cudaDeviceProp();
		jcuda.runtime.JCuda.cudaGetDeviceProperties(properties, 0);
		System.out.println("  SIZE OF GLOBAL MEMORY:         " + properties.totalGlobalMem / (1000 * 1000 * Sizeof.FLOAT) + " Mfloats");
		System.out.println("  MAXIMUM MEMORY PITCH:          " + properties.memPitch / (1000 * 1000 * Sizeof.FLOAT) + " Mfloats");
		System.out.println("  KERNEL TIMEOUT:                " + (properties.kernelExecTimeoutEnabled > 1 ? "YES" : "NO"));
		line();
	}

	private static void line() {
		System.out.println("--------------------------------------------------------------------------------");
	}

	private static void result(SimulationLauncher launcher, SimulationBenchmarkResult benchmarkResult) {
		System.out.print("  ");
		String name = launcher.getClass().getSimpleName();
		System.out.print(name);
		int length1 = name.length();
		StringBuilder tabs1 = new StringBuilder();
		for (int i = 0; i < 35 - length1; i++) {
			tabs1.append(" ");
		}
		int length2 = new Integer((int) (benchmarkResult.getTime() / 1000000)).toString().length();
		StringBuilder tabs2 = new StringBuilder();
		for (int i = 0; i < 10 - length2; i++) {
			tabs2.append(" ");
		}
		System.out.print(tabs1);
		System.out.print(benchmarkResult.getTime() / 1000000);
		System.out.print(tabs2);
		System.out.print(benchmarkResult.getAvgSimulationLength());
		System.out.println();
	}
}
