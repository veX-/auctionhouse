package app.model;

import java.util.Vector;

public class Buyer extends User {

	private static final long serialVersionUID = 4997591897842952944L;

	public Buyer(String name, Vector<String> products) {
		this(name, "");
		this.setProducts(products);
	}

	public Buyer(String name, String password) {
		super(name, password);
		this.type = "buyer";
	}

	public Buyer(String name, String ip, int port) {
		super(name, ip, port);
		this.type = "buyer";
	}

	public Buyer(String name, String ip, int port, Vector<String> items) {
		super(name, ip, port, items);
		this.type = "buyer";
	}
}
