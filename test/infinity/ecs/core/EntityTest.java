/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.ecs.core;

import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simon
 */
public class EntityTest {
    
    private Entity _entity;
    private HashSet<Entity> _entitys;
    private static EntityManager _manager;
    
    @BeforeClass
    public static void setUpClass(){
        _manager = new EntityManager();
    }
    
    @AfterClass
    public static void tearDownCalss(){
        _manager = null;
    }
    
    @Before
    public void setUp() {
        _entity = _manager.createEntity();
        _entitys = new HashSet<>();
    }

    @After
    public void tearDown() {
        _entity = null;
    }

    /**
     * Test if the entity manager creates unique Entitys with unique ids.
     */
    @Test
    public void testUniqueEntity() {
        HashSet<Integer> ids = new HashSet<>();
        for(int i = 0; i<100000; i++){
            _entity = _manager.createEntity();
            assertTrue("A Entity is not unique",_entitys.add(_entity));
            assertTrue("A Entity has a ID that is not unique!",
                    ids.add(_entity.getId()));
        }
    }

    /**
     * Test of equals method, of class Entity.
     */
    @Test
    public void testEquals() {
        assertTrue("A Entity does not equal it self",_entity.equals(_entity));
        assertFalse("A Entity equals another Entity",
                _entity.equals(_manager.createEntity()));
        assertFalse("Entity equals null",_entity.equals(null));
    }

    /**
     * Test of hashCode method, of class Entity.
     */
    @Test
    public void testHashCode() {
        assertTrue("The hashCode is not the ID",
                _entity.getId() == _entity.hashCode());      
    }
    
    /**
     * Test of getComponent method, of class Entity
     */
    @Test
    public void testGetComponent() {
        //TODO: Implement this when there are components to test with
    }
    
    /**
     * Test of addComponent method, of class Entity
     */
    @Test
    public void testAddComponent() {
        //TODO: Implement this when there are components to test with    
    }
    
    /**
     * Test of getComponentMask method, of class Entity
     */
    @Test
    public void testGetComponentMask() {
        //TODO: Implement this when there are components to test with
    }
}
