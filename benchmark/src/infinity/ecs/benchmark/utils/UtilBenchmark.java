package infinity.ecs.benchmark.utils;

import infinity.ecs.benchmark.BenchmarkItem;
import infinity.ecs.utils.IndexedCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtilBenchmark implements BenchmarkItem {
	
	public String getName() {
		return "Utility Benchmarks";
	}
	
	public void printMenu() {
		System.out.println("\n-------------------------------------");
		System.out.println("- InfinityECS - Utility Benchmarks: -");
		System.out.println("-------------------------------------");
		System.out.println(" [01] Indexed Collection Performance");
	}
	
	public void handleInput(String input) {
		switch (input) {
		case  "1":
		case "01":
			this.testIndexedCollectionPerformance();
			break;
		default:
			System.out.println("unrecognized command");
			break;
		}
	}
	
	public static final int ITERATION_COUNT = 10000;
	
	public void testIndexedCollectionPerformance() {
		System.out.println("\nTexting the performance of 'IndexedCollection' in comparison to"
				+ "'java.util.ArrayList' with " + ITERATION_COUNT + " elements:");
		List<Integer> list = new ArrayList<Integer>(32); 
		IndexedCollection<Integer> iCol = new IndexedCollection<Integer>(32);
		
		System.out.println("> Adding " + ITERATION_COUNT + " elements to the lists:");
		// ArrayList add performance
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < ITERATION_COUNT; i++)
				list.add(i);
			long endTime = System.nanoTime();
			System.out.println("> \tArrayListt            = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		// IndexedCollection add performance
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < ITERATION_COUNT; i++)
				iCol.set(i, i);
			long endTime = System.nanoTime();
			System.out.println("> \tIndexedCollection     = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		
		System.out.println("> Getting elements (by their id):");
		// ArrayList indexOf performance
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < ITERATION_COUNT; i++)
				list.indexOf(i);
			long endTime = System.nanoTime();
			System.out.println("> \tArrayList (indexOf)   = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		// ArrayList indexOf performance
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < ITERATION_COUNT; i++)
				list.get(i);
			long endTime = System.nanoTime();
			System.out.println("> \tArrayList (get)       = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		// IndexedCollection get performance
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < ITERATION_COUNT; i++)
				iCol.get(i);
			long endTime = System.nanoTime();
			System.out.println("> \tIndexedCollection     = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		
		System.out.println("Iterating to the elements by using 'iterator()' without gaps:");
		// ArrayList iterator performance
		{
			Iterator<Integer> it = list.iterator();
			long startTime = System.nanoTime();
			
			while(it.hasNext()) {
				it.next();
			}
			long endTime = System.nanoTime();
			System.out.println("> \tArrayList             = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		// IndexedCollection iterator performance
		{
			// first call to iterator() takes ~3ms and messes up test if executed after nanoTime()
			Iterator<Integer> it = iCol.iterator();
			long startTime = System.nanoTime();
			while(it.hasNext()) {
				it.next();
			}
			long endTime = System.nanoTime();
			System.out.println("> \tIndexedCollection     = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		
		// now only keep every tenth element in the array
		for (int i = 0; i < ITERATION_COUNT; i++) {
			if (i % 10 != 0) {
				list.remove(list.size() - 1);
				iCol.remove(i);
			}
		}
		System.out.println("> Iterating to the elements by using 'iterator()' with all but every"
				+ "tenth element missing:");
		// ArrayList iterator performance
		{
			Iterator<Integer> it = list.iterator();
			long startTime = System.nanoTime();
			while(it.hasNext()) {
				it.next();
			}
			long endTime = System.nanoTime();
			System.out.println("> \tArrayList             = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		// IndexedCollection iterator performance
		{
			Iterator<Integer> it = iCol.iterator();
			long startTime = System.nanoTime();
			while(it.hasNext()) {
				it.next();
			}
			long endTime = System.nanoTime();
			System.out.println("> \tIndexedCollection     = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
	}
}
