package app;

import app.states.RequestTypes;

public class DropAuctionComm implements Command {

	private Mediator med;

	public DropAuctionComm(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int row, int col, int index) {
		String product = med.getProduct(row, col);
		String user = med.getUserInListCol(row, index);

		med.removeUserFromList(user, product);
		med.sendNotifications(RequestTypes.REQUEST_DROP_AUCTION, user, product);
	}

}
