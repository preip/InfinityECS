package infinity.ecs.messaging;

import infinity.ecs.core.Component;
import infinity.ecs.core.Entity;

/**
 * Message class which is send after a {@link Component} was changed.
 * 
 * @author preip
 */
public class ComponentChangedMessage {
	
	/**
	 * The {@link Component} which was changed.
	 */
	private final Component _component;
	
	/**
	 * Creates a new instance of the {@link ComponentAddedMessage} class. 
	 * 
	 * @param component The {@link Component} which was changed.
	 */
	public ComponentChangedMessage(Component component) {
		_component = component;
	}
	
	/**
	 * Gets the {@link Component} which was changed.
	 * 
	 * @return The {@link Component} which was changed.
	 */
	public Component getComponent() {
		return _component;
	}
	
	/**
	 * Gets the {@link Entity} to which the {@link Component} was changed.
	 * 
	 * @return The {@link Entity} to which the {@link Component} was changed.
	 */
	public Entity getEntity() {
		return _component.getEntity();
	}
}
