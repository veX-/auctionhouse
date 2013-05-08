package network;

import java.util.Vector;

import app.model.User;

public interface NetworkMediator {
	public void initLogger();

	public void startServer(String ip, int port);

	public void setLoginFailed();
	
	public void setLoginSuccess();
	
	public boolean sendLoginNotification(int action, String ip, int port, User user);
	
	public boolean sendNotifications(int action, String userName, String product,
			int price, Vector<User> destinations);
	
	public boolean sendNotifications(int action, String userName, String ip, int port,
			String product, Vector<User> destinations);
}
