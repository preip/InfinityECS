package infinity.ecs.core;

import infinity.ecs.exceptions.AlreadyNestedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import infinity.ecs.utils.IdPool;
import infinity.ecs.utils.ReadOnlyCollection;
import infinity.ecs.exceptions.ComponentAlreadyExistsException;
import infinity.ecs.exceptions.EntityDoesNotExistsException;
import infinity.ecs.exceptions.InfinityException;

/**
 * Class which manages a set of entities. It can be used to create new entities with unique IDs,
 * add new components to an entity, get lists of entities which match a specific component mask
 * etc.  
 * 
 * @author preip, simon
 * @version 0.1
 */
public class EntityManager {
	
	//----------------------------------------------------------------------------------------------
	// Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The only instance of the EntityManager.
	 * TODO: Why is EntityManager a Singleton???
	 */
	private static final EntityManager _instance = new EntityManager();
	
	/**
	 * The IdPool used by this entity manager to generate IDs for new entities.
	 */
	private final IdPool _idPool;
	
	/**
	 * The list of all entities managed by this EntityManager indexed by their IDs. 
	 */
	private final HashMap<Integer, Entity> _entities;
	
	/**
	 * A mapping of a specific components mask and a list of all entities which contain that mask. 
	 */
	private final HashMap<ComponentMask, ArrayList<Entity>> _entitiesByMask;

	//----------------------------------------------------------------------------------------------
	// Constructors
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Creates a new instance of the EntityManager class.
	 */
	private EntityManager() {
		_idPool = new IdPool();
		_entities = new HashMap<>();
		_entitiesByMask = new HashMap<>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * 
	 * @return The only instance of EntityManager.
	 */
	public static EntityManager getEntityManager() {
	    return _instance;
	}
	
	/**
	 * Creates a list of all entities that contain the specified component mask.
	 * NOTE: The list is only created and not added to anything.
	 * @param mask The mask for which the list should be created.
	 * @return The resulting list of entities that contain the mask.
	 */
	private ArrayList<Entity> createNewEntityListByMask(ComponentMask mask) {
		ArrayList<Entity> result = new ArrayList<>();
		// NOTE: According to some random benchmarks on the Internet, iterating over the entry sets
		// is faster than iterating over the sources only. Thats why the whole set is used, but
		// the key is of no interest for the operation.
		Iterator<Entry<Integer, Entity>> it = _entities.entrySet().iterator();
		while(it.hasNext()) {
			Entity entity = it.next().getValue();
			// check for each entry, if its components match the specified mask
			if (entity.getComponentMask().contains(mask))
				result.add(entity);
		}
		return result;
	}
	
	
	/**
	 * Creates a new, empty Entity with a unique ID.
	 * @return The created Entity.
	 */
	public Entity createEntity() {
		int id = _idPool.getId();
		Entity entity = new Entity(id, null);
		_entities.put(id, entity);
		return entity;
	}
	
	/**
	 * 
	 * @param id The unique id of the Entity requested.
	 * @return
	 * @throws EntityDoesNotExistsException 
	 */
	public Entity getEntity(Integer id) throws EntityDoesNotExistsException {
	    Entity entity = _entities.get(id);
	    if(entity == null)
	    	throw new EntityDoesNotExistsException();
	    return entity;
	}
	
	/**
	 * Adds a new nested Entity to a super Entity, removes the nested Entity from _parentEntities
 and _parentEntitiesByMask
	 * @param parentEntity
	 * @param childEntity
	 * @throws infinity.ecs.exceptions.AlreadyNestedException
	 */
	public void addChildEntity(Entity parentEntity, Entity childEntity)
			throws AlreadyNestedException {
	    parentEntity.addChildEntity(childEntity);
	}
	
	/**
	 * Removes a nested Entity from a super Entity, adds the nested Entity to _parentEntities and
 _superEntititesByMask.
	 * @param parentEntity
	 * @param childEntity 
	 */
	public void removeChildEntity(Entity parentEntity, Entity childEntity) {
	    parentEntity.removeChildEntity(childEntity);
	}
	
	/**
	 * Adds new Components to a Entity and updates _entitiesByMask.
	 * @param entity
	 * @param components
	 * @throws ComponentAlreadyExistsException 
	 */
	public void addComponents(Entity entity, Component... components) 
		throws ComponentAlreadyExistsException {
	    entity.addComponents(components);
	    ComponentMask componentMask = entity.getComponentMask();
	    ArrayList<Entity> componentMaskList = _entitiesByMask.get(componentMask);
	    if(componentMaskList != null)
	    	componentMaskList.remove(entity);
	    
	    entity.addComponents(components);
	    
	    componentMaskList = _entitiesByMask.get(entity.getComponentMask());
	    if(componentMaskList == null)
	    	componentMaskList = new ArrayList<>();
	    componentMaskList.add(entity);
	}
	
	/**
	 * Adds new Components to an Entity and updates _entitiesByMask.
	 * @param id
	 * @param components
	 * @throws InfinityException 
	 */
	public void addComponents(Integer id, Component... components) throws InfinityException{
	    Entity entity = this.getEntity(id);
	    this.addComponents(entity, components);
	}
	
	/**
	 * Gets all entities that contain the components defined by the specified mask.
	 * @param mask The mask for which an entity list should be got.
	 * @return The resulting list of entities which match the mask.
	 */
	public ReadOnlyCollection<Entity> getMatchingEntities(ComponentMask mask) {
		// try to get the entity list from the stored ones
		ArrayList<Entity> list = _entitiesByMask.get(mask);
		// if a query for the specified mask was not already in storage, create a new one and
		// save it for further queries
		if (list == null) {
			list = createNewEntityListByMask(mask);
			// Clone the mask, so changes to the original mask won't change the index mask
			// (which would probably screw up the index)
			_entitiesByMask.put(new ComponentMask(mask), list);
		}
		// finally, return not the list itself but a read only variant to prevent modifications
		// NOTE: if every call of this method would result in a new list being created, there
		// would be no need for a read only collection, but that would be sub-optimal from a
		// performance point of view. So would cloning the existing list. Therefore a read only
		// collection seems the way to go.
		// TODO: It would be even better to store a single ReadOnlyCollection instance for each
		// list in the first place, instead if creating a new one every time. While the overhead
		// from creating a new collection should be minimal, its an overhead nonetheless and the
		// collection is read only anyway, so there would be no harm in handing each caller the
		// same instance.
		return new ReadOnlyCollection<>(list);
	}
}
