package infinity.ecs.core;

import infinity.ecs.exceptions.AlreadyNestedException;
import infinity.ecs.exceptions.ComponentAlreadyExistsException;
import infinity.ecs.utils.ReadOnlyCollection;

/**
 *
 * @author preip, Simon
 * @version 0.1
 */
public final class Entity {
	
	//----------------------------------------------------------------------------------------------
	// Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The unique id of the Entity.
	 */
	private final int _id;

	private final EntityManager _em;
	
	//----------------------------------------------------------------------------------------------
	// Constructor
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Package private constructor, which initializes the entity with the 
	 * specified id. Can't be public, or IDs were no longer be guaranteed 
	 * to be unique
	 * @param id The unique id of the entity.
	 */
	Entity(int id, EntityManager em) {
		_id = id;
		_em = em;
	}
	
	//----------------------------------------------------------------------------------------------
	// Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * 
	 * @return the unique ID of the Entity.
	 */
	public int getId() {
		return _id;
	}
	
	//----------------------------------------------------------------------------------------------
	// Component related methods
	//----------------------------------------------------------------------------------------------
	
	public Component addComponent(ComponentType componentType)
			throws IllegalArgumentException, ComponentAlreadyExistsException {
		return _em.addComponent(this, componentType);
	}
	
	public void addComponents(ComponentType... componentTypes)
			throws IllegalArgumentException, ComponentAlreadyExistsException {
		_em.addComponents(this, componentTypes);
	}
	
	/**
	 * Gets the {@link Component} of the specified {@link ComponentType}.
	 * 
	 * @param componentType the Type of the {@link Component}.
	 * @return the {@link Component} of the specified {@link ComponentType} or null if there was
	 * 		no matching {@link Component}.
	 */
	public Component getComponent(ComponentType componentType) {
		return _em.getComponent(this, componentType);
	}
	
	/**
	 * Removes the components of the specified types from this entity.
	 * 
	 * @param componentType The type of the {@link Component} which should be removed.
	 */
	public boolean removeComponents(ComponentType componentType) {
		return _em.removeComponent(this, componentType);
	}
	
	/**
	 * Gets the ComponentMask for this Entity.
	 * 
	 * @return The ComponentMask.
	 */
	public ComponentMask getComponentMask() {
		return _em.getComponentMask(this);
	}
	
	//----------------------------------------------------------------------------------------------
	// Children related methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Indicates if this Entity is the child of another Entity.
	 * 
	 * @return True if this Entity is the child of another entity, otherwise false.
	 */
	public boolean hasParent(){
	    return _em.getParent(this) != null;
	}
	
	/**
	 * Adds the specified child to this entity.
	 * 
	 * @param child The child that should be added.
	 * @throws AlreadyNestedException Is thrown when nestedEntity is already nested.
	 * @throws IllegalArgumentException When the entity is added as a child to itself.
	 */
	public void addChildEntity(Entity child) throws AlreadyNestedException{
	    _em.addChildEntity(this, child);
	}
	
	/**
	 * Removes the specified child from this Entity.
	 * 
	 * @param child The child that should be removed.
	 */
	public boolean removeChildEntity(Entity child) {
	    return _em.removeChildEntity(this, child);
	}
	
	/**
	 * Returns a ReadOnlyCollection of all children of the entity.
	 * @return The ReadOnlyCollection of all children.
	 */
	public ReadOnlyCollection<Entity> getChildEntities() {
	    return _em.getChildren(this);
	}

	//----------------------------------------------------------------------------------------------
	// Miscellaneous methods
	//----------------------------------------------------------------------------------------------
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity entity = (Entity)obj;
		return _id == entity._id;
	}

	@Override
	public int hashCode() {
		// hash code is just the id, which is assumed to be unique,
		// so there shouldn't be a problem
		return _id;
	}
}
