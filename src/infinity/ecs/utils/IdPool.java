package infinity.ecs.utils;

/**
 * Class which handles the distribution of unique IDs.
 * <p>
 * <b>Notes:</b><br>
 * All IDs are integers and guaranteed to be unique, if the IdPools was used correctly.
 * Previously distributed IDs can be freed, if they are no longer needed. Those free IDs
 * can than be distributed again. This ensures that the maximum number of IDs which can
 * be distributed at the same time stays the same during the lifetime of the pool.
 * <p>
 * To ensure the best possible performance, any freed ID is neither checked if it was
 * distributed by this pool in the first place (because this would require bookkeeping
 * of all distributed IDs, which would require loots of memory), nor if the ID was
 * already freed before without being redistributed (as this would require to search the
 * entire stack of currently free IDs). It is therefore the responsibility of all objects
 * using this pool to make sure these two points are taken care of.
 * <p>
 * To make sure the maximum number of possible IDs stays the same, it's important to set
 * the size of the stack which holds the free IDs big enough. All IDs which are freed
 * when the stack is full are lost and can't be redistributed again. On the other hand,
 * making the stack to big will needlessly waste memory. The ideal size of the stack
 * would take the number of IDs into account, which will be freed before new IDs are
 * distributed again.
 * <p>
 * To disable the storage of IDs, which have been free, simply set the capacity of the
 * stack to zero. This will on one one hand guarantee that every IDs acquired to the
 * pool will be unique, regardless of the actions of other objects. On the other hand
 * this means that the IdPool can only distribute  2^32 IDs during it's lifetime,
 * after which it will simply overflow.
 * 
 * ToDo: Needs unit tests.
 * 
 * @author preip
 * @version 1.0
 */
public class IdPool {
	/**
	 * The amount of IDs handed out by this pool, this includes freed IDs.
	 * <p>
	 * Used to determine the next unique ID if there are no free IDs, which
	 * can be distributed first. 
	 */
	private int _idCount; //TODO: Something to capture the integer overflow.
	/**
	 * Array that is used as a stack and contains all IDs that was have been
	 * freed and can therefore be reused.
	 */
	private int[] _freeIds;
	/**
	 * Points to the current position an the stack of free IDs. A value of -1
	 * means there are currently no free IDs.
	 */
	private int _freeIdsPointer;
	
	/**
	 * Creates a new instance of the IdPool class. The capacity of the stack for free IDs
	 * will be set to 255.
	 */
	public IdPool() {
		this(255);
	}
	/**
	 * Creates a new instance of the IdPool class.
	 * @param freeIdCapacity The capacity of the stack for free IDs.
	 */
	public IdPool(int freeIdCapacity) {
		_idCount = 0;
		_freeIds = new int[freeIdCapacity];
		_freeIdsPointer = -1;
	}
	/**
	 * Get an unique ID from this pool.
	 * @return The resulting ID.
	 */
	public int getId() {
		if (_freeIdsPointer != -1) {
			return _freeIds[_freeIdsPointer--];
		}
		return _idCount++;
	}
	/**
	 * Free a previously distributed ID which is no longer needed.
	 * @param id The ID which should be free.
	 */
	public void freeId(int id) {
		if (_freeIdsPointer != _freeIds.length - 1)
			_freeIds[++_freeIdsPointer] = id; 
	}
	/**
	 * Gets the current
	 * @return
	 */
	public int getFreeIdCapacity() {
		return _freeIds.length;
	}
	/**
	 * Sets the capacity of the stack which is used to store the currently free IDs.
	 * WARNING: This is a slow operation, because it requires copying the current stack,
	 * if it's not empty. Therefore this should be used sparingly.
	 * @param capacity The desired new capacity. Values below zero are set to zero.
	 */
	public void setFreeIdCapacity(int capacity) {
		// The minimum length is of course zero, so make sure everything smaller is set to zero
		if (capacity < 0)
			capacity = 0;
		// if the new capacity is the same as the current one, there is no need to do anything
		if (capacity == _freeIds.length)
			return;
		int[] newFreeIds = new int[capacity];
		// if the stack is currently not empty, its contents must be copied to the new array
		if	(_freeIdsPointer != -1) {
			// get whichever is smaller, the new capacity or the current position of the stack pointer,
			// because it's not possible to copy more than the new stack would hold, but there is also
			// no point in copying more data than is used anyway
			int length = Math.min(capacity, _freeIdsPointer);
			System.arraycopy(_freeIds, 0, newFreeIds, 0, length);
		}
		_freeIds = newFreeIds;
	}
}
