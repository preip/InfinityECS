package infinity.ecs.core;

import java.util.ArrayList;
import java.util.List;

import infinity.ecs.utils.IdPool;
import infinity.ecs.utils.IndexedCollection;
import infinity.ecs.utils.ReadOnlyCollection;
import infinity.ecs.exceptions.AlreadyNestedException;
import infinity.ecs.exceptions.ComponentAlreadyExistsException;
import infinity.ecs.exceptions.EntityDoesNotExistsException;

/**
 * Class which manages a set of {@link Entity}s. It can be used to create new entities with unique
 * IDs, add new {@link Component}s to an entity, etc.  
 * 
 * @author preip, simon
 */
public class EntityManager {
	
	//----------------------------------------------------------------------------------------------
	// Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The IdPool used by this entity manager to generate IDs for new entities.
	 */
	private final IdPool _idPool;
	
	/**
	 * The list of all entities managed by this {@link EntityManager} indexed by their IDs. 
	 */
	private final IndexedCollection<Entity> _entities;
	
	private final IndexedCollection<Entity> _parents;
	
	private final IndexedCollection<List<Entity>> _children;
	
	/**
	 * The list of the {@link Component}s of all entities, indexed by the id of the entity and the
	 * id of the {@link ComponentType}. 
	 */
	private final IndexedCollection<IndexedCollection<Component>> _components;
	
	private final IndexedCollection<ComponentMask> _componentMasks;
	
	private final IndexedCollection<ComponentFactory> _factories;

