package infinity.ecs.core;

public abstract class EntitySystem {
	
	/**
	 * A mask consisting of the IDs of all component types the system is interested in.
	 */
	protected final ComponentMask _mask;
	
	/**
	 * Creates a new instance of the EntitySystem class.
	 * 
	 * @param mask A mask consisting of the IDs of all component types the system is interested in.
	 * 		A value of null is treated as an empty mask.
	 */
	protected EntitySystem(ComponentMask mask) {
		if (mask == null)
			mask = new ComponentMask();
		_mask = mask;
	}
	
	/**
	 * Gets the component mask of this system.
	 * 
	 * @return The component mask
	 */
	public final ComponentMask getComponentMask() {
		return _mask;
	}
	
	public abstract void initialize();
	
	/**
	 * Updates this system.
	 */
	public abstract void update();
	
	public abstract void terminate();
}
