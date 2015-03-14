/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.ecs.core;

import infinity.ecs.exceptions.EntityDoesNotExistsException;
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
		_manager = new EntityManager();
		_entity = _manager.createEntity();
		_entity2 = _manager.createEntity();
    }
    
    @After
    public void tearDown() {
		_manager = null;
		_entity = null;
    }

    /**
     * Test of createEntity method, of class EntityManager.
     */
    @Test
    public void testCreateEntity() {
    	_entity = _manager.createEntity();
    	assertFalse("Those Entities should not be the same.", _entity == _entity2);
    }
    
    @Test
    public void addChildEntity() {
    	try {
    		_manager.addChildEntity(_entity, _entity);
    		fail("Entities can not be nested in itself");
    	} catch (Exception e) {
    		
    	}
    	
    	
    	try {
			_manager.addChildEntity(_entity, _entity2);
			assertFalse("Should not have a parent", _entity.hasParent());
		    assertTrue("Should have a parent", _entity2.hasParent());
		} catch (Exception e) {
			fail("Couldnt add a child Entity");
		}
    	try {
			_manager.addChildEntity(_entity, _entity2);
			fail("Children that already have a parent can't be added as a children");
		} catch (Exception e) {

		}
    	
    	_manager.removeChildEntity(_entity2);
    	assertFalse("Should not have a parent",_entity.hasParent());
	    assertFalse("Should have a parent",_entity2.hasParent());
    	
    	Entity entity3 = _manager.createEntity();
    	try {
    		_manager.addChildEntity(entity3, _entity);
    		_manager.addChildEntity(_entity2, entity3);
    	} catch (Exception e) {
			fail("Couldnt add a child Entity");
		}
    	
    	try {
    		_manager.addChildEntity(_entity, _entity2);
    		fail("Entites can not contain children that are in truth their parents");
    	} catch (Exception e) {
    		
    	}
    }

    /**
     * Test of addComponents method, of class EntityManager.
     */
    @Test
    public void testAddComponents_Entity_ComponentArr() throws Exception {
    	//TODO: Implement
    }
}
