package app;

import java.util.HashMap;
import java.util.Map;

import app.states.RequestTypes;
import app.states.State;

public class DropOfferReqComm implements Command {
	private Mediator med;

	public DropOfferReqComm(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int row, int col, int index) {
		String productName = med.getProduct(row, col);

		Map<String, String> users = new HashMap<String, String>();
		users.put("", State.STATE_INACTIVE);
		med.updateUsersList(productName, users);
		med.sendNotifications(RequestTypes.REQUEST_DROP_OFFER,
				med.getUserName(), productName);
	}
}
