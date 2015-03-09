package infinity.ecs.messaging;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A {@link MessageEndpoint} which is created and used by the {@link IndexedMessageDispatcher}.
 * <p>
 * <b>Notes:</b><br>
 * This class is basically a skeleton implementation of the {@link MessageEndpoint} interface.
 * Besides to functionality to store and retrieve the received {@link Message}, all other calls are
 * directly forwarded to the dispatcher.
 * 
 * @author preip
 */
public class IndexedMessageEndpoint implements MessageEndpoint {
	
	/**
	 * The {@link IndexedMessageDispatcher} this endpoint is associated with.
	 */
	private final IndexedMessageDispatcher _dispatcher;
	
	/**
	 * The queue which is used to store all received messages.
	 */
	private final Queue<Message> _queue;
	
	/**
	 * Indicates if the endpoint has been terminated or not.
	 */
	private boolean _isTerminated;
	
	/**
	 * Creates a new instance of the {@link IndexedMessageEndpoint} instance. Only called by the
	 * {@link IndexedMessageDispatcher}.
	 * 
	 * @param dispatcher The {@link IndexedMessageDispatcher} this endpoint is associated with.
	 */
	IndexedMessageEndpoint(IndexedMessageDispatcher dispatcher) {
		_dispatcher = dispatcher;
		_queue = new ArrayDeque<Message>();
		_isTerminated = false;
	}
	
	/**
	 * Receives a new message which has been dispatched by the {@link IndexedMessageDispatcher}
	 * this endpoint is associated with. The received {@link Message} is stored in the internal
	 * queue.
	 * 
	 * @param msg The received {@link Message}.
	 */
	void receive (Message msg) {
		_queue.offer(msg);
	}
	
	/**
	 * Sends the specified message to the dispatcher an all other relevant endpoints.
	 * 
	 * @param msg The message which should be send.
	 * @throws IllegalStateException when the endpoint has been terminated.
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
	 * @param type IllegalStateException when the endpoint has been terminated.
	 */
	public void register(MessageType type) throws IllegalStateException {
		if (_isTerminated)
			throw new IllegalStateException("endpoint has been terminated");
		_dispatcher.register(this, type);
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
		_dispatcher.deregister(this, type);
	}
	
	public boolean check(MessageType msgType) {
		return _dispatcher.check(this, msgType);
	}
	
	public Message retrieveNext() {
		return _queue.poll();
	}
	
	public void terminate() {
		_isTerminated = true;
		_dispatcher.removeEndpoint(this);
		_queue.clear();
	}
	
	public MessageDispatcher getDispatcher() {
		return _dispatcher;
	}
}
