package infinity.ecs.core;

public class Entity2 {
	final int _id;
	final EntityManager2 _em;
	
	Entity2(int id, EntityManager2 em) {
		_id = id;
		_em = em;
	}
	
	public void addComponent(Component c) {
		_em.addComponents(_id, c);
	}
	
	public void addComponents(Component... c) {
		_em.addComponents(_id, c);
	}
	
	public Component getComponent(ComponentType type) {
		return _em.getComponent(_id, type);
	}
	
	public boolean removeComponent(ComponentType type) {
		return _em.removeEntity(_id);
	}
}
