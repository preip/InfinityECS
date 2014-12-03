package infinity.ecs.core;

import java.util.Iterator;
import java.util.HashMap;

/**
 * ComponentType hold a unique for the type of a Component and some static utility functions for
 * getting the specific id of the ComponentType of a Component.
 * 
 * @author preip
 * @version 1.0 
 */
public final class ComponentType {

	/**
	 * Contains all current componentTypes indexed by their class.
	 */
	private static final HashMap<Class<? extends Component>, 
		ComponentType> _classLib = new HashMap<>();

	/**
	 * Gets the ComponentType based on the specified Component instance.
	 *
	 * @param Component The Component instance for this the ComponentType should be got.
	 * @return The resulting ComponentType.
	 */
	public static ComponentType get(Component component) {
		// lets just call get() for the class type and hope the compiler inlines it
		return get(component.getClass());
	}

	/**
	 * Gets the ComponentType based on the specified Component class.
	 *
	 * @param typeClass The Component class for this the ComponentType should be got.
	 * @return The resulting ComponentType.
	 */
	public static ComponentType get(Class<? extends Component> typeClass) {
		ComponentType cType = _classLib.get(typeClass);
		if (cType == null) {
			cType = new ComponentType(_classLib.size());
			_classLib.put(typeClass, cType);
		}
		return cType;
	}

	/**
	 * Tries to get the ComponentType based on the specified type ID.
	 * WARNING: comparatively slow to getting ComponentTypes by their class.
	 *
	 * @param typeId The ID for which the ComponentType should be got.
	 * @return The resulting ComponentType or null if the id was not found.
	 */
	public static ComponentType get(int typeId) {
		Iterator<ComponentType> it = _classLib.values().iterator();
		while (it.hasNext()) {
			ComponentType c = it.next();
			if (c._id == typeId) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Gets the ComponentType ID based on the specified Component class.
	 *
	 * @param typeClass The Component class for this the ID should be got.
	 * @return The resulting type ID.
	 */
	public static int getId(Class<? extends Component> typeClass) {
		return get(typeClass)._id;
	}

	/**
	 * The unique id of this ComponentType.
	 */
	private final int _id;

	/**
	 * Creates a new instance of the ComponentType class. No public access
	 * allowed, because type IDs must be managed centrally.
	 *
	 * @param id The id of this ComponentType.
	 */
	private ComponentType(int id) {
		_id = id;
	}

	/**
	 * Gets the ID of this ComponentType.
	 *
	 * @return The resulting type ID.
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Checks if this ComponentType is equal to the specified object.
	 *
	 * @param obj The object this ComponentType is compared with.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ComponentType other = (ComponentType) obj;
		return _id == other._id;
	}

	/**
	 * Gets the hash code of this ComonentType.
	 *
	 * @return The resulting hash code.
	 */
	@Override
	public int hashCode() {
		// the has code is just the id. This should make sure, 
		// every componenetType is different from each other
		return _id;
	}
}
