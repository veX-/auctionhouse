package wsc;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
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

	public boolean logIn(String username, String password, String type,
						 String listenIp, int listenPort) {

		boolean ret = false;

		call.setOperationName(new QName("logIn"));
		
		try {
			Object r = call.invoke(new Object[] { username, password, type,
												  listenIp + ":" + listenPort });
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
		Object[] params = new Object[] {username};
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
	public boolean getInterestedUsers(String username, String type, String connInfo) {
		String result = null;

		call.setOperationName(new QName("getDB"));
		
		try {
			Object r = call.invoke(new Object[] { username, type, connInfo });
			if (r == null) {
				logger.error("No results");
			}
			else {
				result = (String)r;
			}

		} catch (RemoteException e) {
			e.printStackTrace();
		}

		logger.info("Interested users: " + result);

		if (result == null)
			return false;
		
		String self = med.getUserName();

		try {
			if (type.equals(User.sellerType)) {
				JSONObject users = new JSONObject(result);
				
				Iterator<String> it = users.keys();
				
				while (it.hasNext()) {
					String name = (String)it.next();
					
					if (name.equals(self))
						continue;
	
					JSONObject info = (JSONObject)users.get(name);
					
					String listenIp = (String)info.get("ip");
					Integer listenPort = (Integer)info.get("port");
					
					JSONArray products = (JSONArray)info.get("products");
					
					int n = products.length();
					for (int i = 0; i < n; i++)
						med.saveUserConnectInfo(name, (String)products.get(i),
												listenIp, listenPort);
				}
				
			} else {
				JSONObject users = null;
				Seller seller = null; 
				
				users = new JSONObject(result);
				
				JSONArray selfArray = (JSONArray)users.get(self);
				Vector<String> selfProducts = new Vector<String>();
				
				int n = selfArray.length();
				for (int i = 0; i < n; i++)
					selfProducts.add((String)selfArray.get(i));
			
				seller = new Seller(med.getUserName(), med.getListenIp(), med.getListenPort());
				seller.setProducts(selfProducts);
			
				Iterator<String> it = users.keys();
				while (it.hasNext()) {
					String name = (String)it.next();
			
					if (name.equals(self))
						continue;
				
					JSONObject info = (JSONObject)users.get(name);
					
					String buyerIp = (String)info.get("ip");
					Integer buyerPort = (Integer)info.get("port");
					
					JSONArray products = (JSONArray)info.get("products");
					
					//TODO: locally save the entire prodlist for all buyers
					
					if (!med.getNetMed().sendLoginNotification(RequestTypes.REQUEST_LOGIN,
							buyerIp, buyerPort, seller)) {
							logger.error("Fail to send login notification to buyer " + name);
					}
				}
			}	

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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