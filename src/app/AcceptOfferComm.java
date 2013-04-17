package app;

import app.states.RequestTypes;
import app.states.State;

public class AcceptOfferComm implements Command {
	private Mediator med;

	public AcceptOfferComm(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int row, int col, int index) {
		String product = med.getProduct(row, ProductListModel.PROD_COL);
		String user = med.getUserInListCol(row, index);
		med.updateStatusList(user, product, State.STATE_OFFERACC);

		med.sendNotifications(RequestTypes.REQUEST_ACCEPT_OFFER,
				user, product);
	}
}
