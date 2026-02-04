package MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.QuizParticipation;

public class QuizParticpationDaoimpl implements QuizParticipationDao {

	@Override
	public int save(QuizParticipation qpn) {
	    String query = "INSERT INTO participantDetails(userId, name, email, correctAnswered, wrongAnswered, score, date, quizId) "
	                 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    int rows = 0;

	    try (Connection conn = ConnectionURL.getConnection(); 
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, qpn.getUserId());
	        ps.setString(2, qpn.getName());
	        ps.setString(3, qpn.getEmail());
	        ps.setInt(4, qpn.getCorrectAnswered());
	        ps.setInt(5, qpn.getWrongAnswered());
	        ps.setInt(6, qpn.getScore());
	        ps.setString(7, qpn.getDate());
	        ps.setInt(8, qpn.getQuizId());

	        rows = ps.executeUpdate();
	        System.out.println(rows + " row(s) inserted to quizParticipation.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return rows;
	}


	@Override
	public int Update(QuizParticipation qpn) {
		String query = "UPDATE participantDetails SET name = ?, email = ?, correctAnswered = ?, wrongAnswered = ?, score = ?, date = ? WHERE userId = ? AND quizId = ?";
		int rows = 0;

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, qpn.getName());
			ps.setString(2, qpn.getEmail());
			ps.setInt(3, qpn.getCorrectAnswered());
			ps.setInt(4, qpn.getWrongAnswered());
			ps.setInt(5, qpn.getScore());
			ps.setString(6, qpn.getDate());
			ps.setInt(7, qpn.getUserId());
			ps.setInt(8, qpn.getQuizId());

			rows = ps.executeUpdate();
			System.out.println(rows + " row(s) updated in quizParticipation.");
			return rows;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public boolean Delete(QuizParticipation qpn) {
		String query = "DELETE FROM participantDetails WHERE userId = ? AND quizId = ?";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, qpn.getUserId());
			ps.setInt(2, qpn.getQuizId());

			int rows = ps.executeUpdate();
			System.out.println(rows + " row(s) deleted from quizParticipation.");
			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<QuizParticipation> getAll() {
		List<QuizParticipation> qptList = new ArrayList<>();
		String query = "SELECT * FROM participantDetails";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int userId = rs.getInt("userId");
				String name = rs.getString("name");
				String email = rs.getString("email");
				int correctAnswered = rs.getInt("correctAnswered");
				int wrongAnswered = rs.getInt("wrongAnswered");
				int score = rs.getInt("score");
				String date = rs.getString("date");
				int quizId = rs.getInt("quizId");
				
				QuizParticipation qptt = new QuizParticipation(quizId, userId, name, email, score, correctAnswered, wrongAnswered, date);
				qptList.add(qptt);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qptList;
	}
}