package infinity.ecs.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	// Dev-Note:
	// Can be made even faster by using a simple array instead of an hash map, in which the id of
	// a message type acts as the index, like this:
	// 	00|	List<IndexedMessageEndpoint2>[] _endpointRegister =
	// 	01|		(List<IndexedMessageEndpoint2>[]) new List<?>[n];
	// Performance tests indicate roughly 10% gain in performance, when the array is big enough to
	// prevent resizing. For the moment the hash map variant is used because of the additional
	// complexity of the array version regarding bound checking etc.
	
	
	/**
	 * All associated endpoints indexed by the id of the message types they have registered.
	 */
	private Map<Integer, List<IndexedMessageEndpoint>> _endpointRegister;
	
	/**
	 * Creates a new instance of the IndexedMessageDispatcher class.
	 */
	public IndexedMessageDispatcher() {
		_endpointRegister = new HashMap<Integer, List<IndexedMessageEndpoint>>();
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
			_endpointRegister.put(msgId, epList);
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
		for (List<IndexedMessageEndpoint> epList : _endpointRegister.values())
			epList.remove(endpoint);
	}

	public MessageEndpoint createEndpoint() {
		return new IndexedMessageEndpoint(this);
	}
}
