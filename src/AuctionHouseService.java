import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuctionHouseService {
	
	private static ConnectionManager cm = new ConnectionManager();
	
	public boolean register(String username, String type, String commaSepProd) {
		return true;
	}
	
	public String getDB(String username, String type, String userconn) {
		JSONObject users = new JSONObject();
		JSONArray products = new JSONArray();

		/* Select info from db */
		String prodQ = "SELECT * FROM products";
		Statement stmt;
		try {
			stmt = cm.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(prodQ);
			while (rs.next()) {
				String prod = rs.getString("Name");
				products.put(prod);
			}
			products.put(username);
			products.put(type);
			products.put(userconn);
			users.put(type, products);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return users.toString();
	}
}