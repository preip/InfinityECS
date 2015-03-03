package infinity.ecs.scheduling;

import infinity.ecs.core.EntitySystem;
import infinity.ecs.exceptions.ScheduleIsRunningException;
import java.util.ArrayList;
import java.util.List;

/**
 * The AdvancedRRScheduler schedules the EntitySystems according to a range of time frames they can 
 * be idle. For example System A should run every time frame and System B only every second, so you 
 * would set the range to be idle for A to 1 and the for B to 2. The Schedule would then be 
 * A - AB - A - AB... Now we want to schedule (A,1), (B,2), (C,4) the schedule than is 
 * A - AB - AC - AB - A ... notice that C runs one time frame earlier to ensure that the systems
 * are spread evenly over the time frames. The algorithm selects the last time frame in idle range, 
 * that has the minimal number of Systems in it.
 * @author Simon
 */
public class AdvancedRRScheduler implements Scheduler {

    /**
     * Contains the current schedule that is planed.
     */
    private ArrayList<EntitySystem> _schedule;
    
    /**
     * Contains all systems that need to be scheduled mapped to their priority.
     */
    private final ArrayList<ArrayList<EntitySystem>> _systems;
    
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
    private final static AdvancedRRScheduler _instance = new AdvancedRRScheduler();
    
    /**
     * RRScheduler is a singelton, so the constructor needs to be private.
     */
    private AdvancedRRScheduler(){
	_index = 0;
	_schedule = new ArrayList<>();
	_systems = new ArrayList<>();
    } 
	
    /**
     * 
     * @return The only instance of RRScheduler. 
     */
    public static AdvancedRRScheduler getInstance() {
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
     * @return 
     */
    @Override
    public boolean removeSystem(EntitySystem system){
	for(ArrayList<EntitySystem> list : _systems){
		if(list.remove(system))
			return true;
	}
	return false;
    }
    
    /**
     * Registers the system in the Scheduler.
     * @param system
     * @param priority The priority says how often the system needs to run, if the priority of 
system A is 1 and the of B is 5, than A runs every frame and B at least every 5th frame
     * @return  
     */
    @Override 
    public boolean registerSystem(EntitySystem system, Integer priority) {
	ArrayList<EntitySystem> list; 
	list = _systems.get(priority);
	if(list == null) {
	    list = new ArrayList<>();
	    _systems.add(priority, list);	    
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
		_schedule.get(_index).update(1);
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
    public void makeSchedule() throws ScheduleIsRunningException {
	if(_runFlag)
	    throw new ScheduleIsRunningException();
	_schedule = new ArrayList<>();
	_index = 0;
	ArrayList<ArrayList<EntitySystem>> frames = makeFrames();
	for(ArrayList<EntitySystem> frame : frames){
	    _schedule.addAll(frame);
	}
    }   
    
    private ArrayList<ArrayList<EntitySystem>> makeFrames() {
	Integer numberOfFrames = _systems.size();
	ArrayList<ArrayList<EntitySystem>> frames = new ArrayList<>();
	
	//Makes the new frames
	for(int i = 0; i < numberOfFrames; i++){
	    frames.add(new ArrayList<>());
	}
	//Adds the system of each priority to the frames
	for(int i = 1; i <= numberOfFrames; i++){
	    ArrayList<EntitySystem> systemList = _systems.get(i);
	    if(systemList != null) {
		for(EntitySystem system : systemList) {
		    addSystemToFrames(system,frames,i);
		}
	    }
	}
	return frames; 
    }
    
    
    /**
     * Adds a system to the right frames. 
     * @param system
     * @param frames
     * @param parameter
     * @return 
     */
    private ArrayList<ArrayList<EntitySystem>> addSystemToFrames(EntitySystem system, 
	    ArrayList<ArrayList<EntitySystem>> frames, Integer parameter) {
	
	int i = 0;
	List<ArrayList<EntitySystem>> nextLists;
	List<ArrayList<EntitySystem>> minimalLists;
	ArrayList<EntitySystem> tempFrame;
	
	while(i < frames.size()) {
	  nextLists = frames.subList(i, i + parameter); //Gets all the lists in the right range.
	  minimalLists = smallestLists(nextLists); //Gets the smallests frames that still can be appended.
	  tempFrame = minimalLists.get(minimalLists.size() - 1 ); //Gets the last frame that is the farest away from the last call.
	  tempFrame.add(system);
	  i  += frames.indexOf(tempFrame);
	}
	
	//This is needed to check if there is no gap (bigger than the paramter) at the end of the frame. 
	nextLists = frames.subList(i, i + parameter);
	if(!contains(nextLists, system)){
	    minimalLists = smallestLists(nextLists);
	    tempFrame = minimalLists.get(minimalLists.size() - 1 ); //Gets the last frame that is the farest away from the last call.
	    tempFrame.add(system);
	}
	
	return frames;
    }
    
    /**
     * Returns true if the system is contained in the lists.
     * @param lists
     * @param system
     * @return 
     */
    private boolean contains(List<ArrayList<EntitySystem>> lists, EntitySystem system){
	for(ArrayList<EntitySystem> list : lists){
	    if(list.contains(system))
		return true;
	}
	return false;
    }
       
    /**
     * Returns the lists that have a minimal size
     * @param lists
     * @return 
     */
    private List<ArrayList<EntitySystem>> smallestLists(List<ArrayList<EntitySystem>> lists) {
	List<ArrayList<EntitySystem>> resultList = new ArrayList<>();
	int minSize = lists.get(0).size();
	for(ArrayList<EntitySystem> list : lists) {
	    int tempListSize = list.size();
	    if(tempListSize == minSize)
		resultList.add(list);
	    if(tempListSize < minSize) {
		resultList.clear();
		minSize = tempListSize;
		resultList.add(list);
	    }
	}    
	return resultList;
    }
}
