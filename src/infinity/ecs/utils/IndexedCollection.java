package infinity.ecs.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a collection of elements, which are accessible by their unique index. Best used for
 * cases where elements are get or set by their index most of the time. Should not be used if a
 * very good iteration performance is required or where iterating trough the collection is the
 * main case.
 * <p>
 * <b>Notes:</b><br>
 * Accessing an elements by its index is O(1) and the indices of elements are guaranteed to stay
 * the same even if other elements have been removed or added to the collection. New elements are
 * inserted by the set method with their specific unique index. Since the collection has no defined
 * end, there is no add method. For the purpose of inserting new elements into the collection, it
 * can be assumed to be of infinite length. If the current capacity is exceeded, the internal
 * array is automatically resized to match the required capacity.
 * <p>
 * Iterating trough the array is more costly than for conventional lists, because elements may be
 * spread through the whole array, so that the position of the next element must be searched
 * incrementally.
 * 
 * @author preip
 */
@SuppressWarnings("unchecked")
public class IndexedCollection<T> implements Iterable<T> {
	
	// Dev-Notes:
	// The java language specification states that arrays are initialized to default values during
	// creation, which is null for all reference types. This means that a field with the value null
	// is considered to be free and every field not null is occupied.
	
	/**
	 * The initial capacity of the internal array if the default constructor is used and the number
	 * of additional elements slots that is allocated if the current capacity is exceeded. 
	 */
	private final static int CAPACITY_OFFSET = 32;
	
	/**
	 * The internal array which stores the elements of the collection based on their index. Each
	 * occupied field contains a valid element reference and each free field has a value of null.
	 */
	private T[] _elements;
	
	/**
	 * Creates a new instance of the {@link IndexedCollection} class.
	 */
	public IndexedCollection() {
		this(CAPACITY_OFFSET);
	}
	
	/**
	 * Creates a new instance of the {@link IndexedCollection} class.
	 * 
	 * @param capacity The initial capacity of the collection.
	 */
	public IndexedCollection(int capacity) {
		_elements = (T[])new Object[capacity];
	}
	
	/**
	 * Tries to get the element with the specified index.
	 * 
	 * @param index The index of the element which should be got.
	 * @return The desired element or null if there was no element with the specified index.
	 */
	public T get (int index) {
		if (index >= _elements.length)
			return null;
		return _elements[index];
	}
	
	/**
	 * Inserts the specified element at the specified index. If an elements with the same index is
	 * already part of the collection it is overridden. 
	 * 
	 * @param index The index at which the element should be inserted.
	 * @param element The element which should be inserted.
	 */
	public void set(int index, T element) {
		if (index >= _elements.length)
			resize(index + CAPACITY_OFFSET);
		_elements[index] = element;
	}
	
	/**
	 * Removes the element with the specified index from the collection and frees the corresponding
	 * field.
	 * 
	 * @param index The index of the element which should be removed.
	 * @return true if the element was removed, otherwise false
	 */
	public boolean remove(int index) {
		if (index >= _elements.length || _elements[index] == null)
			return false;
		_elements[index] = null;
		return true;
	}
	
	public Iterator<T> iterator() {
		return new IndexedCollectionIterator(this);
	}
	
	/**
	 * Resizes the collection to the specified capacity. Can be used to shrink or enlarge the
	 * internal array. If the array is reduced in size, all overlapping elements are removed.
	 * 
	 * @param capacity The desired capacity.
	 */
	private void resize(int capacity) {
		T[] newElements = (T[])new Object[capacity];
		int length = Math.min(capacity, _elements.length);
		System.arraycopy(_elements, 0, newElements, 0, length);
		_elements = newElements;
	}
	
	/**
	 * Special iterator for the {@link IndexedCollection} which is adapted to the special structure
	 * of the collection.
	 * <p>
	 * <b>Notes.</b><br>
	 * Since the collection doesn't consist of a continuous block of elements, traditional
	 * iterators wont work. The {@link IndexedCollectionIterator} addresses this by ignoring
	 * empty fields within the collection and by seeking the next occupied field from the current
	 * position. A result of this is that it performs more slowly than traditional iterators.
	 * 
	 * @author preip
	 */
	private class IndexedCollectionIterator implements Iterator<T> {
		
		/**
		 * A reference to the collection which is iterated trough for easier (and faster) access.
		 */
		private final IndexedCollection<T> _collection;
		
		/**
		 * The current position of the iterator within the collection. This determines which
		 * element is returned by the {@link #next()} method and which element is affected by
		 * the {@link #remove()} method.
		 * <p>
		 * Values range from -1 to the capacity of the collection, whereby -1 indicates that the
		 * iterator has just been created and {@link #next()} has still to be called for the first
		 * time.
		 */
		private int _currentPos;
		
		/**
		 * Indicates the position of the next element within the collection. Values range from -1
		 * to the capacity of the collection, whereby -1 means that the position of the next
		 * element is currently unknown has has to be determined by calling {@link #seekNext()}.
		 */
		private int _nextPos;
		
		/**
		 * Creates a new instance of the {@link IndexedCollectionIterator} class.
		 * 
		 * @param collection The {@link IndexedCollection} that is iterated trough.
		 */
		public IndexedCollectionIterator(IndexedCollection<T> collection) {
			_collection = collection;
			_currentPos = -1;
			_nextPos = -1;
		}
		
		/**
		 * Indicated if there is another element in the collection or if the iterator has reached
		 * the end.
		 * 
		 * @return true if there is another element, otherwise false.
		 */
		public boolean hasNext() {
			// if the position of the next element is currently unknown, try to find it. If that
			// fails, the end of the collection has been reached.
			if (_nextPos == -1)
				return seekNext();
			// if the position of the next element is known, this obviously means that there must
			// be a next element
			return true;
		}
		
		/**
		 * Gets the next element in the collection.
		 * 
		 * @return The next element of the collection.
		 * @exception NoSuchElementException when there is no next element because the iterator has
		 * 		reached the end of the collection.
		 */
		public T next() throws NoSuchElementException {
			// if the position of the next element is currently unknown, try to find it. If that
			// fails, the iterator has reached the end of the collection.
			if (_nextPos == -1 && !seekNext())
				throw new NoSuchElementException();
			// otherwise the former next element is now the current one, which is also the desired
			// element and therefore returned. The position of the next element is no unknown
			// again.
			_currentPos = _nextPos;
			_nextPos = -1;
			return (T)_collection._elements[_currentPos];
		}
		
		/**
		 * Removes the element which was by returned by the last call to {@link #next()} from the
		 * collection.
		 * 
		 * @exception IllegalStateException when {@link #next()} wasn't called at least ones,
		 * because otherwise there is no current element.
		 */
		public void remove() throws IllegalStateException {
			if (_currentPos == -1)
				throw new IllegalStateException();
			_collection.remove(_currentPos);
		}
		
		/**
		 * Tries to find the position of the next element in the collection and sets
		 * {@link #_nextPos} to the resulting value.
		 * 
		 * @return true if the element was found, or false if there is no next element because the
		 * iterator has reached the end of the collection.
		 */
		private boolean seekNext() {
			int length = _collection._elements.length;
			for (int i = _currentPos + 1; i < length; i++)
				if (_collection._elements[i] != null) {
					_nextPos = i;
					return true;
				}
			return false;
		}
	}
}