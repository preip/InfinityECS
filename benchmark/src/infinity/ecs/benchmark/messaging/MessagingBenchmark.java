package infinity.ecs.benchmark.messaging;

import infinity.ecs.benchmark.BenchmarkItem;
import infinity.ecs.messaging.IndexedMessageDispatcher;
import infinity.ecs.messaging.Message;
import infinity.ecs.messaging.MessageDispatcher;
import infinity.ecs.messaging.MessageEndpoint;
import infinity.ecs.messaging.MessageType;
import infinity.ecs.messaging.SimpleMessageDispatcher;

public class MessagingBenchmark implements BenchmarkItem {

	static final int NUMBER_OF_EPS = 1000;
	static final int NUMBER_OF_ITERATIONS = 10000;
	
	@Override
	public String getName() {
		return "Messaging Benchmark";
	}

	@Override
	public void printMenu() {
		System.out.println("\n---------------------------------------");
		System.out.println("- InfinityECS - Messaging Benchmarks: -");
		System.out.println("---------------------------------------");
		System.out.println(" [01] MessageDispatcher Comparison");
	}

	@Override
	public void handleInput(String input) {
		switch (input) {
		case  "1":
		case "01":
			compareDispatchers();
			break;
		default:
			System.out.println("unrecognized command");
			break;
		}
	}
	
	public void compareDispatchers() {
		System.out.println("\nCompare SimpleMessageDispatcher and IndexedMessageDispatcher with "
				+ NUMBER_OF_EPS + " endpoints and " + NUMBER_OF_ITERATIONS + " messages:");
		
		System.out.println("> Send messages, which are only part of one Endpoint:");
		{
			MessageDispatcher disp = new SimpleMessageDispatcher();
			MessageEndpoint ep1 = disp.createEndpoint();
			ep1.register(MessageType.get(MessageC.class));
			MessageEndpoint ep2 = disp.createEndpoint();
			ep2.register(MessageType.get(MessageD.class));
			for (int i = 0; i < NUMBER_OF_EPS; i++) {
				MessageEndpoint ept = disp.createEndpoint();
				ept.register(MessageType.get(MessageA.class));
				ept.register(MessageType.get(MessageB.class));
			}
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				ep1.send(new MessageD(5)); // send a message that is only part of one endpoint
			}
			long endTime = System.nanoTime();
			
			System.out.println("> \tSimpleMessageDispatcher  = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		{
			MessageDispatcher fastDisp = new IndexedMessageDispatcher();
			MessageEndpoint ep1 = fastDisp.createEndpoint();
			ep1.register(MessageType.get(MessageC.class));
			MessageEndpoint ep2 = fastDisp.createEndpoint();
			ep2.register(MessageType.get(MessageD.class));
			for (int i = 0; i < NUMBER_OF_EPS; i++) {
				MessageEndpoint ept = fastDisp.createEndpoint();
				ept.register(MessageType.get(MessageA.class));
				ept.register(MessageType.get(MessageB.class));
			}
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				ep1.send(new MessageD(5));
			}
			long endTime = System.nanoTime();
			System.out.println("> \tIndexedMessageDispatcher = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		
		System.out.println("> Send messages, which are part of nearly every Endpoint:");
		{
			MessageDispatcher disp = new SimpleMessageDispatcher();
			MessageEndpoint ep1 = disp.createEndpoint();
			ep1.register(MessageType.get(MessageC.class));
			MessageEndpoint ep2 = disp.createEndpoint();
			ep2.register(MessageType.get(MessageD.class));
			for (int i = 0; i < NUMBER_OF_EPS; i++) {
				MessageEndpoint ept = disp.createEndpoint();
				ept.register(MessageType.get(MessageA.class));
				ept.register(MessageType.get(MessageB.class));
			}
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				ep1.send(new MessageA(5));
			}
			long endTime = System.nanoTime();
			
			System.out.println("> \tSimpleMessageDispatcher  = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
		{
			MessageDispatcher fastDisp = new IndexedMessageDispatcher();
			MessageEndpoint ep1 = fastDisp.createEndpoint();
			ep1.register(MessageType.get(MessageC.class));
			MessageEndpoint ep2 = fastDisp.createEndpoint();
			ep2.register(MessageType.get(MessageD.class));
			for (int i = 0; i < NUMBER_OF_EPS; i++) {
				MessageEndpoint ept = fastDisp.createEndpoint();
				ept.register(MessageType.get(MessageA.class));
				ept.register(MessageType.get(MessageB.class));
			}
			long startTime = System.nanoTime();
			for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
				ep1.send(new MessageA(5));
			}
			long endTime = System.nanoTime();
			
			System.out.println("> \tIndexedMessageDispatcher = " + (float)(endTime - startTime)
					/ 1000000.0f + " ms");
		}
	}

	//----------------------------------------------------------------------------------------------
	// Message Classes
	//----------------------------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private static class MessageA extends Message {
		public int x;
		public MessageA(int x) {
			this.x = x;
		}
	}
	@SuppressWarnings("unused")
	private static class MessageB extends Message {
		public int x;
		public MessageB(int x) {
			this.x = x;
		}
	}
	@SuppressWarnings("unused")
	private static class MessageC extends Message {
		public int x;
		public MessageC(int x) {
			this.x = x;
		}
	}
	@SuppressWarnings("unused")
	private static class MessageD extends Message {
		public int x;
		public MessageD(int x) {
			this.x = x;
		}
	}
}
