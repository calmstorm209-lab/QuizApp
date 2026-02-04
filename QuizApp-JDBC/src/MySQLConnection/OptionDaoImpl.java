package MySQLConnection;

import java.sql.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Option;

public class OptionDaoImpl implements Optiondao {

	@Override
	public int save(Option option) {
	    int id = -1;
	    String query = "INSERT INTO options (text, is_correct, question_id) VALUES (?, ?, ?)";

	    try (Connection conn = ConnectionURL.getConnection(); 
	         PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setString(1, option.getText());
	        ps.setBoolean(2, option.getCorrect());
	        ps.setInt(3, option.getQuestionId());

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
	public int Update(Option option) {
		String query = "UPDATE options SET text = ?, is_correct = ?, question_id = ? WHERE option_id = ?";
		int rows = 0;

		try (Connection conn = ConnectionURL.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, option.getText());
			ps.setBoolean(2, option.getCorrect());
			ps.setInt(3, option.getQuestionId());
			ps.setInt(4, option.getOptionId());

			rows = ps.executeUpdate();
			System.out.println(rows + " row(s) updated in options.");
			return rows;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public boolean Delete(Option option) {
		String query = "DELETE FROM options WHERE option_id = ?";
		int rows = 0;

		try (Connection conn = ConnectionURL.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setInt(1, option.getOptionId());

			rows = ps.executeUpdate();
			System.out.println(rows + " row(s) deleted from options.");
			return rows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Option> getAll() {
		List<Option> optList = new ArrayList<>();
		String query = "SELECT * FROM options";

		try (Connection conn = ConnectionURL.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				int optionId = rs.getInt("option_id");
				String text = rs.getString("text");
				boolean isCorrect = rs.getBoolean("is_correct");
				int questionId = rs.getInt("question_id");
				Option opt = new Option(questionId, text, isCorrect, optionId);

				optList.add(opt);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return optList;
	}
}