	//----------------------------------------------------------------------------------------------
	// Constructors
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Creates a new instance of the {@link EntityManager} class.
	 */
	public EntityManager() {
		_idPool = new IdPool();
		_entities = new IndexedCollection<Entity>();
		_parents = new IndexedCollection<Entity>();
		_children = new IndexedCollection<List<Entity>>();
		_components = new IndexedCollection<IndexedCollection<Component>>();
		_componentMasks = new IndexedCollection<ComponentMask>();
		_factories = new IndexedCollection<ComponentFactory>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Entity related methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Creates a new, empty Entity with a unique ID.
	 * @return The created Entity.
	 */
	public Entity createEntity() {
		int id = _idPool.getId();
		Entity entity = new Entity(id, this);
		_entities.set(id, entity);
		_children.set(id, new ArrayList<Entity>());
		_components.set(id, new IndexedCollection<Component>());
		_componentMasks.set(id, new ComponentMask());
		return entity;
	}
	
	/**
	 * Tries to get the {@link Entity} with the specified id.
	 * 
	 * @param id The unique id of the {@link Entity} requested.
	 * @return The desired {@link Entity} or <i>null</i> if it was not found.
	 */
	public Entity getEntity(Integer id) throws EntityDoesNotExistsException {
		Entity entity = _entities.get(id);
		if(entity == null)
			throw new EntityDoesNotExistsException();
		return entity;
	}
	
	/**
	 * Removes the specified {@link Entity} from this {@link EntityManager}.
	 * @param entity The {@link Entity} which should be removed.
	 * @return true if the {@link Entity} was removed, otherwise false.
	 */
	public boolean removeEntity(Entity entity) {
		int eId = entity.getId();
		if (_entities.remove(eId)) {
			_components.remove(eId);
			_componentMasks.remove(eId);
			// TODO: Remove all children too
			return true;
		}
		return false;
	}
		
	//----------------------------------------------------------------------------------------------
	// Child-/Parent-Entity related methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Adds a new nested Entity to a super Entity, removes the nested Entity from _parentEntities
	 * and _parentEntitiesByMask
	 * 
	 * @param parent
	 * @param child
	 * @throws infinity.ecs.exceptions.AlreadyNestedException
	 */
	public void addChildEntity(Entity parent, Entity child)
			throws IllegalArgumentException, AlreadyNestedException {
		List<Entity> tChilds = _children.get(parent.getId());
		if (tChilds == null)
			throw new IllegalArgumentException();
		if (_parents.get(child.getId()) != null)
			throw new AlreadyNestedException();
		tChilds.add(child);
		_parents.set(child.getId(), parent);
	}
	
	/**
	 * Removes a nested Entity from a super Entity, adds the nested Entity to _parentEntities and
	 * _superEntititesByMask.
	 * 
	 * @param parentEntity
	 * @param childEntity 
	 */
	public boolean removeChildEntity(Entity parent, Entity child)
			throws IllegalArgumentException {
		List<Entity> tChilds = _children.get(parent.getId());
		if (tChilds == null)
			throw new IllegalArgumentException();
		if (tChilds.remove(child)) {
			_parents.remove(child.getId());
			return true;
		}
		return false;
	}
	
	public Entity getParent(Entity entity) {
		return _parents.get(entity.getId());
	}
	
	public ReadOnlyCollection<Entity> getChildren(Entity entity) {
		List<Entity> tChilds = _children.get(entity.getId());
		if (tChilds == null)
			throw new IllegalArgumentException();
		return new ReadOnlyCollection<>(tChilds);
	}
	
	//----------------------------------------------------------------------------------------------
	// Component related methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Registers the specified {@link ComponentFactory} with this {@link EntityManager}. All
	 * {@link Component}s of the {@link ComponentType} specified by the factory are now constructed
	 * by that factory. Any previously existing factories for the same type are overridden.
	 * 
	 * @param factory The {@link ComponentFactory} which should be registered.
	 * @throws IllegalArgumentException when factory was null.
	 */
	public void registerComponentFactory(ComponentFactory factory)
			throws IllegalArgumentException {
		if (factory == null)
			throw new IllegalArgumentException();
		int id = factory.getComponentType().getId();
		_factories.set(id, factory);
	}
	
	/**
	 * Adds a new {@link Component} of the specified type to the specified {@link Entity}.
	 * 
	 * @param entity The {@link Entity} to which the {@link Component} should be added.
	 * @param componentType The type of the {@link Component} that should be added.
	 * @return The {@link Component} which has been created.
	 * @throws IllegalArgumentException When the specified {@link Entity} was not part of this
	 * 		{@link EntityManager} or if a new instance of the specified type of {@link Component}
	 * 		could not be constructed.
	 * @throws ComponentAlreadyExistsException when the {@link Component} which should be added is
	 * 		already part of the {@link Entity}.
	 */
	public Component addComponent(Entity entity, ComponentType componentType)
			throws IllegalArgumentException, ComponentAlreadyExistsException {
		int eId = entity.getId();
		// try to get the list of all components for the specified entity
		IndexedCollection<Component> ec = _components.get(eId);
		if (ec == null)
			throw new IllegalArgumentException();
		
		int cId = componentType.getId();
		// check if there is already a component of the same type
		if (ec.get(cId) != null)
			throw new ComponentAlreadyExistsException();
		// try to get the factory which constructs components of the specified type
		ComponentFactory fac = _factories.get(cId);
		if (fac == null)
			throw new IllegalArgumentException();
		
		Component c = fac.createNewComponent();
		ec.set(cId, c);
		c.bind(entity);
		_componentMasks.get(eId).add(componentType);
		return c;
	}
	
	/**
	 * Adds new {@link Component}s of the specified types to the specified {@link Entity}.
	 * 
	 * @param entity The {@link Entity} to which the {@link Component}s should be added.
	 * @param componentTypes The types of {@link Component}s that should be added.
	 * @throws IllegalArgumentException When the specified {@link Entity} was not part of this
	 * 		{@link EntityManager} or if a new instance of one of the specified types of
	 * 		{@link Component}s could not be constructed.
	 * @throws ComponentAlreadyExistsException when one of the {@link Component}s which should be
	 * 		added is already part of the {@link Entity}.
	 */
	public void addComponents(Entity entity, ComponentType... componentTypes)
			throws IllegalArgumentException, ComponentAlreadyExistsException {
		int eId = entity.getId();
		// try to get the list of all components for the specified entity
		IndexedCollection<Component> ec = _components.get(eId);
		if (ec == null)
			throw new IllegalArgumentException();
		
		for (ComponentType componentType : componentTypes) {
			int cId = componentType.getId();
			// check if there is already a component of the same type
			if (ec.get(cId) != null)
				throw new ComponentAlreadyExistsException();
			// try to get the factory which constructs components of the specified type
			ComponentFactory fac = _factories.get(cId);
			if (fac == null)
				throw new IllegalArgumentException();
			
			Component c = fac.createNewComponent();
			ec.set(cId, c);
			c.bind(entity);
			_componentMasks.get(eId).add(componentType);
		}
	}
	
	public Component getComponent(Entity entity, ComponentType type) {
		int eId = entity.getId();
		IndexedCollection<Component> ec = _components.get(eId);
		if (ec == null)
			throw new IllegalArgumentException();
		return ec.get(type.getId());
	}

	public boolean removeComponent(Entity entity, ComponentType componentType) {
		int eId = entity.getId();
		IndexedCollection<Component> ec = _components.get(eId);
		if (ec == null)
			throw new IllegalArgumentException();
		if (ec.remove(componentType.getId())) {
			_componentMasks.get(eId).remove(componentType);
			return true;
		}
		return false;
	}
	
	public ComponentMask getComponentMask(Entity entity) {
		return _componentMasks.get(entity.getId());
	}
}
