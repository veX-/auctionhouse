package wsc;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.Mediator;
import app.model.Seller;
import app.model.User;
import app.states.RequestTypes;

public class WSClientMediatorImpl implements WSClientMediator {

	private Logger logger;
	private Call call = null;
	private Mediator med;

	public WSClientMediatorImpl(String url, Mediator med) {

		this.med = med;
		
		initLogger();

		try {
			URL endpoint = new URL(url);
			logger.debug("New URL: " + url);
			Service service = new Service();

			call = (Call)service.createCall();
			call.setTargetEndpointAddress(endpoint);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void initLogger() {
		logger = Logger.getLogger(WSClientMediatorImpl.class.getName());
	}

	public boolean register(String username, String pass, String type, String products) {

		boolean ret = false;

		call.setOperationName(new QName("register"));
		Object[] params = new Object[]{username, pass, type, products};
		try {
			Object r = call.invoke(params);
			if (r == null) {
				logger.error("No results");
			}
			else {
				ret = (boolean)r;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		logger.info("Register result: " + ret);
		return ret;
	}

	public boolean logIn(String username, String password,
			String type, String listenIp, int listenPort, Vector<String> products) {

		boolean ret = false;

		call.setOperationName(new QName("logIn"));
		Object[] params = new Object[1];
		try {
			Object r = call.invoke(params);
			if (r == null) {
				logger.error("No results");
			}
			else {
				ret = (boolean)r;
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		logger.info("Authentication for " + username + ": " + ret);		
		return ret;
	}
	
	public boolean logOut(String username) {
		
		boolean ret = false;

		call.setOperationName(new QName("logOut"));
		Object[] params = new Object[1];
		try {
			Object r = call.invoke(params);
			if (r == null) {
				logger.error("No results");
			}
			else {
				ret = (boolean)r;
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		logger.info("Logout for " + username + ": " + ret);		
		return ret;
	}

	@SuppressWarnings("unchecked")
	public boolean getInterestedUsers(String type, Vector<String> products) {
		
		HashMap<String, User> users = null;

		call.setOperationName(new QName("getInterested"));
		Object[] params = new Object[2];
		try {
			Object r = call.invoke(params);
			if (r == null) {
				logger.error("No results");
			}
			else {
				users = (HashMap<String, User>)r;
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		logger.info("Interested users: " + users);

		if (users == null)
			return false;

		if (type.equals(User.sellerType)) {
			for (User user : users.values()) {
				for (String prod : user.getProducts()) {
					med.saveUserConnectInfo(user.getName(), prod, user.getIp(), user.getPort());
				}
			}
		} else {
			/* notify all buyers that we're online. some buyers will reply with offer requests */
			Seller seller = new Seller(med.getUserName(), med.getListenIp(), med.getListenPort());
			seller.setProducts(products);
			
			for (User u : users.values()) {
				if (!med.getNetMed().sendLoginNotification(RequestTypes.REQUEST_LOGIN,
													u.getIp(), u.getPort(), seller)) {
					logger.error("Fail to send login notification to buyer " + u.getName());
				}
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void testGetDb() {
		call.setOperationName(new QName("getDB"));
		try {
			Object r = call.invoke(new Object[]{"me", "buyer", "127.0.0.1:60001"});
			if (r == null) {
				System.out.println("No results");
			}
			else {
				JSONObject users = new JSONObject((String)r);
				Iterator<String> it = users.keys();
				while (it.hasNext()) {
					String username = (String)it.next();
					System.out.println("Username: " + username);
					try {
						JSONArray products = (JSONArray)users.get(username);
						int n = products.length();
						for (int i = 0; i < n; i++)
							System.out.println(products.get(i));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

//	public static void main(String[] args) {
//		WSClientMediatorImpl cl = new WSClientMediatorImpl("http://localhost:8383/axis/services/AuctionHouseService", null);
//		cl.register();
//	}

	public boolean handleLogoutEvent(String userName) {
		return false;
	}
}