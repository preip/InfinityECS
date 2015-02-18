package infinity.ecs.core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ComponentTypes act as unique identifiers for every type of {@link Component}, by which they can
 * be distinguished. ComponentTypes therefore also act as the basis for {@link ComponentMask}.
 * 
 * @author preip
 * @version 1.0 
 */
public final class ComponentType {
	
	//----------------------------------------------------------------------------------------------
	// Static Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Contains all current {@link ComponentType}s indexed by their class for lookup.
	 */
	private static final HashMap<Class<? extends Component>, ComponentType> _classLib
		= new HashMap<>();
	
	/**
	 * A list of all existing {@link Component}s, sorted by their id.
	 * Is used to look up {@link ComponentType}s based on their id.
	 * 
	 * NOTE: The list is in fact never actually sorted, but since component types and therefore
	 * their id never become invalid during runtime, every component that is added to the list is
	 * therefore automatically at the same position as it's ID, since each new ID, that is issued
	 * to a component type is simply the current length of the component list.
	 */
	private static final ArrayList<ComponentType> _cTypes
		= new ArrayList<ComponentType>();

	//----------------------------------------------------------------------------------------------
	// Static Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Gets the {@link ComponentType} based on the specified {@link Component} instance.
	 *
	 * @param component The {@link Component} instance for which the {@link ComponentType} should be got.
	 * @return The resulting {@link ComponentType}.
	 */
	public static ComponentType get(Component component) {
		// lets just call get() for the class type and hope the compiler inlines it
		return get(component.getClass());
	}

	/**
	 * Gets the {@link ComponentType} based on the specified {@link Component} class.
	 *
	 * @param typeClass The {@link Component} class for which the {@link ComponentType} should
	 * 		be got.
	 * @return The resulting {@link ComponentType}.
	 */
	public static ComponentType get(Class<? extends Component> typeClass) {
		// try to get the component type, assuming there already is an entry for the class
		ComponentType cType = _classLib.get(typeClass);
		// if not,
		if (cType == null) {
			// a new one must be created
			cType = new ComponentType(_classLib.size());
			// and added to the class library, to enable the class based lookup
			_classLib.put(typeClass, cType);
			// and also to the component type list to enable the lookup based on IDs
			_cTypes.add(cType);
		}
		return cType;
	}

	/**
	 * Tries to get the {@link ComponentType} based on the specified type ID.
	 *
	 * @param typeId The ID for which the {@link ComponentType} should be got.
	 * @return The resulting {@link ComponentType} or null if the id was not found.
	 */
	public static ComponentType get(int typeId) {
		if (typeId < 0 || typeId >= _cTypes.size())
			return null;
		return _cTypes.get(typeId);
	}

	/**
	 * Gets the {@link ComponentType} ID based on the specified {@link Component} class.
	 *
	 * @param typeClass The {@link Component} class for which the ID should be got.
	 * @return The resulting type ID.
	 */
	public static int getId(Class<? extends Component> typeClass) {
		return get(typeClass)._id;
	}
	
	//----------------------------------------------------------------------------------------------
	// Private Fields
	//----------------------------------------------------------------------------------------------

	/**
	 * The unique id of this {@link ComponentType}.
	 */
	private final int _id;
	
	//----------------------------------------------------------------------------------------------
	// Constructors
	//----------------------------------------------------------------------------------------------

	/**
	 * Creates a new instance of the {@link ComponentType} class. No public access allowed, because
	 * type IDs must be managed centrally.
	 *
	 * @param id The id of this {@link ComponentType}.
	 */
	private ComponentType(int id) {
		_id = id;
	}

	//----------------------------------------------------------------------------------------------
	// Public Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Gets the ID of this {@link ComponentType}.
	 *
	 * @return The resulting type ID.
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Checks if this {@link ComponentType} is equal to the specified object.
	 *
	 * @param obj The object this {@link ComponentType} is compared with.
	 * @return 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentType other = (ComponentType) obj;
		return _id == other._id;
	}

	/**
	 * Gets the hash code of this {@link ComponentType}.
	 *
	 * @return The resulting hash code.
	 */
	@Override
	public int hashCode() {
		// the hash code is just the ID. This should make sure every componenetType has a unique
		// hash value, since the IDs themselves are unique.
		return _id;
	}
}
