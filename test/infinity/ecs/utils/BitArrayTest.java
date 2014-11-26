package infinity.ecs.utils;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

public class BitArrayTest 
{
    private BitArray _ba,_ba1;
    
    @Before
    public void setUp() {
        _ba = new BitArray();
        _ba1 = new BitArray();
    }
    
    @After
    public void tearDown() {
        _ba = null;
        _ba1 = null;
    }
    
    @Test
    public void testCapacityAndResize() {
		
	// first get the capacity, which should be 32
	int cap = _ba.getCapacity();
	assertTrue("Initial Capacity should be 32 bit", cap == 32);
		
	// try to set the 32th bit to false, which should leave the capacity unchanged
	_ba.set(32, false);
	assertTrue("BitArray Capacity must be unchanged", cap == _ba.getCapacity());
		
	// try to set the 32th bit to true, which should change the capacity
	_ba.set(32, true);
	assertTrue("BitArray Capacity must be unchanged", cap != _ba.getCapacity());
	// the capacity should also be 64 bit
	assertTrue("BitArray Capacity must be 64 bit", _ba.getCapacity() == 64);
		
	// try to set the 127th bit to true, which should change the capacity to 128
	_ba.set(127, true);
	assertTrue("BitArray Capacity must be unchanged", _ba.getCapacity() == 128);
	}
	
    @Test
    public void testGetAndSetFor128Bit() {
    	// first set all bits to true and check their state
    	for (int i = 30; i < 128; i++) {
    		_ba.set(i, true);
    		assertEquals("Bit " + i + "must be true", true, _ba.get(i));
    	}
    	// next set all bits to false and check their state again
    	for (int i = 0; i < 128; i++) {
    		_ba.set(i, false);
    		assertEquals("Bit " + i + "must be false", false, _ba.get(i));
    	}
        assertTrue(true);
    }
    
    @Test
    public void testEquals() {
    	_ba1.set(31, true);
    	
    	// first check for arbitrary objects and null
    	assertFalse("BitArray should not be equal null", _ba1.equals(null));
    	assertFalse("BitArray should not be equal the number 5", _ba1.equals(5));
    	assertFalse("BitArray should not be equal the string \"Hello World\"", _ba1.equals("Hello World"));
    	
    	// than test with a bit array of the same length
    	BitArray ba2 = new BitArray();
    	assertFalse("BitArray should not be equal empty BitArray", _ba1.equals(ba2));
    	
    	ba2.set(31, true);
    	assertTrue("BitArray should be equal with other BitArray with same set bits", _ba1.equals(ba2));
    	
    	// test when the second array is longer than the first
    	ba2.set(35, true);
    	assertFalse("BitArray should not be equal with other longer BitArray with different set bits", _ba1.equals(ba2));
    	
    	ba2.set(35, false);
    	assertTrue("BitArray should be equal with other longer BitArray with same set bits", _ba1.equals(ba2));
    	
    	// and the other way around
    	_ba1.set(68, true);
    	assertFalse("BitArray should not be equal with other shorter BitArray with different set bits", _ba1.equals(ba2));
    	
    	_ba1.set(68, false);
    	assertTrue("BitArray should be equal with other shorter BitArray with same set bits", _ba1.equals(ba2));
    }
    
    @Test
    public void testClone() {
    	_ba1.set(31, true);
    	
    	BitArray ba2 = _ba1.clone(); 
    	assertTrue("Both cloned BitArray should be equal", _ba1.equals(ba2));
    }
}