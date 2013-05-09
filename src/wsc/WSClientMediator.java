package wsc;


/**
 * Interface between the Web Service and the main Application Mediator class
 * 
 * @authors Andreea Hodea, Liviu Chircu, IDP 2013
 * 
 */
public interface WSClientMediator {

	public void initLogger();

	/* user presence functions */
	public boolean logIn(String username, String password, String type,
			 			 String listenIp, int listenPort);

	public boolean logOut(String username);

	public boolean register(String username, String pass, String type, String products);

	public boolean getInterestedUsers(String username, String type, String connInfo);

	public boolean handleLogoutEvent(String userName);
	/* buyer action functions */
}
