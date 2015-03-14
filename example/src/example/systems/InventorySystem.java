package example.systems;

import infinity.ecs.core.ComponentMask;
import infinity.ecs.core.Entity;
import infinity.ecs.core.EntityManager;
import infinity.ecs.core.EntitySystem;
import infinity.ecs.messaging.MessageDispatcher;
import infinity.ecs.utils.IndexedCollection;

/**
 *
 * @author Simon
 */
public class InventorySystem extends EntitySystem {
    
    private IndexedCollection<Entity> _allEntities;
    private EntityManager _manager;
    private MessageDispatcher _dispatcher;
    
    public InventorySystem(ComponentMask mask){
	super(mask);
    }
    
    @Override
    public void update(int time){
	
    }
    
    @Override
    public void terminate(){}
    
    @Override
    public void initialize(){
    }
    
    @Override
    public void setManager(EntityManager manager){
	_manager = manager;
    }
    
    @Override
    public void setDispatcher(MessageDispatcher dispatcher){
	_dispatcher = dispatcher;
    }
}
