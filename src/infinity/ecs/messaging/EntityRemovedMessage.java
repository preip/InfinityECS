package infinity.ecs.messaging;

import infinity.ecs.core.Entity;

/**
 * Message class which is send when an entity has been removed.
 * Should only be send by the EntityManager.
 * 
 * @author preip
 */
public class EntityRemovedMessage extends Message {
	
	/**
	 * An immutable reference to the entity which has been removed.
	 */
	private final Entity _entity;
	
	/**
	 * Creates a new instance of the {@link EntityRemovedMessage} class.
	 * 
	 * @param entity The Entity which has been removed.
	 */
	public EntityRemovedMessage(Entity entity) {
		_entity = entity;
	}
	
	/**
	 * Gets the entity which has been removed.
	 * 
	 * @return The entity which has been removed.
	 */
	public Entity getEntity() {
		return _entity;
	}
	
	/**
	 * Gets the id of the entity which has been removed.
	 * 
	 * @return The id of the entity which has been removed.
	 */
	public int getEntityId() {
		return _entity.getId();
	}
}
