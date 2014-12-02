package infinity.ecs.utils;

import java.util.Iterator;

/**
 * Special Iterator, which prevents calls to remove and is therefore read only.
 * 
 * @author preip
 * @version 1.0
 * 
 * @param <T> The type of the iteration. 
 */
public class ReadOnlyIterator<T> implements Iterator<T> {
	/**
	 * The iterator which acts as the source of this ReadOnlyIterator. 
	 */
	private final Iterator<T> _source;
	/**
	 * Creates a new instance of the ReadOnlyIterator class.
	 * @param source The iterator which acts as the source of this ReadOnlyIterator.
	 * @throws NullPointerException If the source iterator is null.  
	 */
	public ReadOnlyIterator(Iterator<T> source) throws NullPointerException {
		if (source == null)
			throw new NullPointerException("The source iterator can't be null");
		_source = source;
	}
	/**
	 * Indicates if there are other elements within the interation, or if the last elements has
	 * been reached.
	 * @return true if there is a next element in the iteration, otherwise false.
	 */
	@Override
	public boolean hasNext() {
		return _source.hasNext();
	}
	/**
	 * Gets the next element in the iteration.
	 * @return The next element in the iteration.
	 */
	@Override
	public T next() {
		return _source.next();
	}
	/**
	 * Removing elements is not allowed for ReadOnlyIterators.
	 * @throws UnsupportedOperationException If called.
	 */
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Remove is not allowed for ReadOnlyIterators");
	}
}
