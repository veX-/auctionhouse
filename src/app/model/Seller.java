package app.model;

import java.util.Vector;

public class Seller extends User {

	private static final long serialVersionUID = -6927902097257890430L;

	public Seller(String name, Vector<String> products) {
		this(name, "");
		this.setProducts(products);
	}

	public Seller(String name, String password) {
		super(name, password);
		this.type = "seller";
	}
	
	public Seller(String name, String ip, int port) {
		super(name, ip, port);
		this.type = "seller";
	}

	public Seller(String name, String ip, int port, Vector<String> items) {
		super(name, ip, port, items);
		this.type = "seller";
	}
}
