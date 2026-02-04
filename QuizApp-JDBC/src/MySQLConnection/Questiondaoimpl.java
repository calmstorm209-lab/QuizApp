package MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Question;

public class Questiondaoimpl implements Questiondao {

	public boolean getQuestion(int question_id) {
		String query = "SELECT * FROM question WHERE question_id = ?";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, question_id);
			ResultSet rs = ps.executeQuery();
			return rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int save(Question question) {
	    int id = -1;
	    String query = "INSERT INTO question (text, type, quiz_id, mark) VALUES (?, ?, ?, ?)";

	    try (Connection conn = ConnectionURL.getConnection(); 
	         PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setString(1, question.getText());
	        ps.setString(2, question.getType());
	        ps.setInt(3, question.getquizId());
	        ps.setInt(4, question.getMark());

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
	public int Update(Question question) {
		int rows = 0;
		String query = "UPDATE question SET text = ?, type = ?, quiz_id = ?, mark = ? WHERE question_id = ?";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, question.getText());
			ps.setString(2, question.getType());
			ps.setInt(3, question.getquizId());
			ps.setInt(4, question.getMark());
			ps.setInt(5, question.getId());


			rows = ps.executeUpdate();
			System.out.println(rows + " row(s) updated in question.");
			return rows;

		} catch (SQLException e) {
			e.printStackTrace();
			return rows;
		}
	}

	public boolean Delete(Question question) {
	    String query = "DELETE FROM question WHERE question_id=?";
	    try (Connection conn = ConnectionURL.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query)) {

	        ps.setInt(1, question.getId());
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public List<Question> getAll() {
		List<Question> questionList = new ArrayList<>();
		String query = "SELECT * FROM question";

		try (Connection conn = ConnectionURL.getConnection(); 
		     PreparedStatement ps = conn.prepareStatement(query)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int questionId = rs.getInt("question_id");
				String text = rs.getString("text");
				String type = rs.getString("type");
				int quizId = rs.getInt("quiz_id");
				int mark = rs.getInt("mark");
				Question question1 = new Question(questionId, quizId, text, type, mark);

				questionList.add(question1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questionList;
	}
}