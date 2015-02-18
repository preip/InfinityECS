package infinity.ecs.messaging;

/**
 * Interface which acts as an endpoint for a specific {@link MessageDispatcher} and is used to receive
 * and send messages.
 * 
 * @author preip
 */
public interface MessageEndpoint {

	/**
	 * Gets the dispatcher this endpoint is associated with.
	 * 
	 * @return The dispatcher.
	 */
	public MessageDispatcher getDispatcher();
	
	/**
	 * Sends the specified message to the dispatcher an all other relevant endpoints.
	 * 
	 * @param msg The message that should be send.
	 */
	public void send(Message msg);
	
	/**
	 * Registers the specified message type with this endpoint. The endpoint will now receive all
	 * messages of the specified type until the type is derigstered again.
	 * 
	 * @param msgType The {@link MessageType} which should be registered.
	 */
	public void register(MessageType msgType);
	
	/**
	 * Deregisters the specified message type with this endpoint. The endpoint will no longer
	 * receive any messages of the specified type.
	 * 
	 * @param msgType The {@link MessageType} which should be deregistered.
	 */
	public void deregister(MessageType msgType);
	
	/**
	 * Checks for the specified {@link MessageType} if it hat been registered for the endpoint or
	 * not.
	 *
	 * @param msgType The {@link MessageType} of the message which should be checked.
	 * @return true if the type has been registered, otherwise false
	 */
	public boolean check(MessageType msgType);
	
	/**
	 * Retrieves the next message from the queue. Retrieved messages are automatically removed.
	 * The method will return null if the queue is empty.
	 *
	 * @return The next message or null.
	 */
	public Message retrieveNext();
	
	/**
	 * Terminates the connection of this endpoint to the dispatcher. The endpoint can no longer be
	 * used to send or receive {@link Message}s or to register or deregister {@link MessageType}s.
	 * <br>
	 * <b>Warning:</b> All unread {@link Message}s in the queue of the endpoint are lost.
	 */
	public void terminate();
}
