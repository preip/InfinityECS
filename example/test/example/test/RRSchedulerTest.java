package example.test;

import example.components.CounterComponent;
import example.systems.Counter;
import infinity.ecs.core.ComponentMask;
import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;
import infinity.ecs.core.EntityManager;
import infinity.ecs.core.EntitySystem;
import infinity.ecs.exceptions.ScheduleIsRunningException;
import infinity.ecs.scheduling.RRScheduler;
import infinity.ecs.scheduling.Scheduler;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Simon
 */
public class RRSchedulerTest {
    
    private EntityManager _manager;
    private ComponentType _type;
    private ComponentMask _mask;
    private Entity _entity1,_entity2;
    private Counter _counter1, _counter2;
    private CounterComponent _component1, _component2;
    private RRScheduler _scheduler;
    
    public RRSchedulerTest() {
    }
    
    @Before
    public void setUp() {
	_type = ComponentType.get(CounterComponent.class);
	_mask = new ComponentMask(_type);
	_manager = new EntityManager();
	_entity1 = _manager.createEntity();
	_entity2 = _manager.createEntity();
	
	try{ 
	    _manager.addComponents(_entity1, _type);
	    _manager.addComponents(_entity2, _type);
	}
	catch(Exception e){
	    fail("Coulnt add Component to an Entity");
	}
	
	_component1 = (CounterComponent) _manager.getComponent(_entity1, _type);
	_component2 = (CounterComponent) _manager.getComponent(_entity2, _type);
	_counter1 = new Counter(_mask);
	_counter2 = new Counter(_mask);
	_scheduler = new RRScheduler();
    }

    /**
     * Test of end method, of class RRScheduler.
     */
    @Test
    public void testEnd() {
	_scheduler.registerSystem(_counter1, 1);
	_scheduler.registerSystem(_counter2, 2);
	try{
	    _scheduler.makeSchedule();
	} catch(ScheduleIsRunningException e){
	    fail("Schedule shouldnt be running");
	}
	(new Thread(_scheduler)).start();
	try{
	Thread.sleep(100); //TODO realize this with a lock...
	} catch(Exception e){
	}
	assertTrue(_scheduler.isRunning());
	_scheduler.end();
	assertFalse(_scheduler.isRunning());
	assertEquals(_scheduler.getRuns() , 0);
    }

    /**
     * Test of pause method, of class RRScheduler.
     */
    @Test
    public void testPause() {
	// TODO
    }

    /**
     * Test of removeSystem method, of class RRScheduler.
     */
    @Test
    public void testRemoveSystem() {
	// TODO
    }

    /**
     * Test of registerSystem method, of class RRScheduler.
     */
    @Test
    public void testRegisterSystem() {
	_scheduler.registerSystem(_counter1, 1);
	_scheduler.registerSystem(_counter2, 0);
	try{
	    _scheduler.makeSchedule();
	}
	catch(ScheduleIsRunningException e) {
	    fail("Couldnt register a System");
	}
	ArrayList<EntitySystem> schedule = this.getSchedule(_scheduler);
	assertTrue("Should be part of the schedule",schedule.contains(_counter1));
	assertFalse("Should not be part of the schedule",schedule.contains(_counter2));
    }

    /**
     * Test of run method, of class RRScheduler.
     */
    @Test
    public void testRun() {
    }

    /**
     * Returns the made schedule of the scheduler.
     * Reflections \0/
     * @return 
     */
    private ArrayList<EntitySystem> getSchedule(Scheduler scheduler){
	Class<?> SchedulerClass = scheduler.getClass();
	Field field;
	try{
	    field = SchedulerClass.getDeclaredField("_schedule");
	    field.setAccessible(true);
	    return (ArrayList<EntitySystem>) field.get(scheduler);
	}
	catch(Exception e){ 
	    e.printStackTrace();
	}
	return null;
    }
    
    /**
     * Test of makeSchedule method, of class RRScheduler.
     */
    @Test
    public void testMakeSchedule() throws Exception {
	// TODO review the generated test code and remove the default call to fail.
    }
    
}
