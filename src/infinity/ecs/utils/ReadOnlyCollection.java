package infinity.ecs.utils;

import java.util.Iterator;
import java.util.List;

public class ReadOnlyCollection<T> implements Iterable<T> {
	/**
	 * The list that represents the source of this ReadOnlyCollection.
	 */
	private final List<T> _source;
	
	/**
	 * Creates a new instance of the ReadOnlyCollection class.
	 * @param source The list that represents the source of this ReadOnlyCollection.
	 * @throws NullPointerException When the source list is null.
	 */
	public ReadOnlyCollection(List<T> source) throws NullPointerException {
		if (source == null)
			throw new NullPointerException("The source of this ReadOnlyCollection can't be null.");
		_source = source;
	}
	
	public int size() {
		return _source.size();
	}
	
	public boolean contains(T value) {
		return _source.contains(value);
	}
	
	public T get(int index) {
		return _source.get(index);
	}
	
	/**
	 * Indicates if the specified list is the source of this ReadOnlyCollection.
	 * @param source The list which should be tested.
	 * @return true if the specified list is the source, otherwise false.
	 */
	public boolean isSource(List<T> source) {
		return source.equals(_source);
	}

	@Override
	public Iterator<T> iterator() {
		return new ReadOnlyIterator<T>(_source.iterator());
	}
}
