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
	
	/**
	 * The list of all Parent-{@link Entity}s of all the registered {@link Entity}s indexed by
	 * their IDs. Every registered entity gets an entry in this list. If the {@link Entity} is null,
	 * the {@link Entity} has no parent, otherwise the list contains a reference to the parent. 
	 */
	private final IndexedCollection<Entity> _parents;
	
	/**
	 * The list of the children of all registered {@link Entity}s indexed by the IDs of their
	 * parents. Every registered {@link Entity} gets an entry in this list. The entries itself
	 * are also lists, which contain references to the children of the corresponding {@link Entity}.
	 */
	private final IndexedCollection<List<Entity>> _children;
	
	/**
	 * The list of the {@link Component}s of all entities, indexed by the id of the entity and the
	 * id of the {@link ComponentType}. 
	 */
	private final IndexedCollection<IndexedCollection<Component>> _components;
	
	/**
	 * The list of the {@link ComponentMask}s of all registered {@link Entity}s indexed by the
	 * IDs of the {@link Entity}s.
	 */
	private final IndexedCollection<ComponentMask> _componentMasks;
	
	/**
	 * The list of all registered {@link ComponentFactory}s indexed by the id of the type of
	 * {@link Component} they construct. The content of this list determines which
	 * {@link Component}s can be added to {@link Entity}s.
	 */
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
	 * Removes the specified {@link Entity} from this {@link EntityManager} and also all
	 * Child-{@link Entity}s of the specified {@link Entity}.
	 * 
	 * @param entity The {@link Entity} which should be removed.
	 * @return true if the {@link Entity} was removed, otherwise false.
	 */
	public boolean removeEntity(Entity entity) {
		int eId = entity.getId();
		if (_entities.remove(eId)) {
			_components.remove(eId);
			_componentMasks.remove(eId);
			for (Entity child : _children.get(eId))
				removeEntity(child);
			_children.set(eId, null);
			if (_parents.remove(eId)) {
				List<Entity> tChilds = _children.get(eId);
				tChilds.remove(eId);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Gets a list of all {@link Entity}s that contain the {@link Component}s defined by the
	 * specified {@link ComponentMask}.
	 * 
	 * @param mask The {@link ComponentMask} which defines the desired {@link Entity}s.
	 * @return A list of all relevant {@link Entity}s.
	 */
	public ReadOnlyCollection<Entity> getEntitiesByMask(ComponentMask mask) {
		List<Entity> result = new ArrayList<Entity>();
		for (Entity entity : _entities)
			if (entity.getComponentMask().contains(mask))
				result.add(entity);
		return new ReadOnlyCollection<Entity>(result);
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
		// check for null and also make sure nobody tries to add the entity to itself
		if (parent == null || child == null || child == parent)
			throw new IllegalArgumentException();
		// check if the entity is registered in the entity manager and try to get the list of
		// all the children it currently has
		List<Entity> tChilds = _children.get(parent.getId());
		if (tChilds == null)
			throw new IllegalArgumentException();
		// check if the child already has a parent
		if (_parents.get(child.getId()) != null)
			throw new AlreadyNestedException();
		// check if the child is in truth a parent of the 'parent'
		Entity parentOfParent = _parents.get(parent.getId());
		while (parentOfParent != null)
		{
			if (parentOfParent == child)
				throw new IllegalArgumentException();
			parentOfParent = _parents.get(parentOfParent.getId());
		}
		// finally add the child to the parent
		tChilds.add(child);
		_parents.set(child.getId(), parent);
	}
	
	/**
	 * Removes the specified Child-{@link Entity} from it's current Parent-{@link Entity}.
	 * This does not remove the Child-{@link Entity} itself, it only detaches the child from it's
	 * parent. If you want to remove the whole {@link Entity}, use <i>removeEntity</i> instead.
	 * 
	 * @param child The Child-{@link Entity} that should be removed from it's parent. 
	 */
	public boolean removeChildEntity(Entity child)
			throws IllegalArgumentException {
		Entity parent = _parents.get(child.getId());
		if (parent == null)
			return false;
		List<Entity> tChilds = _children.get(parent.getId());
		if (tChilds == null)
			throw new IllegalArgumentException();
		if (tChilds.remove(child)) {
			_parents.remove(child.getId());
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the Parent-{@link Entity} of the specified {@link Entity}.
	 * 
	 * @param entity The {@link Entity} which parent should be got.
	 * @return The parent of the specified {@link Entity} or null if the {@link Entity} has no
	 * 		parent.
	 */
	public Entity getParent(Entity entity) {
		return _parents.get(entity.getId());
	}
	
	/**
	 * Gets a read only list of all Child-{@link Entity}s of the specified {@link Entity}.
	 * 
	 * @param entity The {@link Entity} which children should be got.
	 * @return The list of all Child-{@link Entity}s.
	 */
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
	
	/**
	 * Gets the {@link Component} with the specified {@link ComponentType}  from the specified
	 * {@link Entity}.
	 * 
	 * @param entity The {@link Entity} which {@link Component} should be got.
	 * @param type The {@link ComponentType} of the {@link Component} which should be got.
	 * @return The desired {@link Component} or null if the type of {@link Component} was not part
	 * 		of the entity.
	 * @throws IllegalArgumentException When the {@link Entity} was not part of this
	 * 		{@link EntityManager}.
	 */
	public Component getComponent(Entity entity, ComponentType type)
			throws IllegalArgumentException {
		int eId = entity.getId();
		IndexedCollection<Component> ec = _components.get(eId);
		if (ec == null)
			throw new IllegalArgumentException();
		return ec.get(type.getId());
	}

	/**
	 * Removes the {@link Component} with the specified {@link ComponentType} from the specified
	 * {@link Entity}.
	 * 
	 * @param entity The {@link Entity} which {@link Component} should be removed.
	 * @param componentType The {@link ComponentType} of the {@link Component} which should be
	 * 		removed.
	 * @return true if the {@link Component} was removed, otherwise false.
	 * @throws IllegalArgumentException When the {@link Entity} was not part of this
	 * 		{@link EntityManager}.
	 */
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
	
	/**
	 * Gets the {@link ComponentMask} of the specified {@link Entity}.
	 * 
	 * @param entity The {@link Entity} which {@link ComponentMask} should be got.
	 * @return The {@link ComponentMask} of the {@link Entity}.
	 */
	public ComponentMask getComponentMask(Entity entity) {
		return _componentMasks.get(entity.getId());
	}
}
