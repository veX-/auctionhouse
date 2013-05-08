public class AuctionHouseService {
	public boolean register(String username, String type, String commaSepProd) {
		return true;
	}
	
	public String[] getDB(String username, String type) {
		int n = 5;
		String[] users = new String[5];

		for (int i = 0; i < n ; i++)
			users[i] = type + i;

		return users;
	}
}