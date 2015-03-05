package infinity.ecs.messaging;

import infinity.ecs.core.Component;
import infinity.ecs.core.Entity;

/**
 * Message class which is send after a {@link Component} was removed from an {@link Entity}.
 * Should only be send by the EntityManager.
 * 
 * @author preip
 */
public class ComponentRemovedMessage {
	
	/**
	 * The {@link Component} which was removed.
	 */
	private final Component _component;
	
	/**
	 * Creates a new instance of the {@link ComponentAddedMessage} class. 
	 * 
	 * @param component The {@link Component} which was removed.
	 */
	public ComponentRemovedMessage(Component component) {
		_component = component;
	}
	
	/**
	 * Gets the {@link Component} which was removed.
	 * 
	 * @return The {@link Component} which was removed.
	 */
	public Component getComponent() {
		return _component;
	}
	
	/**
	 * Gets the {@link Entity} to which the {@link Component} was removed.
	 * 
	 * @return The {@link Entity} to which the {@link Component} was removed.
	 */
	public Entity getEntity() {
		return _component.getEntity();
	}
}
