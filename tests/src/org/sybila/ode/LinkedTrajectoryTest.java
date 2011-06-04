package org.sybila.ode;

import org.junit.Test;
import static org.junit.Assert.*;

public class LinkedTrajectoryTest
{

	private static final float[] TEST_POINTS = new float[] {(float)0.0, (float)0.1, (float)0.2, (float) 0.3, (float)0.4, (float)0.5, (float)0.6, (float)0.7};

	private static final float[] TEST_TIMES_1 = new float[] {(float) 0.0, (float)1.0, (float)2.0, (float)3.0};

	private static final float[] TEST_TIMES_2 = new float[] {(float) 4.0, (float)5.0, (float)6.0, (float)7.0};

	private static final int TEST_DIMENSION = 2;

	@Test
	public void testDimension() {
		LinkedTrajectory trajectory = new LinkedTrajectory(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_1, TEST_DIMENSION));
		trajectory.append(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_2, TEST_DIMENSION));
		assertEquals("The dimension doesn't match.", TEST_DIMENSION, trajectory.getDimension());
	}

	@Test
	public void testFirstPoint() {
		LinkedTrajectory trajectory = new LinkedTrajectory(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_1, TEST_DIMENSION));
		trajectory.append(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_2, TEST_DIMENSION));
		Point first = new ArrayPoint(TEST_POINTS, TEST_TIMES_1[0], 0, TEST_DIMENSION);
		assertEquals("The first point doesn't match.", first, trajectory.getFirstPoint());
	}

	@Test
	public void testIterator() {
		LinkedTrajectory trajectory = new LinkedTrajectory(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_1, TEST_DIMENSION));
		trajectory.append(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_2, TEST_DIMENSION));
		int i=0;
		for(Point point : trajectory) {
			float time;
			if (i<TEST_TIMES_1.length) {
				time = TEST_TIMES_1[i];
			}
			else {
				time = TEST_TIMES_2[i - TEST_TIMES_1.length];
			}
			Point p = new ArrayPoint(TEST_POINTS, time, i < TEST_TIMES_1.length ? i * TEST_DIMENSION : (i - TEST_TIMES_1.length) * TEST_DIMENSION, TEST_DIMENSION);
			assertEquals("The retrieved point doesn't match.", p, point);
			i++;
		}
		assertEquals("The length doensn't match.", trajectory.getLength(), i);
	}

	@Test
	public void testLastPoint() {
		LinkedTrajectory trajectory = new LinkedTrajectory(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_1, TEST_DIMENSION));
		trajectory.append(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_2, TEST_DIMENSION));
		Point last = new ArrayPoint(TEST_POINTS, TEST_TIMES_2[TEST_TIMES_2.length - 1], TEST_POINTS.length - TEST_DIMENSION, TEST_DIMENSION);
		assertEquals("The last point doesn't match.", last, trajectory.getLastPoint());
	}

	@Test
	public void testLength() {
		LinkedTrajectory trajectory = new LinkedTrajectory(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_1, TEST_DIMENSION));
		trajectory.append(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_2, TEST_DIMENSION));
		assertEquals("The length doensn't match.", TEST_TIMES_1.length + TEST_TIMES_2.length, trajectory.getLength());
	}

	@Test
	public void testPoint() {
		LinkedTrajectory trajectory = new LinkedTrajectory(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_1, TEST_DIMENSION));
		trajectory.append(new ArrayTrajectory(TEST_POINTS, TEST_TIMES_2, TEST_DIMENSION));
		for(int i=0; i<trajectory.getLength(); i++) {
			float time;
			if (i<TEST_TIMES_1.length) {
				time = TEST_TIMES_1[i];
			}
			else {
				time = TEST_TIMES_2[i - TEST_TIMES_1.length];
			}
			Point point = new ArrayPoint(TEST_POINTS, time, i < TEST_TIMES_1.length ? i * TEST_DIMENSION : (i - TEST_TIMES_1.length) * TEST_DIMENSION, TEST_DIMENSION);
			assertEquals("The retrieved point doesn't match.", point, trajectory.getPoint(i));
		}
	}
}
