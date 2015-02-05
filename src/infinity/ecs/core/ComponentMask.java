package infinity.ecs.core;

import infinity.ecs.utils.BitArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Can be used to create an immutable mask that resembles a group of several types of components.
 * Can be compared to other masks to find matches etc.
 * <p>
 * <b>Notes:</b><br>
 * Tight wrapper around {@link BitArray} class. Should be used by Entities and  Systems to check if
 * the requirements from to Systems to the Entities are  matched.
 * <p>
 * Manipulation of the masks after their initial creation is only allowed by core members. This
 * enables entities to quickly alter their masks if they  gain or loose components without the need
 * to create a completely new mask.
 * 
 * Manipulation is not exposed outside of the core to prevent other objects from changing the mask
 * of an entity in a way that results in an invalid state, because the mask no longer resembles the
 * components that entity contains. All operations performed by core members are guaranteed to
 * leave the mask in a valid state all the time.
 * 
 * @author preip, Simon
 * @version 1.0
 */
public class ComponentMask {
	
	//----------------------------------------------------------------------------------------------
	// Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The BitArray that acts as the base for the component mask and stores the id of every
	 * ComponentType that is part of the map.
	 */
	private final BitArray _bitArray;
	
	//----------------------------------------------------------------------------------------------
	// Constructors
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Copy constructor.
	 * @param copy The componentMask which should be cloned.
	 */
	public ComponentMask(ComponentMask copy) {
		_bitArray = copy._bitArray.clone();
	}

	/**
	 * Creates a new instance of the ComponentMask class.
	 */
	public ComponentMask() {
		_bitArray = new BitArray();
	}

	/**
	 * Creates a new instance of the ComponentMask class.
	 * 
	 * @param componentTypes The list of component types that the mask should resemble.
	 */
	public ComponentMask(ComponentType... componentTypes) {
		_bitArray = new BitArray();
		add(componentTypes);
	}

	/**
	 * Creates a new instance of the ComponentMask class.
	 * 
	 * @param componentTypes The list of component types that the mask should resemble.
	 */
	public ComponentMask(Collection<ComponentType> componentTypes) {
		_bitArray = new BitArray();
		add(componentTypes);
	}

	//----------------------------------------------------------------------------------------------
	// Package-Private Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Add the list of specified ComponentTypes to this mask.
	 * 
	 * @param componentTypes The list of ComponentTypes that should be added.
	 */
	final void add(Collection<ComponentType> componentTypes) {
		Iterator<ComponentType> iter = componentTypes.iterator();
		while(iter.hasNext())
			_bitArray.set(iter.next().getId(), true);
	}
	
	/**
	 * Add the list of specified ComponentTypes to this mask.
	 * 
	 * @param componentTypes The list of ComponentTypes that should be added.
	 */
	final void add(ComponentType... componentTypes) {
		for (ComponentType componentType : componentTypes)
			_bitArray.set(componentType.getId(), true);
	}
	
	/**
	 * Removed the list of specified ComponentTypes from this mask.
	 * 
	 * @param componentTypes The list of ComponentTypes that should be removed.
	 */
	final void remove(Collection<ComponentType> componentTypes) {
		Iterator<ComponentType> iter = componentTypes.iterator();
		while(iter.hasNext())
			_bitArray.set(iter.next().getId(), false);            
	}
	
	/**
	 * Removed the list of specified ComponentTypes from this mask.
	 * 
	 * @param componentTypes The list of ComponentTypes that should be removed.
	 */
	final void remove(ComponentType... componentTypes) {
		for (ComponentType componentType : componentTypes)
			_bitArray.set(componentType.getId(), false);
	}
	
	//----------------------------------------------------------------------------------------------
	// Public Methods
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Checks, if the component mask contains all specified ComponentTypes.
	 * 
	 * @param componentTypes The ComponentTypes which should be checked.
	 * @return true if the mask contains all specified types, otherwise false.
	 */
	public boolean contains(ComponentType... componentTypes) {
		for (ComponentType componentType : componentTypes)
			if (!_bitArray.get(componentType.getId()))
				return false;
		return true;
	}
	
	/**
	 * Checks if this ComponentMask contains all the types defined by the specified mask.
	 * 
	 * @param mask The mask which should be checked.
	 * @return True if this mask contains all the types of the specified mask, otherwise false.
	 */
	public boolean contains(ComponentMask mask) {
		return _bitArray.contains(mask._bitArray);
	}
	
	/**
	 * Checks if this ComponentMask contains at least one of the types in the specified list.
	 * 
	 * @param mask The mask which should be checked.
	 * @return True if this mask contains a least one of the types in the specified list,
	 * otherwise false.
	 */
	public boolean intersects(ComponentType... componentTypes) {
		for (ComponentType componentType : componentTypes)
			if (_bitArray.get(componentType.getId()))
				return true;
		return false;
	}
	
	/**
	 * Checks if this ComponentMask contains at least one of the types defined by the specified
	 * mask.
	 * 
	 * @param mask The mask which should be checked.
	 * @return True if this mask contains a least one of the types of the specified mask,
	 * otherwise false.
	 */
	public boolean intersects(ComponentMask mask) {
		return _bitArray.intersects(mask._bitArray);
	}
	
	/**
	 * Gets a list of all ComponentTypes that are part of the mask.
	 * 
	 * @return The list of all ComponentTypes.
	 */
	public List<ComponentType> getComponentTypes() {
		ArrayList<ComponentType> result = new ArrayList<ComponentType>();
		int max = _bitArray.getCapacity();
		for (int i = 0; i < max; i++)
			if (_bitArray.get(i))
				result.add(ComponentType.get(i));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentMask mask = (ComponentMask)obj;
		return _bitArray.equals(mask._bitArray);
	}
}
