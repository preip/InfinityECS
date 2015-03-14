package infinity.ecs.messaging;

import infinity.ecs.core.Entity;

/**
 * Message class which is send after a Child-{@link Entity} was added to a parent.
 *  
 * @author preip
 */
public class ChildEntityAddedMessage extends Message {
	/**
	 * The {@link Entity} to which a child was added.
	 */
	private final Entity _parent;
	
	/**
	 * The child {@link Entity} which was added.
	 */
	private final Entity _child;
	
	/**
	 * Creates a new instance of the {@link ChildEntityAddedMessage} class.
	 * 
	 * @param parent The {@link Entity} to which a child was added.
	 * @param child The child {@link Entity} which was added.
	 */
	public ChildEntityAddedMessage(Entity parent, Entity child) {
		_parent = parent;
		_child = child;
	}
	
	/**
	 * Gets the {@link Entity} to which a child was added.
	 * 
	 * @return The {@link Entity} to which a child was added.
	 */
	public Entity getParent() {
		return _parent;
	}
	
	/**
	 * Gets the child {@link Entity} which has been added.
	 * 
	 * @return The child {@link Entity} which was added.
	 */
	public Entity getChild() {
		return _child;
	}
}
