import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Auction House Service main role is to persist and retrieve users, products
 * and the association between users and products.
 * 
 * The service persists data to a database having the parameters specified in
 * DatabaseInfo.java. In order to connect to the db, the service makes use of
 * ConnectionManager.
 *
 * @author Andreea HODEA, Liviu CHIRCU; IDP - AuctionHouse 2013
 *
 */
public class AuctionHouseService {

	private static ConnectionManager cm = new ConnectionManager();

	public boolean logIn(String username, String password, String type,
						 String userConn) {

		System.out.println(String.format("%s %s %s %s", username, password,
														type, userConn));

		String getUserQuery		= "SELECT * FROM users WHERE Name=?";
		ResultSet res;

		try {
			PreparedStatement stmt = cm.getConnection().prepareStatement(
					getUserQuery, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, username);
			
			res = stmt.executeQuery();

			/* user not registered */
			if (!res.next())
				return false;

			int uId = res.getInt("id");

			/* password mismatch */
			String dbPassword = res.getString("password");

			if (dbPassword == null && !password.isEmpty())
				return false;

			if (dbPassword != null && !dbPassword.equals(password))
				return false;

			/* type mismatch or user already logged in (has ip and port) */
			if (!res.getString("usertype").equals(type) ||
					res.getString("ip") != null ||
					res.getString("port") != null) {
				return false;
			}

			/* User can be successfully logged in. 
			 * Update his entry in the users table */
			String updateQ = "UPDATE users SET Ip=?, Port=? WHERE Id=?";
			String[] connInfo = userConn.split(":");
			stmt = cm.getConnection().prepareStatement(updateQ);
			stmt.setString(1, connInfo[0]);
			stmt.setInt(2, Integer.parseInt(connInfo[1]));
			stmt.setInt(3, uId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean logOut(String username) {
		int success = 0;
		String logoutQ = "UPDATE users SET Ip=?, Port=? WHERE Name=?";
		PreparedStatement stmt;

		try {
			stmt = cm.getConnection().prepareStatement(logoutQ);
			stmt.setNull(1, Types.VARCHAR);
			stmt.setNull(2, Types.INTEGER);
			stmt.setString(3, username);
			System.out.println("Execute logOut query: " + stmt);
			success = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return success > 0;
	}
	
	public boolean register(String username, String pass, String type,
			String products) {
		System.out.println(String.format("Register: %s - %s - %s - %s",
				username, pass, type, products));

		String regUQ = "INSERT INTO users(Name, Password, UserType) VALUES(?, ?, ?)";
		String regPQ = "INSERT INTO products(Name) VALUES(?)";
		String getPQ = "SELECT * FROM products WHERE Name=?";
		String reqAQ = "INSERT INTO users_products VALUES(?, ?)";
		ResultSet res;
		String[] prods = products.split(",");
		try {
			/* Insert user. */
			PreparedStatement stmt = cm.getConnection().prepareStatement(
					regUQ, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, username);
			if (pass == null || pass.isEmpty())
				stmt.setNull(2, java.sql.Types.VARCHAR);
			else
				stmt.setString(2, pass);
			stmt.setString(3, type);
			int success = stmt.executeUpdate();
			if (success < 1)
				return false;
			res = stmt.getGeneratedKeys();
			res.next();
			int uId = res.getInt(1);

			/* Insert products. */
			stmt = cm.getConnection().prepareStatement(
					regPQ, Statement.RETURN_GENERATED_KEYS);
			PreparedStatement getStmt = cm.getConnection().
					prepareStatement(getPQ);
			PreparedStatement assocStmt = cm.getConnection().
					prepareStatement(reqAQ);
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
				System.out.println(String.format("(UserId, ProductId) = (%d, %d)",
						uId, pId));

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

	private class QUser {
		String username;
		String ip;
		Integer port;
		public QUser(String username, String ip, Integer port) {
			super();
			this.username = username;
			this.ip = ip;
			this.port = port;
		}

		public boolean equals(Object o) {
			if (o instanceof QUser) {
				QUser qu = (QUser)o;
				return qu.hashCode() == this.hashCode();
			}
			return false;
		}

		public int hashCode() {
			return username.hashCode() * 23 + ip.hashCode() * 17 + port.hashCode();
		}
	}

	/**
	 * Return relevant users and the products they want/offer as JSON and
	 * update user connection details.
	 *
	 * @param username User issuing the request.
	 * @param type Type of user issuing the request.
	 * @param userconn ip:port for username.
	 * @return relevant users and associated products.
	 */
	public String getDB(String username, String type, String userconn) {
		JSONObject users = new JSONObject();
		HashMap<QUser, List<String>> temp = new HashMap<QUser, List<String>>();

		/* Select info from db */
		String prodQ = "SELECT u.Name user, u.Ip ip, u.Port port, p.Name prod " +
					"FROM users u, products p, users_products up " +
					"WHERE p.Id IN (select distinct ProductId from users_products up, users u where u.Name=?) " +
					"AND (u.UserType!=? OR u.Name=?) AND p.Id=up.ProductId AND u.Id=up.UserId AND u.Ip IS NOT NULL " +
					"ORDER BY u.Name;";
		PreparedStatement stmt;

		try {
			stmt = cm.getConnection().prepareStatement(prodQ);
			stmt.setString(1, username);
			stmt.setString(2, type);
			stmt.setString(3, username);
			System.out.println("Execute query:" + stmt);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				QUser user = new QUser(rs.getString("user"),
						rs.getString("ip"), rs.getInt("port"));
				if (temp.get(user) == null)
					temp.put(user, new ArrayList<String>());
				List<String> prods = temp.get(user);
				String prod = rs.getString("prod");
				if (!prods.contains(prod))
					prods.add(prod);
			}

			/* Prepare json to be returned to client. */
			for (Map.Entry<QUser, List<String>> e : temp.entrySet()) {
				QUser qu = e.getKey();
				System.out.println("\tFound user " + qu.username);

				JSONArray products = new JSONArray();
				for (String s : e.getValue())
					products.put(s);
				JSONObject userInfo = new JSONObject();
				userInfo.put("ip", qu.ip);
				userInfo.put("port", qu.port);
				userInfo.put("products", products);
				users.put(qu.username, userInfo);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return users.toString();
	}
}
