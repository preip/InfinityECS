package infinity.ecs.scheduling;

import infinity.ecs.core.EntitySystem;
import infinity.ecs.exceptions.ScheduleIsRunningException;

/**
 *
 * @author Simon
 */
public interface Scheduler extends Runnable {   
    /**
     * Runs the schedule.
     */
    @Override
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
    
    /**
     * Returns how often the schedule was executed since the start.
     * @return 
     */
    public abstract int getRuns();
 
    /**
     * True if the schedule is currently running.
     * @return 
     */
    public abstract boolean isRunning();
}
