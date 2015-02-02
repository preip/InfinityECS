package infinity.ecs.messaging;

import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;

/**
 * Class which represents an entity modification endpoint which can be used to send modification
 * messages to the associated dispatcher and receive relevant messages that have been send from 
 * other endpoints.
 * <p>
 * <b>Notes:</b><br>
 * Every endpoint needs to be associated with a specific dispatcher to receive and send messages.
 * Therefore new endpoint instances can only be created trough an existing disptcher instance.
 * <p>
 * To receive any messages, an endpoint needs to register a desired set of component types at the
 * dispatcher for which it want's to receive messages. If an endpoint no longer want's any messages
 * regarding a specific component type, it can also deregister that type at the dispatcher.
 * 
 * @author preip
 * @version 0.1
 */
public final class EntityModificationEndpoint {
	
	/**
	 * The dispatcher that this endpoit is associated with and to which new messages are send.
	 */
	final EntityModificationDispatcher _dispatcher;
	
	// TODO: Think about best way to save modification messages
	//private Queue<EntityModificationMessage> _msgQueue;
	
	EntityModificationEndpoint(EntityModificationDispatcher dispatcher) {
		// since new endpoints are only created by the dispatcher, there is no need to check if
		// the reference might be null
		_dispatcher = dispatcher;
		
		//_msgQueue = new ArrayDeque<EntityModificationMessage>();
	}
	
	/**
	 * Gets the dispatcher this endpoint is associated with.
	 * 
	 * @return The dispatcher.
	 */
	public EntityModificationDispatcher getDispatcher() {
		return _dispatcher;
	}
	
	/**
	 * Sends a new message to the dispatcher an all other relevant endpoints.
	 * 
	 * @param entity The entity which has been manipulated.
	 * @param componentType The type of the component which has been manipulated.
	 */
	public void send(Entity entity, ComponentType componentType) {
		_dispatcher.dispatch(entity, componentType);
	}
	
	/**
	 * Notifies the endpoint of an occurred modification.
	 * <b>Note:</> This method is package-private and should only be called by the dispatcher.
	 * 
	 * @param entity The entity which has been manipulated.
	 * @param componentType The type of the component which has been manipulated.
	 */
	void receive(Entity entity, ComponentType componentType) {
		// TODO: Check if there is already a message in the queue which concerns the same entity
		// and component
		
		//_msgQueue.offer(message);
	}
}
