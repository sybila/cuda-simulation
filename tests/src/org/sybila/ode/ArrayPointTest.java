package org.sybila.ode;

import org.junit.Test;
import static org.junit.Assert.*;


public class ArrayPointTest
{

	private static float[] testValues = new float[] {(float)1.0, (float)2.0, (float)3.0};

	private static float testTime = (float)0.0001;

	@Test
	public void testDimension() {
		Point point = new ArrayPoint(testValues, testTime);
		assertEquals("The dimension doesn't match.", testValues.length, point.getDimension());
	}

	@Test
	public void testTime() {
		Point point = new ArrayPoint(testValues, testTime);
		assertEquals("The time doesn't match.", testTime, point.getTime(), 0.0001);
	}

	@Test
	public void testToArray() {
		Point point = new ArrayPoint(testValues, testTime);
		float[] returned = point.toArray();
		assertEquals("The legth of returned array doesn't match.", testValues.length, returned.length);
		for(int i=0; i<testValues.length; i++) {
			assertEquals("The returned array doesn't match.", testValues[i], returned[i], 0.0001);
		}
	}

	@Test
	public void testValues() {
		Point point = new ArrayPoint(testValues, testTime);
		for(int i=0; i<point.getDimension(); i++) {
			assertEquals("The returned value doesn't match.", testValues[i], point.getValue(i), 0.0001);
		}
		try {
			point.getValue(-1);
			fail("The method doesn't throw exception when it is called with index out of the range.");
		}
		catch(IllegalArgumentException ex) {}
		try {
			point.getValue(point.getDimension());
			fail("The method doesn't throw exception when it is called with index out of the range.");
		}
		catch(IllegalArgumentException ex) {}
	}

	@Test
	public void testEquals() {
		Point p1 = new ArrayPoint(testValues, testTime);
		Point p2 = new ArrayPoint(testValues, testTime);
		assertEquals(p1, p2);
	}

}
