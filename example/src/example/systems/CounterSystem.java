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
    private ReadOnlyCollection<Entity> _allEntities;
    private final ComponentType _counterComponentType; 
    private boolean _isInitialized;
    
    public CounterSystem(ComponentMask mask) {
	super(mask);
	_counterComponentType = ComponentType.get(CounterComponent.class);
    }
    
    @Override
    public void initialize(EntityManager manager, MessageDispatcher dispatcher) {
	_manager = manager;
	_allEntities = _manager.getEntitiesByMask(_mask);
	Iterator<Entity> iter = _allEntities.iterator();
	while(iter.hasNext()) {
	    Entity tempEntity = iter.next();
	    CounterComponent count;
	    count = (CounterComponent)tempEntity.getComponent(_counterComponentType);
	    count.counter = 0;
	}
    }
    
    @Override
    public void update(int elapsedTime){
	Iterator<Entity> iter = _allEntities.iterator();
	while(iter.hasNext()) {
	    Entity tempEntity = iter.next();
	    CounterComponent count;
	    count = (CounterComponent)tempEntity.getComponent(_counterComponentType);
	    count.counter += 1;
	}
	_isInitialized = true;
    }
    
    @Override
    public boolean isInitialized(){
	return _isInitialized;	
    }
    
    @Override
    public void terminate(){}
}
