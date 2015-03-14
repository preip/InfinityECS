package infinity.ecs.core;

import infinity.ecs.messaging.MessageDispatcher;

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
	 * Gets the {@link ComponentMask} of this system.
	 * 
	 * @return The component mask
	 */
	public final ComponentMask getComponentMask() {
		return _mask;
	}
	
	/**
	 * Initializes all resources needed by the system.
	 */
	public abstract void initialize();
	
	/**
	 * Updates this system.
	 * 
	 * @param elapsedTime The time which has elapsed since the last update.
	 */
	public abstract void update(int elapsedTime);
	
	/**
	 * Frees all resources that are used by the system. 
	 */
	public abstract void terminate();
	
	/**
	 * Sets the {@link EntityManager} for this system.
	 * @param manager 
	 */
	public abstract void setManager(EntityManager manager);
	
	/**
	 * Sets the {@link MessageDispatcher} for this systems
	 * @param dispatcher 
	 */
	public abstract void setDispatcher(MessageDispatcher dispatcher);
}
