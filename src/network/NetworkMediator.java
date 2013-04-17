package network;

import java.util.Vector;

import app.model.User;

public interface NetworkMediator {
	public boolean sendItem(String name, int value);

	/**
	 * 
	 * @param users
	 *            Name of the users to be announced.
	 * @param ProductId
	 *            Product to be exluded from auction.
	 * @param userId
	 *            User dropping the auction.
	 */
	public void sendDropAnnouncement(String[] users, String productName,
			String userId);

	public void startLoginServer(String ip, int port);
	public void startServer(String ip, int port);
	
	public boolean validateUsername(String username, String password, String type,
									String ip, int port, Vector<String> products);
	
	public void setLoginFailed();
	
	public void setLoginSuccess();
	
	public boolean sendLoginNotification(int action, String ip, int port, User user);
	
	public boolean fetchRelevantUsers(User user);
	
	public boolean sendNotifications(int action, String userName, String product,
			int price, Vector<User> destinations);
}
