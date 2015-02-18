/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.ecs.core;

import infinity.ecs.exceptions.EntityDoesNotExistsException;
import infinity.ecs.utils.ReadOnlyCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simon
 */
public class EntityManagerTest {
    EntityManager _manager;
    Entity _entity, _entity2;
    Component _component;
    
    public EntityManagerTest() {
    }
    
    @Before
    public void setUp() {
	_manager = EntityManager.getEntityManager();
	_entity = _manager.createEntity();
	_entity2 = _manager.createEntity();
    }
    
    @After
    public void tearDown() {
	_manager = null;
	_entity = null;
    }

    /**
     * Test of getEntityManager method, of class EntityManager.
     */
    @Test
    public void testGetEntityManager() {
	assertTrue("The EntityManager is not a singleton",_manager.equals(EntityManager.getEntityManager()));
    }

    /**
     * Test of addChildEntity method, of class EntityManager.
     */
    @Test
    public void testAddNestedEntity() {
	assertFalse("Should not be nested",_entity2.isChild());
	try{
	    _entity.addChildEntity(_entity2);
	    assertFalse("Should not be nested",_entity.isChild());
	    assertTrue("Should be nested",_entity2.isChild());
	} catch (Exception e) {
	    fail("Couldnt add a nested Entity");
	}
    }

    /**
     * Test of createEntity method, of class EntityManager.
     */
    @Test
    public void testCreateEntity() {
	_entity = _manager.createEntity();
	assertFalse("Those Entities should not be the same.", _entity == _entity2);
    }

    /**
     * Test of getEntity method, of class EntityManager.
     */
    /*@Test
    public void testGetEntity() throws Exception {
	assertTrue("", _entity == _manager.getEntity(_entity.getId()));
	try {
	    _entity = _manager.getEntity(100000);
	    fail("getEntity should throw an Exception");
	} catch (EntityDoesNotExistsException e) {
	    
	}
    }*/

    /**
     * Test of addComponents method, of class EntityManager.
     */
    @Test
    public void testAddComponents_Entity_ComponentArr() throws Exception {
	//TODO: Implement
    }

    /**
     * Test of getMatchingEntities method, of class EntityManager.
     */
    @Test
    public void testGetMatchingEntities() {
	ComponentMask componentMask = _entity.getComponentMask();
	ReadOnlyCollection<Entity> entities = _manager.getMatchingEntities(componentMask);
	assertTrue("The Entity should be part of the collection", entities.contains(_entity));
    }
    
}
