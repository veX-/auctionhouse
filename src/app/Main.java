package app;

import gui.ProductsView;

import java.awt.EventQueue;
import org.apache.log4j.Logger;

public class Main {

	private Mediator med;
	protected Logger l;

	public Main(String url, String listenIp, int listenPort, String configFile) {
		med = new Mediator(url, listenIp, listenPort, configFile);

		med.getNetMed().startServer(listenIp, listenPort);

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ProductsView(med.getGuiMed());
			}
		});

		// new SystemSimulator(med).execute();

	}

	public static void main(String[] args) {

		if (args.length < 4) {
			System.err.println("Usage: java Main <ws url> <ip> <port> <filename>");
			System.exit(1);
		}

		final String url = args[0];
		final String ip = args[1];
		final int port = Integer.parseInt(args[2]);
		final String configFile = args[3];
		
		new Main(url, ip, port, configFile);
	}
}
