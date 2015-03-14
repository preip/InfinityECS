/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.ecs.utils;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simon
 */
public class ReadOnlyIteratorTest {
    
    public ReadOnlyIteratorTest() {
    }
    
    private List<Integer> _src1,_src2,_src3;
    private ReadOnlyCollection<Integer> _col1,_col2,_col3;
    private ReadOnlyIterator<Integer> _iter1,_iter2,_iter3;
    
    @Before
    public void setUp() {
	_src1 = new ArrayList<>();
	_src1.add(1);_src1.add(2);_src1.add(3);_src1.add(4);_src1.add(5);_src1.add(6);_src1.add(7);
	_src1.add(8);_src1.add(9);_src1.add(10);
	_src2 = new ArrayList<>();
	_src2.add(11);_src2.add(12);_src2.add(13);_src2.add(14);_src2.add(15);_src2.add(16);
	_src2.add(17);_src2.add(18);_src2.add(19);_src2.add(20);
	_src3 = new ArrayList<>();
	_col1 = new ReadOnlyCollection<>(_src1);
	_col2 = new ReadOnlyCollection<>(_src2);
	_col3 = new ReadOnlyCollection<>(_src3);
	_iter1 = (ReadOnlyIterator) _col1.iterator();
	_iter2 = (ReadOnlyIterator) _col2.iterator();
	_iter3 = (ReadOnlyIterator) _col3.iterator();
	}
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class ReadOnlyIterator.
     */
    @Test
    public void testHasNext() {
	assertTrue(_iter1.hasNext());
	assertTrue(_iter2.hasNext());
	assertFalse(_iter3.hasNext());
	int i = _col1.size();
	for(int k =0 ; k <i; k++){
	   _iter1.next();
	}
	assertFalse(_iter1.hasNext());
    }

    /**
     * Test of next method, of class ReadOnlyIterator.
     */
    @Test
    public void testNext() {
	int i = 1;
	while(_iter1.hasNext()){
	    assertTrue(_iter1.next().equals(i));
	    i++;
	}
    }

    /**
     * Test of remove method, of class ReadOnlyIterator.
     */
    @Test
    public void testRemove() {
	try{
	    _iter1.remove();
	    fail("This should throw an Exception!");
	} catch(UnsupportedOperationException e) {}
	try{
	    _iter2.remove();
	    fail("This should throw an Exception!");
	} catch(UnsupportedOperationException e) {}
    }
    
}
