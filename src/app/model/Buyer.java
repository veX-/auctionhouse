package app.model;

import java.util.Vector;

public class Buyer extends User {

	public Buyer(String name, Vector<String> products) {
		this(name, "");
		this.setItems(products);
	}

	public Buyer(String name, String password) {
		super(name, password);
		this.type = "buyer";
	}

	public Buyer(String name, String ip, int port, Vector<String> items) {
		super(name, ip, port, items);
		this.type = "buyer";
	}
}
