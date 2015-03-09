package infinity.ecs.messaging;

import infinity.ecs.utils.BitArray;

import java.util.ArrayDeque;
import java.util.Queue;

// TODO: Comment this
public class SimpleMessageEndpoint implements MessageEndpoint {
	
	/**
	 * The {@link IndexedMessageDispatcher} this endpoint is associated with.
	 */
	final SimpleMessageDispatcher _dispatcher;
	
	/**
	 * The queue which is used to store all received messages.
	 */
	final Queue<Message> _queue;
	
	/**
	 * Determines which {@link MessageType}s have been registered or not, by setting the id of the
	 * corresponding type within the bit array.
	 */
	final BitArray _msgBits;
	
	/**
	 * Indicates if the endpoint has been terminated or not.
	 */
	private boolean _isTerminated;
	
	/**
	 * Creates a new instance of the {@link SimpleMessageEndpoint} instance. Only called by the
	 * {@link SimpleMessageDispatcher}.
	 * 
	 * @param dispatcher The {@link IndexedMessageDispatcher} this endpoint is associated with.
	 */
	SimpleMessageEndpoint(SimpleMessageDispatcher dispatcher) {
		_dispatcher = dispatcher;
		_queue = new ArrayDeque<Message>();
		_msgBits = new BitArray();
		_isTerminated = false;
	}
	
	/**
	 * Receives a new message which has been dispatched by the {@link SimpleMessageDispatcher}
	 * this endpoint is associated with. The received {@link Message} is stored in the internal
	 * queue.
	 * 
	 * @param msg The received {@link Message}.
	 */
	void receive(Message msg) {
		_queue.offer(msg);
	}
	
	/**
	 * Sends the specified message to the dispatcher an all other relevant endpoints.
	 * 
	 * @param msg The message which should be send.
	 * @throws IllegalStateException When a message is send after the endpoint has been terminated.
	 */
	public void send(Message msg) throws IllegalStateException {
		if (_isTerminated)
			throw new IllegalStateException("endpoint has been terminated");
		_dispatcher.dispatch(this, msg);
	}
	
	/**
	 * Registers the specified message type with this endpoint. The endpoint will now receive all
	 * messages of the specified type until the type is derigstered again.
	 * 
	 * @param type The {@link MessageType} which should be registered.
	 * @throws IllegalStateException when the endpoint has been terminated.
	 */
	public void register(MessageType type) throws IllegalStateException {
		if (_isTerminated)
			throw new IllegalStateException("endpoint has been terminated");
		_msgBits.set(type.getId(), true);
	}
	
	/**
	 * Deregisters the specified message type with this endpoint. The endpoint will no longer
	 * receive any messages of the specified type.
	 * 
	 * @param type The {@link MessageType} which should be deregistered.
	 * @throws IllegalStateException when the endpoint has been terminated.
	 */
	public void deregister(MessageType type) throws IllegalStateException {
		if (_isTerminated)
			throw new IllegalStateException("endpoint has been terminated");
		_msgBits.set(type.getId(), false);
	}
	
	public boolean check(MessageType msgType) {
		if (_isTerminated)
			return false;
		return _msgBits.get(msgType.getId());
	}
	
	public MessageDispatcher getDispatcher() {
		return _dispatcher;
	}

	public Message retrieveNext() {
		return _queue.poll();
	}
	
	public void terminate() {
		_isTerminated = true;
		_dispatcher.remove(this);
		_queue.clear();
	}
}
