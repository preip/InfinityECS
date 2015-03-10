package infinity.ecs.scheduling;

import infinity.ecs.core.EntitySystem;
import infinity.ecs.exceptions.ScheduleIsRunningException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is a very simple RoundRobin-Scheduler. The ECSSystems are executed according to their
 * priority, whereby 0 is the highest priority. After every system is executed the schedule starts 
 * anew, the schedule only stops if pauseSchedule() or endSchedule() are called.
 * @author Simon
 */
public class RRScheduler implements Scheduler{
    
    /**
     * Contains the current schedule that is planed.
     */
    private ArrayList<EntitySystem> _schedule;
    
    /**
     * Contains all systems that need to be scheduled mapped to their priority.
     */
    private final ArrayList<EntitySystem> _systems;
    
    /**
     * The current Index of the Execution. Is needed if you want to pause the main loop.
     */
    private int _index; 
    
    /**
     * Is used to end the main loop.
     */
    private boolean _runFlag;
    
    /**
     * The only instance of the RRScheduler.
     */
    private static RRScheduler _instance = new RRScheduler();
    
    /**
     * RRScheduler is a singelton, so the constructor needs to be private.
     */
    private RRScheduler(){
	_index = 0;
	_schedule = new ArrayList<>();
	_systems = new ArrayList<>();
    } 
	
    /**
     * 
     * @return The only instance of RRScheduler. 
     */
    public static RRScheduler getInstance() {
	    return _instance;
    }
    
    /**
     * Ends the main loop in run() and sets the execution index back to 0.
     */
    @Override
    public void end(){
	_runFlag = false; 
	_index = 0;
    }
    /**
     * Stops the main loop in run(), if run() is called again the schedule resumes where it was stopped.
     */
    public void pause(){
	_runFlag = false;
    }
    
    /**
     * Removes the system from the schedule.
     * @param system 
     */
    @Override
    public boolean removeSystem(EntitySystem system){
	return _systems.remove(system);
    }
    
    /**
     * Registers the system in the Scheduler.
     * @param system
     * @param priority The priority of each System must be unique, if a second system with the same 
     * priority is registered nothing happens. The highest priority is 0.
     * @return  
     */
    @Override
    public boolean registerSystem(EntitySystem system, Integer priority) 
	     {

	if(_systems.get(priority) != null)
	    return false;
	_systems.add(priority,system);
	return true;
    }
    
    /**
     * Runs the schedule until the _runFlag is set to false. makeSchedule() needs to be called first
     */
    @Override
    public void run(){
	_runFlag = true;
	while(_runFlag) {
	    while(_index < _schedule.size()){
		_schedule.get(_index).update(0);
		_index += 1;
	    }
	    _index = 0;
	}
    }
    
    /**
     * Recalculates the schedule.
     * @throws ScheduleIsRunningException
     */
    @Override
    public void makeSchedule()throws ScheduleIsRunningException{
	if(_runFlag)
	    throw new ScheduleIsRunningException();
	_schedule = new ArrayList<>();
	_index = 0;
	Iterator<EntitySystem> iter = _systems.iterator();
	while(iter.hasNext()){
	    _schedule.add(iter.next());
	}	  
    }
    
    /**
     * Discards the current instance of the Scheduler and creates a new one.
     */
    public void newInstance(){
	_instance = new RRScheduler();
    }
}
