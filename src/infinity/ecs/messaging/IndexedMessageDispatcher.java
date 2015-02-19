package infinity.ecs.messaging;

import infinity.ecs.utils.IndexedCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link MessageDispatcher} which uses an internal map to index the {@link MesssageType}s of
 * every associated endpoint.
 * <p>
 * <b>Notes:</b><br>
 * The indexing results in a speed advantage in most scenarios, because messages can be send to
 * endpoints directly without the need to check if they are interested in a message of the specific
 * type. This advantage is bought by using additional memory in comparison to an implementation
 * using a simple list for the endpoints. 
 * 
 * @author preip
 */
public final class IndexedMessageDispatcher implements MessageDispatcher {
	
	/**
	 * All associated endpoints indexed by the id of the message types they have registered.
	 */
	private IndexedCollection<List<IndexedMessageEndpoint>> _endpointRegister;
	
	/**
	 * Creates a new instance of the IndexedMessageDispatcher class.
	 */
	public IndexedMessageDispatcher() {
		_endpointRegister = new IndexedCollection<List<IndexedMessageEndpoint>>(32);
	}
	
	/**
	 * Dispatches a {@link Message} which has been send by the specified endpoint to all other
	 * relevant endpoints.
	 * 
	 * @param sender The sender of the {@link Message}.
	 * @param msg The {@link Message} which should be dispatched.
	 */
	void dispatch(IndexedMessageEndpoint sender, Message msg) {
		int msgId = msg.getType().getId(); 
		List<IndexedMessageEndpoint> epList = _endpointRegister.get(msgId);
		// if there are no endpoints registered for the type of the message, there is nothing to
		// dispatch
		if (epList == null)
			return;
		// otherwise send the message to every registered endpoint, except the original sender
		for (IndexedMessageEndpoint ep : epList)
			if (ep != sender)
				ep.receive(msg);
	}
	
	/**
	 * Registers the specified {@link MessageType} witch the specified
	 * {@link IndexedMessageEndpoint}.  
	 * 
	 * @param endpoint The {@link IndexedMessageEndpoint} for which the type should be registered.
	 * @param msgType The {@link MessageType} which should be registered.
	 */
	void register(IndexedMessageEndpoint endpoint, MessageType msgType) {
		int msgId = msgType.getId();
		List<IndexedMessageEndpoint> epList = _endpointRegister.get(msgId);
		// if there is no entry for the type of the message, a new one needs to be created
		if (epList == null) {
			epList = new ArrayList<IndexedMessageEndpoint>();
			epList.add(endpoint);
			_endpointRegister.set(msgId, epList);
			return;
		}
		// otherwise the endpoint can be simply added to the list if it is not already part of it
		if (!epList.contains(endpoint))
			epList.add(endpoint);
	}
	
	/**
	 * Deregisters the specified {@link IndexedMessageEndpoint} with the specified
	 * {@link MessageEndpoint}.
	 * 
	 * @param endpoint The {@link IndexedMessageEndpoint} for which the type should be deregistered.
	 * @param msgType The {@link MessageType} which should be deregistered.
	 */
	void deregister(IndexedMessageEndpoint endpoint, MessageType msgType) {
		int msgId = msgType.getId();
		List<IndexedMessageEndpoint> epList = _endpointRegister.get(msgId);
		if (epList != null)
			epList.remove(endpoint);
	}
	
	/**
	 * Checks for the specified {@link IndexedMessageEndpoint} of the specified {@link MessageType}
	 * has been registered or not.
	 * 
	 * @param endpoint The {@link IndexedMessageEndpoint} for which the type should be checked.
	 * @param msgType The {@link MessageType} which should be checked.
	 */
	boolean check(IndexedMessageEndpoint endpoint, MessageType msgType) {
		List<IndexedMessageEndpoint> epList = _endpointRegister.get(msgType.getId());
		if (epList != null && epList.contains(endpoint))
			return true;
		return false;
	}
	
	/**
	 * Removes the specified endpoint from this dispatcher. Only called by the
	 * {@link IndexedMessageEndpoint} itself.
	 *
	 * @param The endpoint which should be removed.
	 */
	void removeEndpoint(IndexedMessageEndpoint endpoint) {
		for (List<IndexedMessageEndpoint> epList : _endpointRegister)
			epList.remove(endpoint);
	}

	public MessageEndpoint createEndpoint() {
		return new IndexedMessageEndpoint(this);
	}
}
