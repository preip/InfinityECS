package infinity.ecs.messaging;

import java.util.ArrayDeque;
import java.util.Queue;

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
 * dispatcher for which it wants to receive messages. If an endpoint no longer wants any messages
 * regarding a specific component type, it can also deregister that type at the dispatcher.
 * 
 * @author preip
 */
public final class EntityModificationEndpoint {
	
	// Dev-Notes:
	// The fastest and most memory friendly way to implement the message queue would be as a set of
	// value types (aka structs) which sadly don't exist in Java. With that option from the table,
	// we need an EntryModificationEntry class and there are two feasible options of implementing
	// the queue with it: Using a HashMap and using a Queue.
	// The HashMap would take the ID of the modified Entity as key and the EntryModificationEntry
	// as value. If new messages are received its trivial (O(1)) to check if there already is an
	// entry of that particular Entity. On the Other hand if the messages are handled by the owner
	// of this endpoint a list (or better a queue) of all entries must be composed, which costs
	// O(n) time.
	// If on the other hand the entries are stored directly in a queue, the time to check if there
	// already is an entry for a particular Entity becomes O(n) and the is no need to create
	// anything if the queue need to be processed.
	// The best solution should be determined through profiling, for the time being the second
	// option is used because it is less complex.
	
	//----------------------------------------------------------------------------------------------
	// Fields
	//----------------------------------------------------------------------------------------------
	
	/**
	 * The dispatcher that this endpoit is associated with and to which new messages are send.
	 */
	final EntityModificationDispatcher _dispatcher;
	
	/**
	 * The queue which holds all messages the endpoint has received.
	 */
	private Queue<EntityModificationEntry> _queue;
	
	//----------------------------------------------------------------------------------------------
	// Constructors
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Creates a new instance of the EntityModificationEndpoint class.
	 * New instances should only be creates by the EntityModificationDispatcher.
	 * 
	 * @param dispatcher The EntityModificationDispatcher that this endpoint is associated with.
	 */
	EntityModificationEndpoint(EntityModificationDispatcher dispatcher) {
		// since new endpoints are only created by the dispatcher, there is no need to check if
		// the reference might be null
		_dispatcher = dispatcher;
		_queue = new ArrayDeque<EntityModificationEntry>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Methods
	//----------------------------------------------------------------------------------------------
	
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
		_dispatcher.dispatch(this, entity, componentType);
	}
	
	/**
	 * Notifies the endpoint of an occurred modification.
	 * <b>Note:</> This method is package-private and should only be called by the dispatcher.
	 * 
	 * @param entity The entity which has been manipulated.
	 * @param componentType The type of the component which has been manipulated.
	 */
	void receive(Entity entity, ComponentType componentType) {
		// Because this is called by the dispatcher, there is no need to check for invalid
		// parameters, because the dispatcher already took care of that.
		
		// To prevent to queue from getting a lot of entries with changes to the same entity,
		// or event the same type of component, it is necessary to modify the existing entries
		// with the new information.
		// Therefore the method first checks for existing entries and if present, adds the new
		// component type to that entry, if its not already in the list of modified component
		// types. A new entry is only created if the entity was not found within the queue.

		for (EntityModificationEntry entry : _queue)
			if (entry.getEntity().getId() == entity.getId()) {
				if (!entry.getComponentTypes().contains(componentType))
					entry.getComponentTypes().add(componentType);
				return;
			}
		_queue.offer(new EntityModificationEntry(entity, componentType));
	}
	
	/**
	 * Retrieves the next EntityModificationEntry from the queue. Retrieved entries are
	 * automatically removed. The method will return null if the queue is empty.
	 *
	 * @return The next entry or null.
	 */
	public EntityModificationEntry retrieveNext() {
		return _queue.poll();
	}
}