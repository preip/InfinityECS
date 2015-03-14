package example.systems;
import example.components.CounterComponent;
import infinity.ecs.core.*;
import infinity.ecs.messaging.MessageDispatcher;
import infinity.ecs.utils.ReadOnlyCollection;
import java.util.Iterator;

/**
 *
 * @author Simon
 */
public class CounterSystem extends EntitySystem{
    
    private EntityManager _manager;
    private ReadOnlyCollection<Entity> _entities;
    private final ComponentType _counterComponentType; 
    
    public CounterSystem(ComponentMask mask) {
	super(mask);
	//_entities = _manager.getMatchingEntities(_mask);
	_counterComponentType = ComponentType.get(CounterComponent.class);
    }
    
    @Override
    public void initialize() {
	Iterator<Entity> iter = _entities.iterator();
	while(iter.hasNext()) {
	    Entity tempEntity = iter.next();
	    CounterComponent count;
	    count = (CounterComponent)tempEntity.getComponent(_counterComponentType);
	    count.counter = 0;
	}
    }
    
    @Override
    public void update(int elapsedTime){
	Iterator<Entity> iter = _entities.iterator();
	while(iter.hasNext()) {
	    Entity tempEntity = iter.next();
	    CounterComponent count;
	    count = (CounterComponent)tempEntity.getComponent(_counterComponentType);
	    count.counter += 1;
	}
    }
    
    @Override
    public void terminate(){
    }
    
    @Override
    public void setManager(EntityManager manager) {
	_manager = manager;
    }
    
    @Override
    public void setDispatcher(MessageDispatcher dispatcher){}
    
}
