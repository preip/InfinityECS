package infinity.ecs.core;

public abstract class Component {
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
