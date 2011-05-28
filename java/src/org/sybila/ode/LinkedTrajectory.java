package org.sybila.ode;



import java.util.ArrayList;
import java.util.List;

public class LinkedTrajectory extends AbstractTrajectory
{

	private List<Trajectory> trajectories = new ArrayList<Trajectory>();

	public LinkedTrajectory(Trajectory trajectory) {
		super(trajectory.getDimension(), trajectory.getLength());
		trajectories.add(trajectory);
	}

	public void append(Trajectory trajectory) {
		if (trajectory == null) {
			throw new IllegalArgumentException("The parameter [trajectory] is NULL.");
		}
		if (trajectory.getFirstPoint().getTime() < trajectories.get(trajectories.size()).getLastPoint().getTime()) {
			throw new IllegalArgumentException("The time of the first point of the given trajectory is lower than the time of the last point of original trajectory.");
		}
		setLength(getLength() + trajectory.getLength());
		trajectories.add(trajectory);
	}

	@Override
	public Point getFirstPoint() {
		return trajectories.get(0).getFirstPoint();
	}

	@Override
	public Point getLastPoint() {
		return trajectories.get(trajectories.size() - 1).getLastPoint();
	}

	public Point getPoint(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("The index has to be non negative number.");
		}
		if (index >= getLength()) {
			throw new IllegalArgumentException("The index has to be lower than trajectory length.");
		}
		// TODO: binary halfing
		int length = 0;
		for(Trajectory t : trajectories) {
			if (index < length + t.getLength()) {
				index -= length;
				return t.getPoint(index);
			}
			length += t.getLength();
		}
		return null;
	}


}
