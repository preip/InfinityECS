package infinity.ecs.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simon
 */
public class ReadOnlyCollectionTest {
    
    public ReadOnlyCollectionTest() {
    }
    
    private List<Integer> _src1,_src2;
    private ReadOnlyCollection<Integer> _col1,_col2;
    
    @Before
    public void setUp() {
	_src1 = new ArrayList<>();
	_src1.add(1);_src1.add(2);_src1.add(3);_src1.add(4);_src1.add(5);_src1.add(6);_src1.add(7);
	_src1.add(8);_src1.add(9);_src1.add(10);
	_src2 = new ArrayList<>();
	_src2.add(11);_src2.add(12);_src2.add(13);_src2.add(14);_src2.add(15);_src2.add(16);
	_src2.add(17);_src2.add(18);_src2.add(19);_src2.add(20);
	_col1 = new ReadOnlyCollection<>(_src1);
	_col2 = new ReadOnlyCollection<>(_src2);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of size method, of class ReadOnlyCollection.
     */
    @Test
    public void testSize() {
	assertEquals(_src1.size(),_col1.size());
	assertEquals(_src2.size(),_col2.size());
	_src1.remove(1);
	_src1.remove(4);
	_src2.remove(1);
	assertEquals(_src1.size(),_col1.size());
	assertEquals(_src2.size(),_col2.size());
    }

    /**
     * Test of contains method, of class ReadOnlyCollection.
     */
    @Test
    public void testContains() {
	for(Integer num : _src1){
	    assertTrue(_col1.contains(num));
	}
	for(Integer num : _src2){
	    assertTrue(_col2.contains(num));
	}
	_src2.remove(1);
	_src1.remove(5);
	for(Integer num : _src1){
	    assertTrue(_col1.contains(num));
	}
	for(Integer num : _src2){
	    assertTrue(_col2.contains(num));
	}
    }

    /**
     * Test of get method, of class ReadOnlyCollection.
     */
    @Test
    public void testGet() {
	for(int i = 0; i < _col1.size(); i++){
	   assertEquals(_col1.get(i),_src1.get(i));
	}
	for(int i = 0; i < _col1.size(); i++){
	   assertEquals(_col1.get(i),_src1.get(i));
	}

    }

    /**
     * Test of isSource method, of class ReadOnlyCollection.
     */
    @Test
    public void testIsSource() {
	assertTrue(_col1.isSource(_src1));
	assertTrue(_col2.isSource(_src2));
    }

    /**
     * Test of iterator method, of class ReadOnlyCollection.
     */
    @Test
    public void testIterator() {
	Iterator<Integer> iter = _col1.iterator();
	while(iter.hasNext()){
	    assertTrue(_col1.contains(iter.next()));
	}
    }
    
}
