package infinity.ecs.messaging;

import infinity.ecs.core.Entity;

/**
 * Message class which is send when a new entity has been created.
 * Should only be send by the EntityManager.
 * 
 * @author preip
 */
public class EntityCreatedMessage extends Message {
	
	/**
	 * An immutable reference to the entity which has been created.
	 */
	private final Entity _entity;
	
	/**
	 * Creates a new instance of the {@link EntityCreatedMessage} class.
	 * 
	 * @param entity The Entity which has been created.
	 */
	public EntityCreatedMessage(Entity entity) {
		_entity = entity;
	}
	
	/**
	 * Gets the entity which has been created.
	 * 
	 * @return The entity which has been created.
	 */
	public Entity getEntity() {
		return _entity;
	}
	
	/**
	 * Gets the id of the entity which has been created.
	 * 
	 * @return The id of the entity which has been created.
	 */
	public int getEntityId() {
		return _entity.getId();
	}
}
