package org.sybila.ode;

public class PointTrajectory extends AbstractTrajectory implements Trajectory
{

    /** First point of the trajectory. */
    private Point initialPoint;    

    public PointTrajectory(Point initialPoint) {
		super(initialPoint.getDimension(), 1);
        this.initialPoint = initialPoint;
    }

	public Point getPoint(int index) {
		if (index != 0) {
			throw new IllegalArgumentException("The point index is out of the range [0, " + (getLength() - 1) + "]");
		}
		return initialPoint;
	}
	
}