package infinity.ecs.messaging;

import infinity.ecs.core.Component;
import infinity.ecs.core.Entity;

/**
 * Message class which is send after a new {@link Component} was added to an {@link Entity}.
 * Should only be send by the EntityManager.
 * 
 * @author preip
 */
public class ComponentAddedMessage extends Message {
	
	/**
	 * The {@link Component} which was added.
	 */
	private final Component _component;
	
	/**
	 * Creates a new instance of the {@link ComponentAddedMessage} class. 
	 * 
	 * @param component The {@link Component} which was added.
	 */
	public ComponentAddedMessage(Component component) {
		_component = component;
	}
	
	/**
	 * Gets the {@link Component} which was added.
	 * 
	 * @return The {@link Component} which was added.
	 */
	public Component getComponent() {
		return _component;
	}
	
	/**
	 * Gets the {@link Entity} to which the {@link Component} was added.
	 * 
	 * @return The {@link Entity} to which the {@link Component} was added.
	 */
	public Entity getEntity() {
		return _component.getEntity();
	}
}
