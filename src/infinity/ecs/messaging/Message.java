package infinity.ecs.messaging;

/**
 * Abstract class which identifies all derived classes as {@link Message}s, which can be send
 * between {@link MessageEndpoint}s with the use of a central {@link MessageDispatcher}.
 * 
 * @author preip
 */
public abstract class Message {
	
	/**
	 * Gets the {@link MessageType} of this {@link Message}.
	 * 
	 * @return The resulting {@link MessageType}.
	 */
	public MessageType getType() {
		return MessageType.get(this.getClass());
	}
}
