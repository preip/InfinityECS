package infinity.ecs.core;

/**
 * A ECSSystem iterates over all entities that have a particular component and updates them.
 * @author Simon
 */
public abstract class ECSSystem {
    /**
     * The ComponentMask for all ComponentTypes that the ECSSystem needs.
     */
    protected ComponentMask _mask;
    /**
     * Creates a new System.
     * @param mask the ComponentMask describing all Components that are needed for the system to
     * operate.
     */
    public ECSSystem(ComponentMask mask){
	this._mask = mask;
    }
    /**
     * 
     * @return The ComponentMask used by the ECSSystem.
     */
    public ComponentMask getComponentMask(){
	return this._mask;
    }
    /**
     * Runs the ECSSystem on all Entities.
     * @return true if all updates where successful.
     */
    public abstract boolean update();
    
}
