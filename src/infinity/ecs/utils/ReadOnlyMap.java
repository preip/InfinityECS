package infinity.ecs.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A read-only map of key value pairs.
 * @author Simon
 * @param <K> Key
 * @param <V> Value 
 */
public class ReadOnlyMap<K,V> {   
    	/**
	 * The Map that represents the source of this ReadOnlyMap.
	 */
	private final Map<K,V> _source;
	
	/**
	 * Creates a new instance of a ReadOnlyMap<K,V> 
	 * @param source the source map
	 * @throws NullPointerException is thrown when the source is null.
	 */
	public ReadOnlyMap(Map<K,V> source) throws NullPointerException {
	    if(source == null)
		throw new NullPointerException("The source of this ReadOnlyMap can`t be null");
	    this._source = source;
	}
	
	/**
	 * 
	 * @param key
	 * @return True if the key is in the map.
	 */
	public boolean containsKey(K key){
	    return _source.containsKey(key);
	}
	
	/**
	 * 
	 * @param value
	 * @return True if the value is in the map. 
	 */
	public boolean containsValue(V value){
	    return _source.containsValue(value);
	}
	
	/**
	 * Returns the value that is specified by the key.
	 * @param key
	 * @return 
	 */
	public V get(K key){
	    return _source.get(key);
	}
	
	/**
	 * 
	 * @return 
	 */
	public boolean isEmpty(){
	    return _source.isEmpty();
	}
	
	/**
	 * A Set containing all keys that are in the map.
	 * @return 
	 */
	public Set<K> keySet(){
	    return _source.keySet();
	}
	
	/**
	 * 
	 * @return 
	 */
	public int size(){
	    return _source.size();
	}
	
	/**
	 * A Collection with all values that are in the map.
	 * @return 
	 */
	public Collection<V> values(){
	    return _source.values();
	}
    
}
