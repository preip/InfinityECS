package infinity.ecs.benchmark.core;

import infinity.ecs.benchmark.BenchmarkItem;
import infinity.ecs.core.Component;
import infinity.ecs.core.ComponentType;
import infinity.ecs.core.Entity;
import infinity.ecs.core.Entity2;
import infinity.ecs.core.EntityManager;
import infinity.ecs.core.EntityManager2;
import infinity.ecs.exceptions.InfinityException;

public class CoreBenchmark implements BenchmarkItem {
	public String getName() {
		return "Core Benchmarks";
	}
	
	public void printMenu() {
		System.out.println("\n----------------------------------");
		System.out.println("- InfinityECS - Core Benchmarks: -");
		System.out.println("----------------------------------");
		System.out.println(" [01] EntityManager Comparison -> Entities");
		System.out.println(" [02] EntityManager Comparison -> Components (direct)");
		System.out.println(" [03] EntityManager Comparison -> Components (indirect)");
	}
	
	public void handleInput(String input) {
		switch (input) {
		case  "1":
		case "01":
			try { this.testEntityPerformance(); } catch (InfinityException e) {}
			break;
		case  "2":
		case "02":
			try { this.testComponentPerformanceDirect(); } catch (InfinityException e) {}
			break;
		case  "3":
		case "03":
			try { this.testComponentPerformanceIndirect(); } catch (InfinityException e) {}
			break;
		default:
			System.out.println("unrecognized command");
			break;
		}
	}
	
	public static final int NUMBER_OF_ITERATIONS = 10000;
	
	public void testEntityPerformance() throws InfinityException {
		System.out.println("\nTest the performance of the EntityMenager (EM1) and "
				+ "EntityManager2 (EM2) with " + NUMBER_OF_ITERATIONS + " entites");
		EntityManager  m1 = new EntityManager ();
		EntityManager2 m2 = new EntityManager2();
		float m1Total = 0; float m2Total = 0;
		
		System.out.println("> Creating new entities:");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
				m1.createEntity();
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
				m2.createEntity();
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Getting entities:");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
				m1.getEntity(i);
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
				m2.getEntity(i);
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Removing entities:");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
				m1.removeEntity(i);
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++)
				m2.removeEntity(i);
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Total Times:");
		System.out.println("> \tEM1 = " + m1Total + " ms");
		System.out.println("> \tEM2 = " + m2Total + " ms");
	}
	
	public void testComponentPerformanceDirect() throws InfinityException {
		System.out.println("\nTest the performance of the EntityMenager (EM1) and "
				+ "EntityManager2 (EM2) with " + NUMBER_OF_ITERATIONS + " components.");
		System.out.println("Component operations are accesed trough the entity itself.");
		EntityManager  m1 = new EntityManager ();
		EntityManager2 m2 = new EntityManager2();
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			m1.createEntity();
			m2.createEntity();
		}
		float m1Total = 0; float m2Total = 0;
		System.out.println("> Create components:");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				Entity e = m1.getEntity(i);
				e.addComponents(new ComponentA(), new ComponentB(), new ComponentC(), new ComponentD());
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				Entity2 e = m2.getEntity(i);
				e.addComponents(new ComponentA(), new ComponentB(), new ComponentC(), new ComponentD());
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Gett components:");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				Entity e = m1.getEntity(i);
				e.getComponent(ComponentType.get(ComponentA.class));
				e.getComponent(ComponentType.get(ComponentB.class));
				e.getComponent(ComponentType.get(ComponentC.class));
				e.getComponent(ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				Entity2 e = m2.getEntity(i);
				e.getComponent(ComponentType.get(ComponentA.class));
				e.getComponent(ComponentType.get(ComponentB.class));
				e.getComponent(ComponentType.get(ComponentC.class));
				e.getComponent(ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Remove components:");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				Entity e = m1.getEntity(i);
				e.removeComponent(ComponentType.get(ComponentA.class));
				e.removeComponent(ComponentType.get(ComponentB.class));
				e.removeComponent(ComponentType.get(ComponentC.class));
				e.removeComponent(ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				Entity2 e = m2.getEntity(i);
				e.removeComponent(ComponentType.get(ComponentA.class));
				e.removeComponent(ComponentType.get(ComponentB.class));
				e.removeComponent(ComponentType.get(ComponentC.class));
				e.removeComponent(ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Total Times)");
		System.out.println("> \tEM1 = " + m1Total + " ms");
		System.out.println("> \tEM2 = " + m2Total + " ms");
	}
	
	public void testComponentPerformanceIndirect() throws InfinityException {
		System.out.println("\nTest the performance of the EntityMenager (EM1) and "
				+ "EntityManager2 (EM2) with " + NUMBER_OF_ITERATIONS + " components.");
		System.out.println("Component operations are accesed trough the entity manager.");
		EntityManager  m1 = new EntityManager ();
		EntityManager2 m2 = new EntityManager2();
		
		float m1Total = 0; float m2Total = 0;
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			m1.createEntity();
			m2.createEntity();
		}
		System.out.println("> Creating Components");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				m1.addComponents(i, new ComponentA(), new ComponentB(), new ComponentC(), new ComponentD());
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				m2.addComponents(i, new ComponentA(), new ComponentB(), new ComponentC(), new ComponentD());
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Getting Components");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				m1.getComponent(i, ComponentType.get(ComponentA.class));
				m1.getComponent(i, ComponentType.get(ComponentB.class));
				m1.getComponent(i, ComponentType.get(ComponentC.class));
				m1.getComponent(i, ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				m2.getComponent(i, ComponentType.get(ComponentA.class));
				m2.getComponent(i, ComponentType.get(ComponentB.class));
				m2.getComponent(i, ComponentType.get(ComponentC.class));
				m2.getComponent(i, ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Removing Components");
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				m1.removeComponent(i, ComponentType.get(ComponentA.class));
				m1.removeComponent(i, ComponentType.get(ComponentB.class));
				m1.removeComponent(i, ComponentType.get(ComponentC.class));
				m1.removeComponent(i, ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM1 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m1Total += (float)(endTime - startTime) / 1000000.0f;
		}
		{
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				m2.removeComponent(i, ComponentType.get(ComponentA.class));
				m2.removeComponent(i, ComponentType.get(ComponentB.class));
				m2.removeComponent(i, ComponentType.get(ComponentC.class));
				m2.removeComponent(i, ComponentType.get(ComponentD.class));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tEM2 = " + (float)(endTime - startTime) / 1000000.0f + " ms");
			m2Total += (float)(endTime - startTime) / 1000000.0f;
		}
		System.out.println("> Total Times)");
		System.out.println("> \tEM1 = " + m1Total + " ms");
		System.out.println("> \tEM2 = " + m2Total + " ms");
	}

	public static class ComponentA implements Component {
		@Override
		public ComponentType getComponentType() {
			return ComponentType.get(getClass());
		}
	}
	public static class ComponentB implements Component {
		@Override
		public ComponentType getComponentType() {
			return ComponentType.get(getClass());
		}
	}
	public static class ComponentC implements Component {
		@Override
		public ComponentType getComponentType() {
			return ComponentType.get(getClass());
		}
	}
	public static class ComponentD implements Component {
		@Override
		public ComponentType getComponentType() {
			return ComponentType.get(getClass());
		}
	}
}
