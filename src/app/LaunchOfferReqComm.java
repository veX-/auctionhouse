package app;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import app.model.User;
import app.states.RequestTypes;
import app.states.State;

public class LaunchOfferReqComm implements Command {

	private Mediator med;
	private Logger logger = null;

	public LaunchOfferReqComm(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int row, int col, int index) {
		logger = Logger.getLogger(LaunchOfferReqComm.class.getName());
		String productName = med.getProduct(row, col);

		if (med.fetchRelevantSellers(productName)) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			Hashtable<String, User> relUsers = med.getRelevantUsers();
			Map<String, String> users = new HashMap<String, String>();
			for (String key : relUsers.keySet())
				users.put(key, State.STATE_NOOFFER);
			med.updateUsersList(productName, users);
		}

		logger.debug("Launching offer");
		med.sendNotifications(RequestTypes.REQUEST_LAUNCH_OFFER,
				med.getUserName(), productName);
	}
}
