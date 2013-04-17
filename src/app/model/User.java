package app.model;

import java.util.Vector;

abstract public class User {
	private String name;
	protected String type;
	private String password;
	private Vector<String> items;
	private Vector<Integer> bids;

	private String ip;
	private int port;

	public static final String sellerType = "seller";
	public static final String buyerType = "buyer";

	public User(String name) {
		this(name, null);
	}

	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public User(String name, String ip, int port, Vector<String> items) {
		this(name, "");
		this.ip = ip;
		this.port = port;
		this.items = items;
		
		if (items == null)
			this.bids = null;
		else
			this.bids = new Vector<Integer>(items.size());
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
	
	public int getBid(String product) {
		int i = items.indexOf(product);
		
		if (i == -1) {
			System.out.println("No such product: " + product);
			return -1;
		}
		
		return bids.get(i);
	}

	public Vector<String> getItems() {
		return items;
	}
	
	public void setItems(Vector<String> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "(" + name + " " + ip + " " + port + ")";
	}
}
