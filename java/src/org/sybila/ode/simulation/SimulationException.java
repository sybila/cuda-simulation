package org.sybila.ode.simulation;

public class SimulationException extends Exception
{

	public SimulationException(Throwable thrwbl) {
		super(thrwbl);
	}

	public SimulationException(String string, Throwable thrwbl) {
		super(string, thrwbl);
	}

	public SimulationException(String string) {
		super(string);
	}

	public SimulationException() {}

}
