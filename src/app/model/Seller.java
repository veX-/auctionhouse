package app.model;

import java.util.Vector;

public class Seller extends User {

	public Seller(String name, Vector<String> products) {
		this(name, "");
		this.setItems(products);
	}

	public Seller(String name, String password) {
		super(name, password);
		this.type = "seller";
	}

	public Seller(String name, String ip, int port, Vector<String> items) {
		super(name, ip, port, items);
		this.type = "seller";
	}
}
