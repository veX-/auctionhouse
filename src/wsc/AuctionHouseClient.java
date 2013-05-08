package wsc;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.*;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.*;

public class AuctionHouseClient {

	private Call call = null;

	public AuctionHouseClient(String url) {
		try {
			URL endpoint = new URL(url);
			Service service = new Service();

			call = (Call)service.createCall();
			call.setTargetEndpointAddress(endpoint);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	public void testRegister() {
		call.setOperationName(new QName("register"));
		Object[] params = new Object[1];
		try {
			Object r = call.invoke(params);
			if (r == null) {
				System.out.println("No results");
			}
			else {
				boolean response = (boolean)r;
				System.out.println(response);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		AuctionHouseClient cl = new AuctionHouseClient("http://localhost:8383/axis/services/AuctionHouseService");
		cl.testRegister();
	}
}