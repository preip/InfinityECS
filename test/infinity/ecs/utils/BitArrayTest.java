package infinity.ecs.utils;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

public class BitArrayTest {

    private BitArray _ba1, _ba2;

    @Before
    public void setUp() {
        _ba1 = new BitArray();
        _ba2 = new BitArray();
    }

    @After
    public void tearDown() {
        _ba1 = null;
        _ba2 = null;
    }

    @Test
    public void testCapacityAndResize() {

        // first get the capacity, which should be 32
        int cap = _ba1.getCapacity();
        assertTrue("Initial Capacity should be 32 bit", cap == 32);

        // try to set the 32th bit to false, which should leave the capacity unchanged
        _ba1.set(32, false);
        assertTrue("BitArray Capacity must be unchanged", cap == _ba1.getCapacity());

        // try to set the 32th bit to true, which should change the capacity
        _ba1.set(32, true);
        assertTrue("BitArray Capacity must be unchanged", cap != _ba1.getCapacity());
        // the capacity should also be 64 bit
        assertTrue("BitArray Capacity must be 64 bit", _ba1.getCapacity() == 64);

        // try to set the 127th bit to true, which should change the capacity to 128
        _ba1.set(127, true);
        assertTrue("BitArray Capacity must be unchanged", _ba1.getCapacity() == 128);
    }

    @Test
    public void testGetAndSetFor128Bit() {
        // first set all bits to true and check their state
        for (int i = 30; i < 128; i++) {
            _ba1.set(i, true);
            assertEquals("Bit " + i + "must be true", true, _ba1.get(i));
        }
        // next set all bits to false and check their state again
        for (int i = 0; i < 128; i++) {
            _ba1.set(i, false);
            assertEquals("Bit " + i + "must be false", false, _ba1.get(i));
        }
        assertTrue(true);
    }
    
    @Test
    public void testContains() {
    	_ba2.set(2, true);
    	assertFalse("BitArray should not contain other BitArray, when the other array has different bits set", _ba1.contains(_ba2));
    	
    	_ba1.set(2, true);
    	assertTrue("BitArray should contain other BitArray, when all bits are the same", _ba1.contains(_ba2));
    	_ba1.set(5, true);
    	assertTrue("BitArray should contain other BitArray, when the first array has more bits set", _ba1.contains(_ba2));
    	
    	_ba2.set(5, true);
    	_ba2.set(35, true);
    	assertFalse("BitArray should not contain other BitArray, when the other array is longer than the first array and has bits set in that range", _ba1.contains(_ba2));
    	
    	_ba2.set(35, false);
    	assertTrue("BitArray should contain other BitArray, when the other array is longer than the first array but has no bits set in that range", _ba1.contains(_ba2));
    	
    	_ba1.set(66, false);
    	assertTrue("BitArray should contain other BitArray, when the fist array is longer than the second, but all other fields match", _ba1.contains(_ba2));
    }
    
    @Test
    public void testIntersects() {
    	assertFalse("Two Bit Arrays can't intersect if both arrays are empty", _ba1.intersects(_ba2));
    	
    	_ba1.set(2, true);
    	_ba2.set(2, true);
    	assertTrue("Two Bit Arrays intersect if both arrays are equal", _ba1.intersects(_ba2));
    	
    	_ba1.set(6, true);
    	_ba1.set(33, true);
    	_ba2.set(9, true);
    	_ba2.set(65, true);
    	assertTrue("Two Bit Arrays intersect if they have at least one common bit", _ba1.intersects(_ba2));
    	
    	_ba1.set(2, false);
    	assertFalse("Two Bit Arrays don't intersect if they haven't at least one common bit", _ba1.intersects(_ba2));
    }

    @Test
    public void testEquals() {
        _ba1.set(31, true);

        // first check for arbitrary objects and null
        assertFalse("BitArray should not be equal null", _ba1.equals(null));
        assertFalse("BitArray should not be equal the number 5", _ba1.equals(5));
        assertFalse("BitArray should not be equal the string \"Hello World\"", _ba1.equals("Hello World"));

        // than test with a bit array of the same length
        assertFalse("BitArray should not be equal empty BitArray", _ba1.equals(_ba2));

        _ba2.set(31, true);
        assertTrue("BitArray should be equal with other BitArray with same bits set", _ba1.equals(_ba2));

        // test when the second array is longer than the first
        _ba2.set(35, true);
        assertFalse("BitArray should not be equal with other longer BitArray with different bits set", _ba1.equals(_ba2));

        _ba2.set(35, false);
        assertTrue("BitArray should be equal with other longer BitArray with same bits set", _ba1.equals(_ba2));

        // and the other way around
        _ba1.set(68, true);
        assertFalse("BitArray should not be equal with other shorter BitArray with different bits set", _ba1.equals(_ba2));

        _ba1.set(68, false);
        assertTrue("BitArray should be equal with other shorter BitArray with same bits set", _ba1.equals(_ba2));
    }

    @Test
    public void testClone() {
        _ba2.set(31, true);

        BitArray ba2 = _ba2.clone();
        assertTrue("Both cloned BitArray should be equal", _ba2.equals(ba2));
    }
}
