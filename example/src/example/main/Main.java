package example.main;

import example.components.CounterComponent;
import example.systems.Counter;
import infinity.ecs.core.ComponentMask;
import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;
import infinity.ecs.core.EntityManager;
import infinity.ecs.core.EntitySystem;
import infinity.ecs.scheduling.RRScheduler;
import infinity.ecs.scheduling.Scheduler;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 *
 * @author Simon
 */
public class Main {
    
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
    
    public static void main(String[] args){
	ComponentType type = ComponentType.get(CounterComponent.class);
	ComponentMask mask = new ComponentMask(type);
	
	EntityManager manager = EntityManager.getEntityManager();
	Entity entity = manager.createEntity();
	CounterComponent component = new CounterComponent(entity);
	Scheduler scheduler = new RRScheduler();
	
	Main main = new Main();

	try{ 
	    manager.addComponents(entity, component);
	}
	catch(Exception e){
	    
	}
	
	Counter counter = new Counter(mask);
	scheduler.registerSystem(counter, 1);
	try{
	    scheduler.makeSchedule();
	}catch (Exception e){
	}
	System.out.println(main.getSchedule(scheduler));
	
	counter.initialize();
	counter.update(100);
	
	System.out.println(component.counter);
	
	counter.update(100);
	
	System.out.println(component.counter);
	
    }
    
}
