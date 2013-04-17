package app;

public class DummyCommand implements Command {

	@Override
	public void execute(int row, int col, int index) {
		System.out.println("Dummy command doing nothing for " + col + ":)");
	}

}
