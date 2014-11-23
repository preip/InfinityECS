package infinity.ecs.core;

public class Entity {
	private final int _id;
	
	/**
	 * Package private constructor, which initializes the entity with the specified id.
	 * Can't be public, or IDs were no longer be guaranteed to be unique.
	 * @param id
	 */
	Entity(int id) {
		_id = id;
	}
	
	public int getId() {
		return _id;
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
