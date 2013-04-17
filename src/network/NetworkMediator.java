package network;

import java.util.Vector;

import app.model.User;

public interface NetworkMediator {
	public boolean sendItem(String name, int value);

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
	
	public boolean sendNotifications(int action, String userName, String ip, int port,
			String product, Vector<User> destinations);
}
