package example.main;

import example.components.CounterComponent;
import example.systems.Counter;
import infinity.ecs.core.ComponentMask;
import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;
import infinity.ecs.core.EntityManager;

/**
 *
 * @author Simon
 */
public class Main {
    
    public static void main(String[] args){
	ComponentType type = ComponentType.get(CounterComponent.class);
	ComponentMask mask = new ComponentMask(type);
	
	EntityManager manager = EntityManager.getEntityManager();
	Entity entity = manager.createEntity();
	CounterComponent component = new CounterComponent(entity);
	
	try{ 
	    manager.addComponents(entity, component);
	}
	catch(Exception e){
	    
	}
	
	Counter counter = new Counter(mask);
	
	counter.initialize();
	counter.update(100);
	
	System.out.println(component.counter);
	
	counter.update(100);
	
	System.out.println(component.counter);
	
    }
    
}
