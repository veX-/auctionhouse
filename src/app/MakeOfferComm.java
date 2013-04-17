package app;

import app.states.RequestTypes;
import app.states.State;

public class MakeOfferComm implements Command {
	private Mediator med;

	public MakeOfferComm(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int row, int offerredPrice, int index) {
		String product = med.getProduct(row, ProductListModel.PROD_COL);
		String user = med.getUserInListCol(row, index);
		int price = offerredPrice;

		med.updateStatusList(user, product, offerredPrice, State.STATE_OFFERMADE);
		med.sendNotifications(RequestTypes.REQUEST_MAKE_OFFER,
				user, product, price);

	}

}
