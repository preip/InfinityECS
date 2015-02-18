package infinity.ecs.messaging;

// TODO: Make this an interface (getType isn't working anyway, because it will always return the
// abstract class and must therefore be overwritten by all derived classes anyway.
public abstract class Message {
	
	public MessageType getType() {
		return MessageType.get(this.getClass());
	}
}
