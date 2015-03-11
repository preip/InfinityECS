package infinity.ecs.core;

/**
 * Interface which marks all implementing classes as being able to construct a specific type of
 * {@link Component}.
 * 
 * @author preip
 */
public interface ComponentFactory {
	/**
	 * Gets the type of the {@link Component} this factory is able to construct.
	 * 
	 * @return The {@link ComponentType} of the component this factory constructs.
	 */
	public ComponentType getComponentType();
	
	/**
	 * Creates a new instance of the {@link Component} this {@link ComponentFactory} represents.
	 * @return
	 */
	public Component createNewComponent();
}
