package infinity.ecs.utils;

import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 * @author preip
 */
public class IndexedCollectionTest {
	@Test
	public void testGetAndSet() {
		IndexedCollection<Integer> col = new IndexedCollection<Integer>(50);
		for (int i = 0; i < 45; i++)
			col.set(i, i);
		for (int i = 0; i < 45; i++)
			assertTrue(col.get(i) == i);
		for (int i = 45; i < 55; i++)
			assertTrue(col.get(i) == null);
	}
	
	@Test
	public void testResize() {
		IndexedCollection<Integer> col = new IndexedCollection<Integer>(5);
		col.set(2, new Integer(5));
		col.set(10, new Integer(7));
		assertTrue("Collection element should be equal to 7", col.get(10) == 7);
	}
	
	@Test
	public void testRemove() {
		IndexedCollection<Integer> col = new IndexedCollection<Integer>(32);
		for (int i = 0; i < 32; i++)
			col.set(i, i);
		for (int i = 0; i < 32; i+=2)
			col.remove(i);
		for (int i = 0; i < 32; i++) {
			if (i % 2 == 0)
				assertTrue(col.get(i) == null);
			else
				assertTrue(col.get(i) == i);
		}
	}
	
	@Test
	public void testIterator() {
		IndexedCollection<Integer> col = new IndexedCollection<Integer>(32);
		col.set(2, 2);
		col.set(3, 3);
		col.set(8, 8);
		col.set(30, 30);

		Iterator<Integer> it = col.iterator();
		assertTrue(it.hasNext() == true);
		assertTrue(it.next() == 2);
		assertTrue(it.hasNext() == true);
		assertTrue(it.next() == 3);
		assertTrue(it.next() == 8);
		assertTrue(it.next() == 30);
		assertTrue(it.hasNext() == false);
	}
}
