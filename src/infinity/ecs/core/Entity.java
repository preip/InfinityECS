package infinity.ecs.core;

import java.util.ArrayList;
import java.util.List;

import infinity.ecs.exceptions.AlreadyNestedException;
import infinity.ecs.exceptions.ComponentAlreadyExistsException;
import infinity.ecs.utils.IndexedCollection;
import infinity.ecs.utils.ReadOnlyCollection;

/**
 *
 * @author preip, Simon
 * @version 0.1
 */
public class Entity {
	
	//----------------------------------------------------------------------------------------------
	// Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The unique id of the Entity.
	 */
	private final int _id;

	/**
	 * A Map containing all Components that belong directly to this Entity, indexed by their
	 * type. There can only be one Component of a specific ComponentType.
	 */
	//private final Map<ComponentType, Component> _components;
	private final IndexedCollection<Component> _components;
	
	/**
	 * The component mask of this entity that indicates which components the entity consists of.
	 */
	private final ComponentMask _componentMask;
	
	/**
	 * A list of all nested entities of this entity.  
	 */
	private final List<Entity> _children;
	
	/**
	 * If the Entity is nested this field contains the super Entity. Should only be set by the
	 * EntityManager and is therefore package private.
	 */
	private Entity _parent;
	
	//----------------------------------------------------------------------------------------------
	// Constructor
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Package private constructor, which initializes the entity with the 
	 * specified id. Can't be public, or IDs were no longer be guaranteed 
	 * to be unique
	 * @param id The unique id of the entity.
	 */
	Entity(int id, Entity parent) {
		_id = id;
		_parent = parent;
		_components = new IndexedCollection<Component>();
		_componentMask = new ComponentMask();
		_children = new ArrayList<>();
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
	
	/**
	 * Gets the Component of the specified Type.
	 * 
	 * @param type the Type of the Component.
	 * @return the Component of the specified ComponentType or null if there was no matching
	 * Component.
	 */
	public Component getComponent(ComponentType type) {
	    return _components.get(type.getId());
	}
	
	public void addComponent(Component component) 
			throws ComponentAlreadyExistsException, IllegalArgumentException {
		if (component == null)
			throw new IllegalArgumentException("The components that should be added to the" +
				"entity can't be null");
		int index = component.getComponentType().getId();
		if (_components.get(index) != null)
			throw new ComponentAlreadyExistsException();
		_components.set(index, component);
	}
	
	/**
	 * Adds new components to the entity, components should only be added by the EntityManager.
	 * TODO: Think about which exception can happen and which not, if this method is only called
	 * by the EntityManager
	 * 
	 * @param components The components that should be added.
	 * @throws ComponentAlreadyExistsException If the entity already contains a component of the
	 * same type than one of the components that should be added.
	 * @throws IllegalArgumentException If one of the components that should be added was null.
	 */
	public void addComponents(Component... components) throws ComponentAlreadyExistsException {
		/*for(Component component : components) {
			if (component == null)
				throw new IllegalArgumentException("The components that should be added to the" +
						"entity can't be null");
			ComponentType type = ComponentType.get(component);
			Component duplicate = _components.get(type);
			// throws an Exception if the Entity already has a Component of the specified type
			if (duplicate != null)
				throw new ComponentAlreadyExistsException
				    ("The entity already contains an instance of: " + component);
			_components.put(type, component);
			_componentMask.add(type);
		}*/
	}
	
	/**
	 * Removes the components of the specified types from this entity.
	 * 
	 * @param componentTypes The types of the components which should be removed.
	 */
	void removeComponents(ComponentType... componentTypes) {
		for (ComponentType cType : componentTypes)
			if (_components.remove(cType.getId()))
				_componentMask.remove(cType);
	}
	
	public boolean removeComponent(ComponentType type) {
		return _components.remove(type.getId());
	}
	
	/**
	 * Gets the ComponentMask for this Entity.
	 * 
	 * @return The ComponentMask.
	 */
	public ComponentMask getComponentMask() {
		return _componentMask;
	}
	
	//----------------------------------------------------------------------------------------------
	// Children related methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Indicates if this Entity is the child of another Entity.
	 * 
	 * @return True if this Entity is the child of another entity, otherwise false.
	 */
	public boolean isChild(){
	    return (_parent != null);
	}
	
	/**
	 * Adds the specified child to this entity.
	 * TODO: Think about which exception can happen and which not, if this method is only called
	 * by the EntityManager
	 * 
	 * @param childEntity The child that should be added.
	 * @throws AlreadyNestedException Is thrown when nestedEntity is already nested.
	 * @throws IllegalArgumentException When the entity is added as a child to itself.
	 */
	void addChildEntity(Entity child) throws AlreadyNestedException{
	    if(this == child)
	    	throw new IllegalArgumentException("Can't add the entity as a child to itself.");
	    if(child.isChild())
	    	throw new AlreadyNestedException("This Entity is already nested");
	    child._parent = this;
	    _children.add(child);
	}
	
	/**
	 * Removes the specified child from this Entity.
	 * 
	 * @param child The child that should be removed.
	 */
	void removeChildEntity(Entity child) {
	    if (_children.remove(child))
	    	child._parent = null;
	}
	
	/**
	 * Returns a ReadOnlyCollection of all children of the entity.
	 * @return The ReadOnlyCollection of all children.
	 */
	public ReadOnlyCollection<Entity> getChildEntities() {
	    return new ReadOnlyCollection<Entity>(_children);
	}

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
