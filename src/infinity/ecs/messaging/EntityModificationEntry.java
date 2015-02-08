package infinity.ecs.messaging;

import java.util.ArrayList;
import java.util.List;

import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;

/**
 * Simple data class which holds entry modification information. Is used by the
 * {@link EntityModificationEndpoint} to store which entities and components within the entities
 * have been changed. 
 * 
 * @author preip
 */
public final class EntityModificationEntry {
	
	//----------------------------------------------------------------------------------------------
	// Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The entity which was modified. 
	 */
	private final Entity _entity;
	
	/**
	 * A list of component types representing the component that have changed within the entry
	 */
	private final List<ComponentType> _cTypes; // could use a bit array instead
	
	//----------------------------------------------------------------------------------------------
	// Constructors
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Creates a new instance of the EntityModificationEntry class.
	 * Package-Private, new0 entries can only be created by endpoints.
	 * 
	 * @param entity The entity which has been modified.
	 * @param componentTypes A list of component types representing the component that have changed
	 * 		  within the entry
	 */
	EntityModificationEntry(Entity entity, ComponentType... componentTypes) {
		// new entries are only created by the endpoints, so there is no need for error handling
		_entity = entity;
		_cTypes = new ArrayList<ComponentType>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Get the entity which has been modified. 
	 * 
	 * @return The entity.
	 */
	public Entity getEntity() {
		return _entity;
	}
	
	/**
	 * Get the list of component types representing the component that have changed within the
	 * entry.
	 * 
	 * @return The list of component types
	 */
	public List<ComponentType> getComponentTypes() {
		// There is no need to return a copy or read-only version of the list, because the moment
		// entries are pulled from an endpoint, they are removed from the internal endpoint queue
		// anyway. So if the list gets modified thereafter, it won't influence other code parts.   
		return _cTypes;
	}
}