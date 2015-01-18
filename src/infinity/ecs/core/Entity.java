package infinity.ecs.core;

import java.util.HashMap;

import infinity.ecs.exceptions.ComponentAlreadyExistsException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author preip, Simon
 * @version 0.1
 */
public class Entity {
	/**
	 * The unique id of the Entity.
	 */
	private final int _id;

	/**
	 * A Map containing all Components that belong directly to this Entity, indexed by their
	 * type. There can only be one Component of a specific ComponentType.
	 */
	private final HashMap<ComponentType,Component> _components;
	
	/**
	 * A list of all nested entities for this entity. Only the EntityManager should add and 
	 * delete from this list, therefor only package private. 
	 */
	final HashMap<Integer,Entity> _nestedEntities;
	
	/**
	 * If the Entity is nested this field contains the super Entity. Should only be set by the
	 * EntityManager therefor only package private.
	 */
	Entity _superEntity;
	
	/**
	 * Package private constructor, which initializes the entity with the 
	 * specified id. Can't be public, or IDs were no longer be guaranteed 
	 * to be unique
	 * @param id The unique id of the entity.
	 */
	Entity(int id) {
		_id = id;
		_components = new HashMap<>();
		_nestedEntities = new HashMap<>();
	}
	
	public int getId() {
		return _id;
	}
	
	public boolean isNested(){
	    return (_superEntity != null);
	}
	
	public HashMap<Integer,Entity> getNestedEntitys() {
	    return _nestedEntities;
	}
	
	public Component getComponent(ComponentType type) {
	    return _components.get(type);
	}
	
	/**
	 * Iterates over all nested Entities (also the nested Entities of nested Entities etc.) 
	 * and retrieves their component with the right ComponentType.
	 * @param type The ComponentType of the Component.
	 * @return the nested Components, or null if there are no components of this type.
	 */
	public ArrayList<Component> getNestedComponents(ComponentType type) {
	    LinkedList<Entity> entityQue = new LinkedList<>();
	    ArrayList<Component> nestedComponents = new ArrayList<>();
	    entityQue.addAll(_nestedEntities.values());
	    while(!entityQue.isEmpty()){
		Entity tempEntity = entityQue.poll();
		//Checks if there are nested Entities in nested the Entity and adds them to the que
		if(!tempEntity._nestedEntities.isEmpty()) {
		    entityQue.addAll(tempEntity._nestedEntities.values());
		}
		//Adds the new nested component to the return list
		nestedComponents.add(tempEntity.getComponent(type));
	    }
	    return nestedComponents;
	}

	/**
	 * Adds new components to the entity, components should only be added by the EntityManager.
	 * @param components the components you wish to add
	 * @throws ComponentAlreadyExistsException If the component that should be added is unique and
	 * the entity already contains a component of the same type.
	 */
	void addComponents(Component ... components) 
			throws ComponentAlreadyExistsException {
		for(Component component : components) {
			ComponentType type = ComponentType.get(component);
			//throws an Exception if the Entity already has a Component of the specified
			//type.
			if (_components.containsKey(type))
			    if(_components.get(type) != null)
				throw new ComponentAlreadyExistsException
				    ("The entity already contains an instance of: " + component);
			if (component == null) {
				_components.put(type, component);
			}
		}
	}

	/**
	 * Creates a new ComponentMask for the Entity.
	 * @return 
	 */
	public ComponentMask getComponentMask(){
		return new ComponentMask(_components.keySet());
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
