package org.sybila.ode.system;

public interface Equation
{

	Derivative getDerivative();

	Addition getRightSide();

}
