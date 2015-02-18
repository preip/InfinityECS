package infinity.ecs.core;

/**
 * Interface which identifies all implementing classes as {@link Component}s which can be used as
 * a specific data storage in combination with {@link Entity}s.
 * 
 * @author preip
 */
public interface Component {
	/**
	 * Gets the {@link ComponentType} of this {@link Component}.
	 * <p>
	 * <b>Notes:</b><br>
	 * Every derived class must make sure to implement this method correctly, so that the returned
	 * {@link ComponentType} is guaranteed to represent that specific class. This also means that
	 * when a {@link Component}-implementation derives from a another {@link Component}-
	 * implementation, it <i>must always override this method</i>.
	 * 
	 * @return The resulting {@link ComponentType}.
	 */
	public ComponentType getComponentType();
}
