package org.sybila.ode;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayTrajectoryTest
{

	private static final float[] TEST_POINTS = new float[] {(float)0.0, (float)0.1, (float)0.2, (float) 0.3, (float)0.4, (float)0.5, (float)0.6, (float)0.7};

	private static final float[] TEST_TIMES = new float[] {(float) 0.0, (float)1.0, (float)2.0, (float)3.0};

	private static final int TEST_DIMENSION = 2;

	@Test
	public void testDimension() {
		Trajectory trajectory = new ArrayTrajectory(TEST_POINTS, TEST_TIMES, TEST_DIMENSION);
		assertEquals("The dimension doesn't match.", TEST_DIMENSION, trajectory.getDimension());
	}

	@Test
	public void testFirstPoint() {
		Trajectory trajectory = new ArrayTrajectory(TEST_POINTS, TEST_TIMES, TEST_DIMENSION);
		Point first = new ArrayPoint(TEST_POINTS, TEST_TIMES[0], 0, TEST_DIMENSION);
		assertEquals("The first point doesn't match.", first, trajectory.getFirstPoint());
	}

	@Test
	public void testIterator() {
		Trajectory trajectory = new ArrayTrajectory(TEST_POINTS, TEST_TIMES, TEST_DIMENSION);
		int i=0;
		for(Point point : trajectory) {
			Point p = new ArrayPoint(TEST_POINTS, TEST_TIMES[i], i * TEST_DIMENSION, TEST_DIMENSION);
			assertEquals("The retrieved point doesn't match.", p, point);
			i++;
		}
		assertEquals("The length doensn't match.", trajectory.getLength(), i);
	}

	@Test
	public void testLastPoint() {
		Trajectory trajectory = new ArrayTrajectory(TEST_POINTS, TEST_TIMES, TEST_DIMENSION);
		Point last = new ArrayPoint(TEST_POINTS, TEST_TIMES[TEST_TIMES.length - 1], TEST_POINTS.length - TEST_DIMENSION, TEST_DIMENSION);
		assertEquals("The last point doesn't match.", last, trajectory.getLastPoint());
	}

	@Test
	public void testLength() {
		Trajectory trajectory = new ArrayTrajectory(TEST_POINTS, TEST_TIMES, TEST_DIMENSION);
		assertEquals("The length doensn't match.", TEST_TIMES.length, trajectory.getLength());
	}

	@Test
	public void testPoint() {
		Trajectory trajectory = new ArrayTrajectory(TEST_POINTS, TEST_TIMES, TEST_DIMENSION);
		for(int i=0; i<trajectory.getLength(); i++) {
			Point point = new ArrayPoint(TEST_POINTS, TEST_TIMES[i], i * TEST_DIMENSION, TEST_DIMENSION);
			assertEquals("The retrieved point doesn't match.", point, trajectory.getPoint(i));
		}
	}

}
