/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.ecs.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author Simon
 */
public class IdPoolTest {
    private IdPool _defaultIdPool,_bigfullIdPool, _emptyIdPool;
    
    @Before
    public void setUp() {
        _emptyIdPool = new IdPool();
        _defaultIdPool = new IdPool();
        _defaultIdPool.freeId(10);
        _defaultIdPool.freeId(42);
        _bigfullIdPool = new IdPool(10000);
        for(int i=0; i < 10000; i++)
            _bigfullIdPool.freeId(i);   
    }
    
    @After
    public void tearDown() {
        _emptyIdPool = null;
        _defaultIdPool = null;
        _bigfullIdPool = null;
    }

    /**
     * Test of getId method, of class IdPool.
     */
    @Test
    public void testGetId() {
        int value = _emptyIdPool.getId();
        assertThat("Two generated ids shouldnt be the same",value, is(not(_emptyIdPool.getId())));
        
        assertEquals("GetId didnt return the right id",_defaultIdPool.getId(),42);
        assertEquals("GetId didnt return the right id",_defaultIdPool.getId(),10);
        //10000 shouldnt be in the IdPool because the stack was already full at 9999
        assertEquals("The bigfullIdPool should return 9999",_bigfullIdPool.getId(),9999);        
    }

    /**
     * Test of getFreeIdCapacity method, of class IdPool.
     */
    @Test
    public void testFreeIdCapacity() {
        assertEquals("The _emptyIdPool should have a capacity of 255",_emptyIdPool.getFreeIdCapacity(),255);
        assertEquals("The _defaultIdPool should have a capacity of 255",_defaultIdPool.getFreeIdCapacity(),255);
        assertEquals("The _bigfullIdPool should have a capacity of 10000",_bigfullIdPool.getFreeIdCapacity(),10000);
        
        _bigfullIdPool.setFreeIdCapacity(255);
        assertEquals("The _bigfullIdPool should have a capacity of 255",_bigfullIdPool.getFreeIdCapacity(),255);
        
        _defaultIdPool.setFreeIdCapacity(10000);
        assertEquals("The _defaultdPool should have a capacity of 10000",_defaultIdPool.getFreeIdCapacity(),10000);
    }
    
}
