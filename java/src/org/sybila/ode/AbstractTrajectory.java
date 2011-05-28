package org.sybila.ode;

abstract public class AbstractTrajectory implements Trajectory {

	private int dimension;
	private int length;

	public AbstractTrajectory(int dimension, int length) {
		if (dimension <= 0) {
			throw new IllegalArgumentException("The dimension has to be a positive number.");
		}
		if (length <= 0) {
			throw new IllegalArgumentException("The length has to be a positive number.");
		}
		this.dimension = dimension;
		this.length = length;
	}

	public int getDimension() {
		return dimension;
	}

	public Point getFirstPoint() {
		return getPoint(0);
	}

	public Point getLastPoint() {
		return getPoint(getLength() - 1);
	}

	public int getLength() {
		return length;
	}

	protected final void setLength(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("The length has to be a positive number.");
		}
		this.length = length;
	}
}
