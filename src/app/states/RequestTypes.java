package app.states;

/**
 * List of the possible contextual menu items of the application
 * 
 * @author Andreea Hodea & Liviu Chircu
 * 
 */
public class RequestTypes {

	/* GUI context menu specific requests */
	public static final int REQUEST_LAUNCH_OFFER = 0;
	public static final int REQUEST_DROP_OFFER = 1;
	public static final int REQUEST_MAKE_OFFER = 2;
	public static final int REQUEST_ACCEPT_OFFER = 3;
	public static final int REQUEST_REFUSE_OFFER = 4;
	public static final int REQUEST_DROP_AUCTION = 5;

	/* internally generated requests */
	public static final int REQUEST_INITIAL_TRANSFER = 6;
	public static final int REQUEST_TRANSFER = 7;

	/* WS related actions */
	public static final int REQUEST_LOGIN = 8;
	public static final int REQUEST_LOGOUT = 9;
	public static final int REQUEST_RELEVANT_BUYERS = 10;
	public static final int REQUEST_RELEVANT_SELLERS = 11;
	public static final int SYSTEM_LOGIN_FAILURE = 12;
	public static final int SYSTEM_NEW_LOGIN_EVENT = 13;
	
}
