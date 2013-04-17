package app;

import org.apache.log4j.Logger;

import app.states.RequestTypes;

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

		if (med.startupPhase()) {
			med.fetchRelevantUsers(productName);
		}

		logger.debug("Launching offer");
		med.sendNotifications(RequestTypes.REQUEST_LAUNCH_OFFER,
				med.getUserName(), productName);
	}
}
