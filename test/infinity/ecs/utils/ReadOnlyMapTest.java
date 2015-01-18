/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.ecs.utils;

import infinity.ecs.core.Entity;
import infinity.ecs.core.EntityManager;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simon
 */
public class ReadOnlyMapTest {
    
    HashMap<Integer,Entity> _hashmap;
    ReadOnlyMap<Integer, Entity> _rdoMap;
    EntityManager _manager;
    Entity _entity;
    
    public ReadOnlyMapTest() {
    }
    
    @Before
    public void setUp() {
	_manager = EntityManager.getEntityManager();
	_hashmap = new HashMap<>();
	_entity = _manager.createEntity();
	_hashmap.put(_entity.getId(), _entity);
	_rdoMap = new ReadOnlyMap(_hashmap);
    }
    
    @After
    public void tearDown() {
	_manager = null;
	_hashmap = null;
	_rdoMap = null;
    }

    /**
     * Test of containsKey method, of class ReadOnlyMap.
     */
    @Test
    public void testContainsKey() {
	assertTrue("Should contain this key", _rdoMap.containsKey(_entity.getId()));
	assertFalse("Should not contain this key", _rdoMap.containsKey(1000));
    }

    /**
     * Test of containsValue method, of class ReadOnlyMap.
     */
    @Test
    public void testContainsValue() {
	assertTrue("Should contain this value", _rdoMap.containsValue(_entity));
	assertFalse("Should not contain this value", _rdoMap.containsValue(_manager.createEntity()));
    }

    /**
     * Test of get method, of class ReadOnlyMap.
     */
    @Test
    public void testGet() {
	assertTrue("Should be the same", _rdoMap.get(_entity.getId()) == _entity);
    }

    /**
     * Test of isEmpty method, of class ReadOnlyMap.
     */
    @Test
    public void testIsEmpty() {
	ReadOnlyMap<Integer,Entity> map = new ReadOnlyMap(new HashMap<Integer,Entity>());
	assertFalse("Should be false", _rdoMap.isEmpty());
	assertTrue("Should be true", map.isEmpty());
    }

    /**
     * Test of keySet method, of class ReadOnlyMap.
     */
    @Test
    public void testKeySet() {
	assertTrue("Should contain the same keys", _rdoMap.keySet().equals(_hashmap.keySet()));
    }

    /**
     * Test of size method, of class ReadOnlyMap.
     */
    @Test
    public void testSize() {
	assertTrue("Should be 1", _rdoMap.size() == 1);
    }

    /**
     * Test of values method, of class ReadOnlyMap.
     */
    @Test
    public void testValues() {
	assertTrue("Should contain the same values", _rdoMap.values().equals(_hashmap.values()));
    }
    
}
