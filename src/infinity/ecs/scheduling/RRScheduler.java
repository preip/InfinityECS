package infinity.ecs.scheduling;

import infinity.ecs.core.EntitySystem;
import infinity.ecs.exceptions.ScheduleIsRunningException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

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
    private final SortedMap<Integer,ArrayList<EntitySystem>> _systems;
      
    /**
     * The current Index of the Execution. Is needed if you want to pause the main loop.
     */
    private int _index;
    
    /**
     * The current amount of runs since the start.
     */
    private int _runs;
    
    /**
     * Is used to end the main loop.
     */
    private boolean _runFlag;
    
    /**
     * RRScheduler is a singleton, so the constructor needs to be private.
     */
    public RRScheduler(){
	_index = 0;
	_schedule = new ArrayList<>();
	_systems = new TreeMap<>();
    } 
    
    /**
     * Ends the main loop in run() and sets the execution index and runs back to 0.
     */
    @Override
    public void end(){
	_runFlag = false; 
	_index = 0;
	_runs = 0;
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
	Collection<Integer> keys = _systems.keySet();
	for(Integer key : keys){
	    if(_systems.remove(key, system))
		return true;
	}
	return false;
    }
    
    /**
     * Registers the system in the Scheduler.
     * @param system
     * @param priority The priority of each System must be unique, if a second system with the same 
     * priority is registered nothing happens. The highest priority is 1. The priority must be &#62; 0
     * @return  
     */
    @Override
    public boolean registerSystem(EntitySystem system, Integer priority) {
	if(priority <= 0)
	    return false;
	ArrayList<EntitySystem> list = _systems.get(priority);
	if(list == null) {
	    list = new ArrayList<>();
	    _systems.put(priority, list);
	}
	return list.add(system);
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
	    _runs += 1;
	    _index = 0;
	    //Catches the overflow
	    if(_runs == Integer.MAX_VALUE)
		_runs = 0; 
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
	Collection<Integer> keys = _systems.keySet();
	for(Integer key : keys){
	    _schedule.addAll(_systems.get(key));
	}
    }
    
    /**
     * Returns how often the schedule was executed since the start.
     * @return 
     */
    @Override
    public int getRuns(){
	return _runs;
    }
    
    /**
     * True if the schedule is currently running.
     * @return 
     */
    @Override
    public boolean isRunning(){
	return _runFlag;
    }
    
}
