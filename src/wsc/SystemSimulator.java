package wsc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import app.model.User;

/**
 * SystemSimulator - Mockup of the future Web Service Client
 * 
 * Useful only in the initial phases
 * 
 * @authors: Andreea Hodea, Liviu Chircu, IDP 2013
 */
public class SystemSimulator extends SwingWorker<Boolean, String> {

	public static final int LOG_IN = 0;
	public static final int LOG_OUT = 1;
	public static final int OFFER_REQ = 2;
	public static final int OFFER_REJ = 3;
	public static final int NEW_BID = 4;
	public static final int ACC_BID = 5;
	public static final int BETTER_BID = 6;
	public static final int REJ_BID = 7;
	public static final int TRANSFER = 8;

	public static final String productList[] = { "beer", "milk", "honey",
			"cola", "bread", "biscuits", "wafers", "peanuts", "shoes",
			"shirts", "hats", "sweaters", "glasses", "bracers", "belts",
			"necklaces" };

	private WSClientMediator med;

	public SystemSimulator(WSClientMediator med) {
		this.med = med;
	}

	/**
	 * doInBackground() - infinite loop on a SwingWorker thread which generates
	 * 6 possible events
	 */
	@Override
	protected Boolean doInBackground() throws Exception {

		Random rnd = new Random();

		int userIndex = 0;

		do {
			int action = rnd.nextInt(2);

			System.out.println("[New action]: " + action);

			switch (action) {
			case LOG_IN:
				String userType = rnd.nextInt(2) == 0 ? User.buyerType
						: User.sellerType;
				int noProd = rnd.nextInt(5) + 3;

				if (med.loadInitialProdList("User" + userIndex, userType,
						genProducts(noProd)) != 0) {
					System.err.println("User initialization error!");
				}

				userIndex++;

				break;

			case LOG_OUT:
				if (userIndex >= 0) {
					if (!med.handleLogoutEvent("User" + userIndex)) {
						System.err.println("Error sending LOG_OUT action!");
					}

					userIndex -= (userIndex == 0 ? 0 : 1);
				}

				break;
			}

			Thread.sleep(1000);

		} while (true);
	}

	public ArrayList<String> genProducts(int no) {

		Random rnd = new Random();

		ArrayList<String> products = new ArrayList<String>();

		for (int j = 0; j < no; j++) {

			String prod = productList[rnd.nextInt(productList.length)];
			if (!products.contains(prod))
				products.add(prod);
		}

		return products;
	}

	@Override
	protected void process(List<String> chunks) {

	}

	@Override
	protected void done() {
		System.out.println("DONE!");
	}
}
