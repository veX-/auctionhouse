package app;

import gui.ProductsView;

import java.awt.EventQueue;
import org.apache.log4j.Logger;

public class Main {

	private Mediator med;
	protected Logger l;

	public Main(String ip, int port, String configFile) {
		med = new Mediator(ip, port, configFile);

		med.getNetMed().startServer(ip, port);

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ProductsView(med.getGuiMed());
			}
		});

		// new SystemSimulator(med).execute();

	}

	public static void main(String[] args) {
		
		if (args.length < 2) {
			System.err.println("Usage: java Main <ip> <port> [<filename>]");
			System.exit(1);
		}
		
		if (args.length == 2) {
			
			final String ip = args[0];
			final int port = Integer.parseInt(args[1]);
			
			System.out.println("[Auction House]: Running in Login Server Mode");
			
			Mediator med = new Mediator(ip, port, null);
			med.getNetMed().startLoginServer(ip, port);
			
		} else {
			
			final String ip = args[0];
			final int port = Integer.parseInt(args[1]);
			final String configFile = args[2];
	
			new Main(ip, port, configFile);
		}
	}
}
