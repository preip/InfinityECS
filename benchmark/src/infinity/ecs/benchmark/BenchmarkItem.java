package infinity.ecs.benchmark;

public interface BenchmarkItem {
	public String getName();
	public void printMenu();
	public void handleInput(String input);
}
