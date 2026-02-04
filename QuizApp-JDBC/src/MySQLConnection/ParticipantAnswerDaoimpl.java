package MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.ParticipantAnswer;

public class ParticipantAnswerDaoimpl implements participantAnswerDao {

    @Override
    public int save(ParticipantAnswer pat) {
        String query = "INSERT INTO participantAnswer (quizId, questionId, selectedOptionId, answer_text, userId) VALUES (?, ?, ?, ?, ?)";
        int rows = 0;

        try (Connection conn = ConnectionURL.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, pat.getQuizId());
            ps.setInt(2, pat.getQuestionId());

            if (pat.getSelectedOptionId() == 0) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, pat.getSelectedOptionId());
            }

            ps.setString(4, pat.getText());
            ps.setInt(5, pat.getUserId());

            rows = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

   
    @Override
    public boolean Delete(ParticipantAnswer pat) {
        String query = "DELETE FROM participantAnswer WHERE quizId = ? AND questionId = ?";

        try (Connection conn = ConnectionURL.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, pat.getQuizId());
            ps.setInt(2, pat.getQuestionId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ParticipantAnswer> getAll() {
        List<ParticipantAnswer> list = new ArrayList<>();
        String query = "SELECT userId, quizId, questionId, answer_text, GROUP_CONCAT(selectedOptionId) AS optIds FROM participantAnswer GROUP BY userId, quizId, questionId, answer_text";

        try (Connection conn = ConnectionURL.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                int quizId = rs.getInt("quizId");
                int questionId = rs.getInt("questionId");
                String answerText = rs.getString("answer_text");
                String optIdsStr = rs.getString("optIds");

                String[] selectedOptions = optIdsStr != null ? optIdsStr.split(",") : new String[0];
                int selectedOptionId = selectedOptions.length > 0 ? Integer.parseInt(selectedOptions[0]) : 0;

                ParticipantAnswer pat = new ParticipantAnswer(
                        userId,
                        quizId,
                        questionId,
                        selectedOptions,
                        selectedOptionId,
                        answerText
                );

                list.add(pat);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


	@Override
	public int Update(ParticipantAnswer object) {
		// TODO Auto-generated method stub
		return 0;
	}
}
