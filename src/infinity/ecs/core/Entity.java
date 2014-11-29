package infinity.ecs.core;

import java.util.HashMap;
import infinity.ecs.exceptions.ComponentAlreadyExistsException;

public class Entity {
	private final int _id;
        private final HashMap<ComponentType,
                Class<? extends Component>> _components = new HashMap<>();
	
	/**
	 * Package private constructor, which initializes the entity with the 
         * specified id. Can't be public, or IDs were no longer be guaranteed 
         * to be unique
	 * @param id
	 */
	Entity(int id) {
		_id = id;
        }
	
	public int getId() {
		return _id;
	}
        
        public Class<? extends Component> getComponent(ComponentType type){
            return _components.get(type);
        }
      
        /**
         * Adds new components to the entity and throws an exception if they
         * already exist in this entity.
         * @param components the components you wish to add
         * @throws ComponentAlreadyExistsException 
         */
        //TODO: We need some check to know if there can only be a single component of this type
        public void addComponent(Class<? extends Component>... components) 
                throws ComponentAlreadyExistsException {
            for(Class<? extends Component> component : components){
                ComponentType type = ComponentType.get(component);
                if(!_components.containsKey(type)){
                    _components.put(type,component);
                }
                else{
                    throw new ComponentAlreadyExistsException();
                }
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
