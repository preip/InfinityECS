package infinity.ecs.benchmark;

import infinity.ecs.benchmark.messaging.MessagingBenchmark;
import infinity.ecs.benchmark.utils.UtilBenchmark;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {

	public static void main(String[] args) {
		App app = new App();
		app.run();
	}
	
	//----------------------------------------------------------------------------------------------

	private final List<BenchmarkItem> _items;
	private BenchmarkItem _current;
	private BufferedReader _reader;
	private boolean _exiting;
	
	public App() {
		_items = new ArrayList<BenchmarkItem>();
		_current = null;
		_reader = new BufferedReader(new InputStreamReader(System.in));
		_exiting = false;
		setup();
	}
	
	/**
	 * Setup all benchmark items.
	 */
	private void setup() {
		// ADD NEW BENCHMARK ITEMS HERE
		_items.add(new MessagingBenchmark());
		_items.add(new UtilBenchmark());
	}
	
	/**
	 * Runs the application.
	 */
	public void run() {
		while(!_exiting) {
			printMenu();
			handleInput();
		}
	}
	
	/**
	 * Prints the menu of the current item or the main menu.
	 */
	private void printMenu() {
		if (_current == null) {
			printMainMenu();
			System.out.print("\nmake your selection or type 'exit' to close the application:\n> ");
		}
		else {
			_current.printMenu();
			System.out.print("\nmake your selection, type 'back' to go the main menu or type 'exit' to close the application:\n> ");
		}
	}
	
	/**
	 * Prints the main menu.
	 */
	private void printMainMenu() {
		System.out.println("\n---------------------------------------");
		System.out.println("- InfinityECS - Benchmark Application -");
		System.out.println("---------------------------------------");
		for (int i = 0; i < _items.size(); i++)
			if (i < 9)
				System.out.println("  [0" + (i + 1) + "] " + _items.get(i).getName());
	}
	
	/**
	 * Handles user input.
	 */
	private void handleInput() {
		String input = null;
		try {
			input = _reader.readLine();
		} catch(IOException e) {
			System.out.println("unrecognized command");
		}
		
		switch(input) {
		case "exit":
			_exiting = true;
			return;
		case "back":
			_current = null;
			return;
		}
		
		if (_current == null)
			handleInput(input);
		else
			_current.handleInput(input);
	}
	
	/**
	 * Handles main menu input
	 * @param input
	 */
	private void handleInput(String input) {
		try {
			int i = Integer.parseInt(input);
			if (i > 0 && i <= _items.size())
				_current = _items.get(i - 1);
			else
				System.out.println("unrecognized command");
		} catch(Exception e) {
			System.out.println("unrecognized command");
		}
	}
}
