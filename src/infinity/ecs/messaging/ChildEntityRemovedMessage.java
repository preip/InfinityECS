package infinity.ecs.messaging;

import infinity.ecs.core.Entity;

/**
 * Message class which is send after a Child-{@link Entity} was removed from its parent.
 *  
 * @author preip
 */
public class ChildEntityRemovedMessage extends Message {
	/**
	 * The {@link Entity} to which a child was removed.
	 */
	private final Entity _parent;
	
	/**
	 * The child {@link Entity} which was removed.
	 */
	private final Entity _child;
	
	/**
	 * Creates a new instance of the {@link ChildEntityRemovedMessage} class.
	 * 
	 * @param parent The {@link Entity} to which a child was removed.
	 * @param child The child {@link Entity} which was removed.
	 */
	public ChildEntityRemovedMessage(Entity parent, Entity child) {
		_parent = parent;
		_child = child;
	}
	
	/**
	 * Gets the {@link Entity} to which a child was removed.
	 * 
	 * @return The {@link Entity} to which a child was removed.
	 */
	public Entity getParent() {
		return _parent;
	}
	
	/**
	 * Gets the child {@link Entity} which has been removed.
	 * 
	 * @return The child {@link Entity} which was removed.
	 */
	public Entity getChild() {
		return _child;
	}
}
