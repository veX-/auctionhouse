package wsc;

import java.util.Map;
import java.util.Vector;

import app.model.GenericUser;

/**
 * Interface between the Web Service and the main Application Mediator class
 * 
 * @authors Andreea Hodea, Liviu Chircu, IDP 2013
 * 
 */
public interface WSClientMediator {

	public void initLogger();

	/* If login is successful return list of products of current user, otherwise return null. */
	public Vector<String> logIn(String username, String password, String type,
			 			 String listenIp, int listenPort);

	public boolean logOut(String username);

	public boolean register(String username, String pass, String type, String products);

	/* Return all relevant/interested users with relevant info associated. */
	public Map<String, GenericUser> getRelevantUsers();
	/* Return info of users relevant to product. The caller usually calls this when needs connection info. */
	public Map<String, GenericUser> getRelevantUsers(String product);

	public boolean handleLogoutEvent(String userName);
}
