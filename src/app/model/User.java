package app.model;

import java.util.Vector;

/**
 * Please instantiate this only if you refer to a generic user (usual use case:
 * connection info)
 *
 * @author Andreea HODEA, Liviu CHIRCU
 *
 */
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 2697874685391642227L;

	private String name;
	protected String type;
	private String password;
	private Vector<String> products;
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

	public User(String name, String ip, int port, Vector<String> products) {
		this(name, "");
		this.ip = ip;
		this.port = port;
		this.products = products;
		
		if (products == null)
			this.bids = null;
		else
			this.bids = new Vector<Integer>(products.size());
	}
	
	public User(String name, String ip, int port) {
		this(name, "");
		this.ip = ip;
		this.port = port;
		this.products = new Vector<String>();
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
		int i = products.indexOf(product);
		
		if (i == -1) {
			System.out.println("No such product: " + product);
			return -1;
		}
		
		return bids.get(i);
	}
	
	public Vector<String> getProducts() {
		return products;
	}
	
	public void setProducts(Vector<String> products) {
		this.products = products;
	}

	public boolean contains(String product) {
		return products.contains(product);
	}

	public int getNoOfProducts() {
		return this.products.size();
	}

	public String getProduct(int index) {
		return this.products.get(index);
	}

	@Override
	public String toString() {
		return "(" + name + " " + ip + " " + port + ")" + products;
	}
}
