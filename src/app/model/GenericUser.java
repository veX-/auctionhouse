package app.model;

import java.util.Vector;

/**
 * Simple generic user useful to store minimum information required for users communication.
 * 
 * @author Andreea HODEA, Liviu CHIRCU; IDP 2013
 *
 */
public class GenericUser {
	private String username;
	private String ip;
	private Integer port;
	private Vector<String> products;

	public GenericUser(String username, String ip, Integer port,
			Vector<String> products) {
		super();
		this.username = username;
		this.ip = ip;
		this.port = port;
		this.products = new Vector<String>(products);
	}

	public boolean contains(String product) {
		return products.contains(product);
	}

	public String getUsername() {
		return username;
	}

	public String getIp() {
		return ip;
	}

	public Integer getPort() {
		return port;
	}

	public int getNoOfProducts() {
		return this.products.size();
	}

	public String getProduct(int index) {
		return this.products.get(index);
	}
}
