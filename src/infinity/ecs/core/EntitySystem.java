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
	 * 
	 * @param entityManager The {@link EntityManager} which handles all {@link Entity}s this
	 * 		system may work with.
	 * @param msgDispatcher The {@link MessageDispatcher} with which this system can register it's
	 * 		own {@link MessageEndpoint} to send end receive {@link Message}s. 
	 */
	public abstract void initialize(EntityManager entityManager, MessageDispatcher msgDispatcher);
	
	/**
	 * Indicates if this system has been initialized or not. 
	 * @return
	 */
	public abstract boolean isInitialized();
	
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
}
