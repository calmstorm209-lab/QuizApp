package MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.User;

public class Userdaoimpl implements Userdao {

	public int save(User user) {
	    int id = -1;
	    String query = "INSERT INTO users (user_name, email, password, user_role, score) VALUES (?, ?, ?, ?, ?)";

	    try (Connection conn = ConnectionURL.getConnection(); 
	         PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setString(1, user.getName());
	        ps.setString(2, user.getEmail());
	        ps.setString(3, user.getPassword());
	        ps.setString(4, user.getRole());
	        ps.setInt(5, user.getScore());

	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return id;
	}

	@Override
	public int Update(User user) {
		int rows = 0;
		String query = "UPDATE users SET user_name = ?, email = ?, password = ?, user_role = ?, score = ? WHERE user_id = ?";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			ps.setInt(5, user.getScore());
			ps.setInt(6, user.getUserId());

			rows = ps.executeUpdate();
			System.out.println(rows + " row(s) updated in users.");
			return rows;

		} catch (SQLException e) {
			e.printStackTrace();
			return rows;
		}
	}

	@Override
	public boolean Delete(User user) {
		String query = "DELETE FROM users WHERE user_id = ?";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, user.getUserId());

			int rows = ps.executeUpdate();
			System.out.println(rows + " row(s) deleted from users.");
			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<User> getAll() {
		List<User> userList = new ArrayList<>();
		String query = "SELECT * FROM users";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int userId = rs.getInt("user_id");
				String name = rs.getString("user_name");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String userRole = rs.getString("user_role");
				int score = rs.getInt("score");
				User user1 = new User(userId, name, email, password, userRole, score);

				userList.add(user1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	
}