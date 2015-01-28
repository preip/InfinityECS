package infinity.ecs.core;

import infinity.ecs.exceptions.AlreadyNestedException;
import java.util.HashMap;

import infinity.ecs.exceptions.ComponentAlreadyExistsException;
import infinity.ecs.utils.ReadOnlyCollection;
import java.util.ArrayList;
import java.util.LinkedList;

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
	 * The maximal depth of the nested hierarchy. An unnested Entity has depth 0.
	 */
	private final static int _maximumNestDepth = 3;

	/**
	 * A Map containing all Components that belong directly to this Entity, indexed by their
	 * type. There can only be one Component of a specific ComponentType.
	 */
	private final HashMap<ComponentType,Component> _components;
	
	/**
	 * A list of all nested entities of this entity.  
	 */
	private final ArrayList<Entity> _nestedEntities;
	
	/**
	 * If the Entity is nested this field contains the super Entity. Should only be set by the
	 * EntityManager therefor only package private.
	 */
	private Entity _parentEntity;
	
	/**
	 * Package private constructor, which initializes the entity with the 
	 * specified id. Can't be public, or IDs were no longer be guaranteed 
	 * to be unique
	 * @param id The unique id of the entity.
	 */
	Entity(int id) {
		_id = id;
		_components = new HashMap<>();
		_nestedEntities = new ArrayList<>();
	}
	
	/**
	 * 
	 * @return the unique ID of the Entity.
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * 
	 * @return True if this Entity is nested in another.
	 */
	public boolean isNested(){
	    return (_parentEntity != null);
	}
	
	/**
	 * 
	 * @param type the Type of the Component.
	 * @return the unique Component of the specified ComponentType.
	 */
	public Component getComponent(ComponentType type) {
	    return _components.get(type);
	}
	
	/**
	 * Adds a nested Entity to the Entity.
	 * Note: It will not work if you try do nest an Entity in itself, or a nested Entity of this 
	 * Entity.
	 * @param nestedEntity
	 * @throws AlreadyNestedException Is thrown when nestedEntity is already nested.
	 */
	void addNestedEntity(Entity nestedEntity) throws AlreadyNestedException{
	    if(this == nestedEntity)
		return;
	    //Checks if nestedEntity gets nested in a nested Entity of itself.
	    //NOTE: This could be a performance issue.
	    if(nestedEntity.getAllNestedEntities().contains(this))
		return;
	    if(nestedEntity.isNested())
		throw new AlreadyNestedException("This Entity is already nested");
	    nestedEntity._parentEntity = this;
	    this._nestedEntities.add(nestedEntity);
	}
	
	/**
	 * Removes a nested Entity from the Entity.
	 * Note: Nothing happens if the 
	 * @param nestedEntity 
	 */
	void removeNestedEntity(Entity nestedEntity) {
	    //This check is needed if the Entity is nested in another Entity.
	    if(_nestedEntities.contains(nestedEntity)) {
		nestedEntity._parentEntity = null;
		_nestedEntities.remove(nestedEntity);
	    }
	}
	
	/**
	 * Returns a ReadOnlyCollection of all Entities that are <u> directly </u> nested in this 
	 * Entity.
	 * @return 
	 */
	public ReadOnlyCollection<Entity> getNestedEntities() {
	    return new ReadOnlyCollection<>(this._nestedEntities);
	}
	
	/**
	 * Returns a ReadOnlyCollections of all Entities that are nested in this Entity, also
	 * returns the nested Entities of the nested Entities and so on...
	 * @return 
	 */
	public ReadOnlyCollection<Entity> getAllNestedEntities(){
	    return new ReadOnlyCollection<>(this.getAllNestedEntitiesHelper());
	}
	
	/**
	 * A Helper function for the function getAllNestedEntities.
	 * @return 
	 */
	private ArrayList<Entity> getAllNestedEntitiesHelper() {
	    ArrayList<Entity> resultQueue = this._nestedEntities;
	    int index = 0;
	    while(index < resultQueue.size()){
		Entity tempEntity = resultQueue.get(index);
		resultQueue.addAll(tempEntity.getAllNestedEntitiesHelper());
	    }
	    return resultQueue;
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
	    entityQue.addAll(_nestedEntities);
	    while(!entityQue.isEmpty()){
		Entity tempEntity = entityQue.poll();
		//Checks if there are nested Entities in nested the Entity and adds them to the que
		if(!tempEntity._nestedEntities.isEmpty()) {
		    entityQue.addAll(tempEntity._nestedEntities);
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
