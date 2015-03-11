package infinity.ecs.core;

/**
 * Abstract class which identifies all derived classes as {@link Component}s, which can be used as
 * a specific data storage in combination with {@link Entity}s.
 * 
 * @author preip
 */
public abstract class Component {
	
	/**
	 * The {@link Entity} which this {@link Component} is part of.
	 */
	private Entity _entity;
	
	/**
	 * Creates a new instance of the {@link Component} class.
	 */
	protected Component() {
		_entity = null;
	}
	
	/**
	 * Binds this {@link Component} to the specified {@link Entity}.
	 * Package private, should only be called by the {@link EntityManager}.
	 * 
	 * @param entity The {@link Entity} this {@link Component} should be bound to.
	 */
	void bind(Entity entity) {
		_entity = entity;
	}
	
	/**
	 * Gets the {@link Entity} which this {@link Component} is part of.
	 * @return The {@link Entity} which this {@link Component} is part of.
	 */
	public Entity getEntity() {
		return _entity;
	}
	
	/**
	 * Gets the {@link ComponentType} of this {@link Component}.
	 * 
	 * @return The resulting {@link ComponentType}.
	 */
	public final ComponentType getComponentType() {
		return ComponentType.get(this.getClass());
	}
}
