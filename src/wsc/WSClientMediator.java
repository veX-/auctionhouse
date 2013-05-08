package wsc;

import java.util.HashMap;
import java.util.Vector;

import app.model.User;

/**
 * Interface between the Web Service and the main Application Mediator class
 * 
 * @authors Andreea Hodea, Liviu Chircu, IDP 2013
 * 
 */
public interface WSClientMediator {

	/* user presence functions */
	public boolean logIn(String username, String password, String type,
			 						String ip, int port, Vector<String> products);

	public boolean logOut(String username);

	public boolean getInterestedUsers(String type, Vector<String> products);

	public boolean handleLogoutEvent(String userName);

	/* buyer action functions */
}
