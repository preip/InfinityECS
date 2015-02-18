package infinity.ecs.messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link MessageDispatcher} implementation.
 * <p>
 * <b>Notes:</b><br>
 * The {@link MessageType}s which the associated endpoints are interested in are stored within each
 * endpoint individually. They are accessed by the dispatcher during the dispatching of messages to
 * decide which endpoints receive which messages.
 *
 * @author preip
 */
public class SimpleMessageDispatcher implements MessageDispatcher {
	
	/**
	 * The list of all associated endpoints.
	 */
	public List<SimpleMessageEndpoint> _endpoints;
	
	/**
	 * Creates a new instance of the {@link SimpleMessageDispatcher} class.
	 */
	public SimpleMessageDispatcher() {
		_endpoints = new ArrayList<SimpleMessageEndpoint>();
	}
	
	/**
	 * Dispatches a {@link Message} which has been send by the specified endpoint to all other
	 * relevant endpoints.
	 * 
	 * @param sender The sender of the {@link Message}.
	 * @param msg The {@link Message} which should be dispatched.
	 */
	void dispatch(SimpleMessageEndpoint sender, Message msg) {
		MessageType msgType = msg.getType();
		int msgId = msgType.getId();
		for (SimpleMessageEndpoint ep : _endpoints)
			if (ep != sender && ep._msgBits.get(msgId))
				ep.receive(msg);
	}
	
	public MessageEndpoint createEndpoint() {
		SimpleMessageEndpoint ep = new SimpleMessageEndpoint(this);
		_endpoints.add(ep);
		return ep;
	}
	
	/**
	 * Removes the specified endpoint from this dispatcher. Only called by the
	 * {@link SimpleMessageEndpoint} itself.
	 *
	 * @endpoint The endpoint which should be removed.
	 */
	void remove(SimpleMessageEndpoint endpoint) {
		_endpoints.remove(endpoint);
	}
}
