package infinity.ecs.core;

import java.util.ArrayList;
import java.util.HashMap;

import infinity.ecs.exceptions.ComponentAlreadyExistsException;
import infinity.ecs.utils.ReadOnlyCollection;

/**
 * 
 * @author preip, Simon
 * @version 0.1
 */
public class Entity {
	private final int _id;
	// CHANGE: This was original a HashMap<ComponentType, Class<? extends Component>>,
	// which seems illogical. Changed to contain actual components, not just their types.
	// CHANGE: Also changed the type so that it contains lists of entities.
    private final HashMap<ComponentType, ArrayList<Component>> _components;
	
	/**
	 * Package private constructor, which initializes the entity with the 
     * specified id. Can't be public, or IDs were no longer be guaranteed 
     * to be unique
	 * @param id The unique id of the entity.
	 */
	Entity(int id) {
		_id = id;
		_components = new HashMap<ComponentType, ArrayList<Component>>();
	}
	
	public int getId() {
		return _id;
	}
	
	// CHANGE: This was original returning Class<? extends Component>, which seems illogical.
	// Changed to return the actual component
	public Component getComponent(ComponentType type) {
		ArrayList<Component> tmp = _components.get(type);
		if (tmp == null || tmp.size() == 0)
			return null;
		return tmp.get(0);
	}
	
	public ReadOnlyCollection<Component> getComponents(ComponentType type) {
		return new ReadOnlyCollection<Component>(_components.get(type));
	}
      
    /**
     * Adds new components to the entity.
     * @param components the components you wish to add
     * @throws ComponentAlreadyExistsException If the component that should be added is unique and
     * the entity already contains a component of the same type.
     */
    // TODO: We need some check to know if there can only be a single component of this type
	// TODO: make this package private and only allow new components to be added to the entity
	// by the EntityManager (so it can track all changes to and entity and notify all systems
	// accordingly
	// CHANGE: changed this to use actual components, not just component types
    public void addComponents(Component ... components) 
            throws ComponentAlreadyExistsException {
        for(Component component : components) {
            ComponentType type = ComponentType.get(component);
            ArrayList<Component> tCmp = _components.get(type);
            // if the component which should be added is unique and the entity already contains
            // an instance of the same type, throw an exception, because adding the component
            // would result in an invalid state
            if (component.isUnique() && tCmp != null && tCmp.size() > 0)
            	throw new ComponentAlreadyExistsException
            		("Component is unique and the entity already contains an instance.");
            // if there was no component list for the specified type, create one
            if (tCmp == null) {
        		tCmp = new ArrayList<Component>();
        		_components.put(type, tCmp);
            }
            // finally, add the component to the list
            tCmp.add(component);
        }
    }
        
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
