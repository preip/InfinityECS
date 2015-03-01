package infinity.ecs.core;

import infinity.ecs.exceptions.EntityDoesNotExistsException;
import infinity.ecs.utils.IdPool;
import infinity.ecs.utils.IndexedCollection;

public class EntityManager2 {
	private final IdPool _idPool;
	
	private final IndexedCollection<Entity2> _entities;
	private final IndexedCollection<IndexedCollection<Component>> _components;
	
	public EntityManager2() {
		_idPool = new IdPool();
		_entities = new IndexedCollection<Entity2>();
		_components = new IndexedCollection<IndexedCollection<Component>>();
	}
	
	public Entity2 createEntity() {
		int id = _idPool.getId();
		Entity2 e = new Entity2(id, this);
		_entities.set(id, e);
		_components.set(id, new IndexedCollection<Component>());
		return e;
	}
	
	public Entity2 getEntity(int id) throws EntityDoesNotExistsException {
		Entity2 e = _entities.get(id);
		if (e == null)
			throw new EntityDoesNotExistsException();
		return e;
	}
	
	public boolean removeEntity(int id) {
		if (_entities.remove(id)) {
			_components.remove(id);
			return true;
		}
		return false;
	}
	
	public void addComponents(int id, Component... components) {
		IndexedCollection<Component> ec = _components.get(id);
		if (ec == null)
			throw new IllegalArgumentException();
		for (Component component : components) {
			int cId = component.getComponentType().getId();
			ec.set(cId, component);
		}
	}
	
	public Component getComponent(int id, ComponentType type) {
		IndexedCollection<Component> ec = _components.get(id);
		if (ec == null)
			throw new IllegalArgumentException();
		return ec.get(type.getId());
	}
	
	public boolean removeComponent(int id, ComponentType type) {
		IndexedCollection<Component> ec = _components.get(id);
		if (ec == null)
			throw new IllegalArgumentException();
		return ec.remove(type.getId());
	}
}
