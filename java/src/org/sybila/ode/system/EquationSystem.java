package org.sybila.ode.system;

import java.util.Collection;

public interface EquationSystem
{

	int getDimension();

	Collection<Equation> getEquations();

}
