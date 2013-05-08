import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		HashMap<String, List<String>> temp = new HashMap<String, List<String>>();

		/* Select info from db */
		String prodQ = "SELECT u.Name user, p.Name prod FROM users u, products p, users_products up, " + 
				"(SELECT u.UserType, ProductId from users_products up, users u where u.Name=?) res " + 
				"WHERE p.Id=res.ProductId AND u.UserType!=res.UserType AND p.Id=up.ProductId AND u.Id=up.UserId";
		PreparedStatement stmt;
		try {
			stmt = cm.getConnection().prepareStatement(prodQ);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String user = rs.getString("user");
				System.out.println(user);
				if (temp.get(user) == null)
					temp.put(user, new ArrayList<String>());
				List<String> prods = temp.get(user);
				String prod = rs.getString("prod");
				prods.add(prod);
			}

			/* Prepare json to be returned to client. */
			for (Map.Entry<String, List<String>> e : temp.entrySet()) {
				for (String s : e.getValue())
					products.put(s);
				users.put(e.getKey(), products);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return users.toString();
	}
}