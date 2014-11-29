package infinity.ecs.core;

import infinity.ecs.utils.IdPool;

public class EntityManager {
	
	private final IdPool _idPool;
	
	public EntityManager() {
		_idPool = new IdPool();
	}
	
	/**
	 * Creates a new, empty Entity with a unique ID.
	 * @return The created Entity.
	 */
	public Entity createEntity() {
		Entity entity = new Entity(_idPool.getId());
		
		return entity;
	}
}
