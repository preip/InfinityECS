package infinity.ecs.messaging;

/**
 * Can be used to dispatch {@link Message}s between a set of {@link MessageEndpoint}s.
 * 
 * @author preip
 */
public interface MessageDispatcher {
	
	/**
	 * Creates a new endpoint which can be used to send and receive messaged. The endpoint is
	 * bound to this specific dispatcher.
	 * 
	 * @return The created endpoint.
	 */
	public MessageEndpoint createEndpoint();
}
