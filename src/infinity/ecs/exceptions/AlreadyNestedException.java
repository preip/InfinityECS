package infinity.ecs.exceptions;

/**
 *
 * @author Simon
 */
public class AlreadyNestedException extends InfinityException{
	
	/**
	 * Version UID for serialization.
	 */
	private static final long serialVersionUID = 0;
	
	/** 
	 * Constructs a new exception with null as its detail message 
	 */
	public AlreadyNestedException(){
		super();
	}
	
	/** 
	 * Constructs a new exception with the specific message
	 * @param message the detail message
	 */
	public AlreadyNestedException(String message){
		super(message);
	}
	
	/**
	 * Constructs a new exception with the specified cause and a detail message 
	 * of (cause==null ? null : cause.toString()) (which typically contains the 
	 * class and detail message of cause).
	 * @param cause the cause
	 */
	public AlreadyNestedException(Throwable cause){
		super(cause);
	}
	
	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * @param message the detail message
	 * @param cause the cause
	 */
	public AlreadyNestedException(String message, Throwable cause){
		super(message, cause);
	}	    
}
