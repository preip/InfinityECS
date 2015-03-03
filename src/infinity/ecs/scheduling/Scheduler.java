package infinity.ecs.scheduling;

import infinity.ecs.core.EntitySystem;
import infinity.ecs.exceptions.ScheduleIsRunningException;

/**
 *
 * @author Simon
 */
public interface Scheduler {   
    /**
     * Runs the schedule.
     */
    public abstract void run();
    
    /**
     * Ends the schedule.
     */
    public abstract void end();
    
    /**
     * Recalculates the schedule.
     * @throws ScheduleIsRunningException
     */
    public abstract void makeSchedule() throws ScheduleIsRunningException;
    
    /**
     * Adds a EntitySystem to the Schedule.
     * @param system 
     * @param parameter
     * @return
     */
    public abstract boolean registerSystem(EntitySystem system,Integer parameter) ;
    
    /**
     * Removes a EntitySystem from the schedule. 
     * @param system 
     * @return
     */
    public abstract boolean removeSystem(EntitySystem system);
    
}
