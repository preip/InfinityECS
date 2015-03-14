package example.systems;

import infinity.ecs.core.ComponentMask;
import infinity.ecs.core.EntityManager;
import infinity.ecs.core.EntitySystem;
import infinity.ecs.messaging.MessageDispatcher;
import infinity.ecs.scheduling.Scheduler;

/**
 *
 * @author Simon
 */
public class EndSystem extends EntitySystem{
    
    private int _runs;
    private final Scheduler _scheduler;
    private boolean _isInitialized;
    
    public EndSystem(ComponentMask mask, int runs,Scheduler scheduler){
	super(mask);
	_runs = runs;
	_scheduler = scheduler;
    }
    
    @Override
    public void terminate(){}
    
    @Override
    public void update(int time){
	if(_runs < 0){
	    _scheduler.end();
	}
	else{
	    _runs = _runs - 1;
	}
    }
    
    @Override
    public boolean isInitialized(){
	return _isInitialized;
    }
    
    @Override
    public void initialize(EntityManager manager, MessageDispatcher dispatcher){
	_isInitialized = true;
    }
}
