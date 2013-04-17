package wsc;

import java.util.List;

/**
 * Interface between the Web Service and the main Application Mediator class
 * 
 * @authors Andreea Hodea, Liviu Chircu, IDP 2013
 * 
 */
public interface WSClientMediator {

	/* user presence functions */
	public int loadInitialProdList(String username, String type,
			List<String> products);

	public boolean handleLogoutEvent(String userName);

	/* buyer action functions */
}
