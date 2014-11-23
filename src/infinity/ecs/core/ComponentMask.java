package infinity.ecs.core;

import infinity.ecs.utils.BitArray;

/**
 * Can be used to create a mask that resembles a group of several components.
 * Can be compared to other masks to find matches etc.
 * <p>
 * <b>Notes:</b><br>
 * Tight wrapper around {@link BitArray} class. Should be used by Entities and Systems to
 * check if the requirements from to Systems to the Entities are matched.
 * <p>
 * Manipulation of the masks after their initial creation is only allowed by core
 * members. This enables entities to quickly alter their masks if the gain or
 * loose components without the need to create a completely new mask.
 * Manipulation is not exposed outside of the core so prevent other objects changing
 * the mask of an entity in a way, that results in an invalid state, because the
 * mask no longer resembles the components that entity contains.
 * 
 * @author preip
 * @version 1.0
 */
public class ComponentMask {
	private BitArray _bitArray;
	
	public ComponentMask() {
		_bitArray = new BitArray();
	}
	
	public ComponentMask(ComponentType... componentTypes) {
		_bitArray = new BitArray();
		set(componentTypes);
	}
	
	void set(ComponentType... componentTypes) {
		for (ComponentType componentType : componentTypes)
			_bitArray.set(componentType.getId(), true);
	}
	
	void clear(ComponentType... componentTypes) {
		for (ComponentType componentType : componentTypes)
			_bitArray.set(componentType.getId(), false);
	}
	/**
	 * Checks, if the component mask contains all specified ComponentTypes.
	 * @param componentTypes The ComponentTypes which should be checked.
	 * @return true if the mask contains all specified types, otherwise false.
	 */
	public boolean contains(ComponentType... componentTypes) {
		for (ComponentType componentType : componentTypes)
			if (!_bitArray.get(componentType.getId()))
				return false;
		return true;
	}
	
	public boolean contains(ComponentMask mask) {
		return _bitArray.contains(mask._bitArray);
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
