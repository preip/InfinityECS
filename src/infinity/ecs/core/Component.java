package infinity.ecs.core;

public abstract class Component {
	/**
	 * Indicates if this component is unique or not. Only a single unique component can be part
	 * of an entity. An entity can contain a arbitrary number of non-unique components.
	 * 
	 * @return true if the component is unique, otherwise false.
	 */
	public abstract boolean isUnique();
	/**
	 * Gets the ComponentType of this Component.
	 * @return The resulting ComponentType.
	 */
	public ComponentType getComponentType() {
		// Kind of redundant with the get methods from ComponentType, but it might be handy to
		// be able to access the type directly through a component instance.
		return ComponentType.get(this.getClass());
	}
}
