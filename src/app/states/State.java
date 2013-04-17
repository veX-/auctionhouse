package app.states;

import java.util.Map;
import java.util.Vector;

import app.Command;
import app.Mediator;
import app.ProductListModel;
import app.model.User;

public abstract class State {
	public static final String STATE_INACTIVE = "Inactive";
	public static final String STATE_NOOFFER = "NoOffer";
	public static final String STATE_OFFERMADE = "OfferMade";
	public static final String STATE_OFFERACC = "OfferAccepted";
	public static final String STATE_OFFERREF = "OfferRefused";
	public static final String STATE_OFFERE = "OfferExceeded";
	public static final String STATE_TRANSFERS = "TransferStarted";
	public static final String STATE_TRANSFERP = "TransferInProgress";
	public static final String STATE_TRANSFERC = "TransferCompleted";
	public static final String STATE_TRANSFERF = "TransferFailed";

	public static Vector<Integer> actions;

	protected Mediator med;
	protected User user;

	public State(Mediator med) {
		this.med = med;
	}

	/**
	 * 
	 * @return The opposite type
	 */
	public abstract String getListName();

	public abstract void login(String username, Vector<String> products);

	public abstract boolean logout();

	public abstract Map<String, Command> getServiceMenuItems(String status);

	public abstract Map<String, Command> getContextualMenuItems(String status);

	public abstract boolean receiveStatusUpdate(int action, String name,
			String product, int price);

	public User getUser() {
		return user;
	}
	
	public String getUserName() {
		return user.getName();
	}
	
	public Vector<String> getProducts() {
		return user.getProducts();
	}
	
	public abstract User createUser(String name, String ip, int port, Vector<String> items);
	
	public abstract Vector<User> computeDestinations(int action, String userName, String product, int price);

	public abstract int getColumnCount();
	public abstract void updateColumns(ProductListModel model);
	public abstract boolean allowedLogout();

	public abstract void checkBestOffer(String userName, String productName, int value);
}
