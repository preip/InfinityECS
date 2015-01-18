package infinity.ecs.core;

/**
 * A System iterates over all entities that have a particular component and updates them.
 * @author Simon
 */
public abstract class System {
    /**
     * The ComponentMask for all ComponentTypes that the System needs.
     */
    protected ComponentMask _mask;
    /**
     * Creates a new System.
     * @param mask the ComponentMask describing all Components that are needed for the system to
     * operate.
     */
    public System(ComponentMask mask){
	this._mask = mask;
    }
    /**
     * 
     * @return The ComponentMask used by the System.
     */
    public ComponentMask getComponentMask(){
	return this._mask;
    }
    /**
     * Runs the System on all Entities.
     * @return true if all updates where successful.
     */
    public abstract boolean update();
    
}
