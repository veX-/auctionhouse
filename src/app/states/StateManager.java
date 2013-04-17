package app.states;

import java.util.Map;
import java.util.Vector;

import app.Command;
import app.Mediator;
import app.ProductListModel;
import app.model.User;

/**
 * For now there are two states of the application: Buyer state and Seller
 * state.
 * 
 * @authors Andreea-Cristina HODEA, Liviu CHIRCU
 * 
 */
public class StateManager {
	private Mediator med;
	private State currentState;

	public StateManager(Mediator med) {
		this.med = med;
		currentState = new BuyerState(med);
	}

	public void setBuyerState() {
		currentState = new BuyerState(med);
	}

	public void setSellerState() {
		currentState = new SellerState(med);
	}

	public String getListName() {
		return currentState.getListName();
	}

	public String getUserName() {
		return currentState.getUserName();
	}
	
	public Vector<String> getProducts() {
		return currentState.getProducts();
	}

	public void login(String username, Vector<String> products) {
		currentState.login(username, products);
	}

	public boolean logout() {
		return currentState.logout();
	}

	public Map<String, Command> getServiceMenuItems(String status) {
		return currentState.getServiceMenuItems(status);
	}

	public Map<String, Command> getContextualMenuItems(String status) {
		return currentState.getContextualMenuItems(status);
	}

	public boolean receiveStatusUpdate(int action, String name, String product,
			int price) {
		return currentState.receiveStatusUpdate(action, name, product, price);
	}

	public User createUser(String name, String ip, int port, Vector<String> products) {
		return currentState.createUser(name, ip, port, products);
	}
	
	public Vector<User> computeDestinations(int action, String userName, String product, int price) {
		return currentState.computeDestinations(action, userName, product, price);
	}

	public int getColumnCount() {
		return currentState.getColumnCount();
	}
	public void updateColumns(ProductListModel model) {
		currentState.updateColumns(model);
	}

	public void checkBestOffer(String userName, String productName, int value) {
		currentState.checkBestOffer(userName, productName, value);
	}
}
