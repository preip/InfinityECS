package infinity.ecs.messaging;

import java.util.HashMap;

/**
 * MessageTypes act as unique identifiers for every type of {@link Message}, by which they can
 * be distinguished.
 * 
 * @author preip
 */
public class MessageType {
	
	//----------------------------------------------------------------------------------------------
	// Static Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Contains all current message types indexed by their class for lookup.
	 */
	private static final HashMap<Class<? extends Message>, MessageType> _classLib
		= new HashMap<Class<? extends Message>, MessageType>();
	
	//----------------------------------------------------------------------------------------------
	// Static Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Gets the MessageType based on the specified Message instance.
	 *
	 * @param component The Message instance for which the MessageType should be got.
	 * @return The resulting MessageType.
	 */
	public static MessageType get(Message component) {
		// lets just call get() for the class type and hope the compiler inlines it
		return get(component.getClass());
	}

	/**
	 * Gets the MessageType based on the specified Message class.
	 *
	 * @param typeClass The Message class for which the MessageType should be got.
	 * @return The resulting MessageType.
	 */
	public static MessageType get(Class<? extends Message> typeClass) {
		// try to get the message type, assuming there already is an entry for the class
		MessageType mType = _classLib.get(typeClass);
		// if not,
		if (mType == null) {
			// a new one must be created
			mType = new MessageType(_classLib.size());
			// and added to the class library, to enable the class based lookup
			_classLib.put(typeClass, mType);
		}
		return mType;
	}
	
	//----------------------------------------------------------------------------------------------
	// Private Fields
	//----------------------------------------------------------------------------------------------

	/**
	 * The unique id of this MessageType.
	 */
	private final int _id;
	
	//----------------------------------------------------------------------------------------------
	// Constructors
	//----------------------------------------------------------------------------------------------

	/**
	 * Creates a new instance of the MessageType class. No public access allowed, because type
	 * IDs must be managed centrally.
	 *
	 * @param id The id of this MessageType.
	 */
	private MessageType(int id) {
		_id = id;
	}
	
	//----------------------------------------------------------------------------------------------
	// Public Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Gets the ID of this MessageType.
	 *
	 * @return The resulting type ID.
	 */
	public int getId() {
		return _id;
	}
	
	/**
	* Checks if this MessageType is equal to the specified object.
	*
	* @param obj The object this MessageType is compared with.
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
		MessageType other = (MessageType) obj;
		return _id == other._id;
	}
	
	/**
	* Gets the hash code of this MessageType.
	*
	* @return The resulting hash code.
	*/
	@Override
	public int hashCode() {
		// the hash code is just the ID. This should make sure every messageType has a unique
		// hash value, since the IDs themselves are unique.
		return _id;
	}
}
