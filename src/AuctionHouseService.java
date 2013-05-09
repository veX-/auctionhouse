import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuctionHouseService {
	
	private static ConnectionManager cm = new ConnectionManager();
	
	public boolean register(String username, String type, String products) {
		System.out.println("Register:" + username + " - " + type + " - " + products);

		String regUQ = "INSERT INTO users(Name, UserType) VALUES(?, ?)";
		String regPQ = "INSERT INTO products(Name) VALUES(?)";
		String getPQ = "SELECT * FROM products WHERE Name=?";
		String reqAQ = "INSERT INTO users_products VALUES(?, ?)";
		ResultSet res;
		String[] prods = products.split(",");
		try {
			/* Insert user. */
			PreparedStatement stmt = cm.getConnection().
					prepareStatement(regUQ, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, username);
			stmt.setString(2, type);
			int success = stmt.executeUpdate();
			if (success < 1)
				return false;
			res = stmt.getGeneratedKeys();
			res.next();
			int uId = res.getInt(1);

			/* Insert products. */
			stmt = cm.getConnection().
					prepareStatement(regPQ, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement getStmt = cm.getConnection().prepareStatement(getPQ);
			PreparedStatement assocStmt = cm.getConnection().prepareStatement(reqAQ);
			for (int i = 0; i < prods.length; i++) {
				stmt.clearParameters();
				stmt.setString(1, prods[i]);
				try {
					success = stmt.executeUpdate();
				} catch(SQLException exc) {
					success = 0;
				}
				int pId;
				if (success > 0) {
					res = stmt.getGeneratedKeys();
					res.next();
					pId = res.getInt(1);
				}
				else {
					getStmt.clearParameters();
					getStmt.setString(1, prods[i]);
					res = getStmt.executeQuery();
					res.next();
					pId = res.getInt("Id");
				}
				System.out.println("(UserId, ProductId) = (" + uId + ", " + pId + ")");

				/* Associate user and product. */
				assocStmt.clearParameters();
				assocStmt.setInt(1, uId);
				assocStmt.setInt(2, pId);
				assocStmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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