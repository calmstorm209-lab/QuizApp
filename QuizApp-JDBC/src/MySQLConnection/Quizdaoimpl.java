package MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Quiz;

public class Quizdaoimpl implements Quizdao {

	@Override
	public int save(Quiz quiz) {
	    int id = -1;
	    String query = "INSERT INTO quiz (quiz_title, description, creator_id, createor_name, quiz_code) VALUES (?, ?, ?, ?, ?)";

	    try (Connection conn = ConnectionURL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setString(1, quiz.getTitle());
	        ps.setString(2, quiz.getdescription());
	        ps.setInt(3, quiz.getCreatorId());
	        ps.setString(4, quiz.getcreatorName());
	        ps.setInt(5, quiz.getquizCode());

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
	public int Update(Quiz quiz) {
	    String query = "UPDATE quiz SET quiz_title=?, description=?, creator_id=?, createor_name=?, quiz_code=? WHERE quiz_id=?";
	    try (Connection conn = ConnectionURL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setString(1, quiz.getTitle());
	        ps.setString(2, quiz.getdescription());
	        ps.setInt(3, quiz.getCreatorId());
	        ps.setString(4, quiz.getcreatorName());
	        ps.setInt(5, quiz.getquizCode());
	        ps.setInt(6, quiz.getId());

	        return ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return 0;
	    }
	}

	@Override
	public boolean Delete(Quiz quiz) {

		String query1 = "DELETE FROM quiz WHERE quiz_id = ?";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query1)) {

			ps.setInt(1, quiz.getId());

			int rows = ps.executeUpdate();
			System.out.println(rows + " row(s) deleted from quiz.");
			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Quiz> getAll() {
		List<Quiz> quizList = new ArrayList<>();
		String query = "SELECT * FROM quiz";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int quizId = rs.getInt("quiz_id");
				String title = rs.getString("quiz_title");
				String description = rs.getString("description");
				int creatorId = rs.getInt("creator_id");
				String creatorName = rs.getString("createor_name");
				int quizCode = rs.getInt("quiz_code");
				Quiz quiz1 = new Quiz(quizId, creatorId, title, description, creatorName, quizCode);

				quizList.add(quiz1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quizList;
	}
}