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
	protected final Entity _entity;
	
	/**
	 * Creates a new instance of the {@link Component} class.
	 * 
	 * @param entity The {@link Entity} which this {@link Component} is part of.
	 * @throws IllegalStateException when the entity is <i>null</i>.
	 */
	protected Component(Entity entity) throws IllegalStateException {
		if (entity == null)
			throw new IllegalStateException();
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
