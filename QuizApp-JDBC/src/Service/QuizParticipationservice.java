package Service;

import java.util.List;
import Model.ParticipantAnswer;
import Model.QuizParticipation;
import MySQLConnection.ParticipantAnswerDaoimpl;
import MySQLConnection.QuizParticpationDaoimpl;

public class QuizParticipationservice {

    private QuizParticpationDaoimpl qpa = new QuizParticpationDaoimpl();

    public int save(QuizParticipation pa) {
        return qpa.save(pa);
    }

    public int update(QuizParticipation pa) {
        return qpa.Update(pa);
    }

    public boolean delete(QuizParticipation pa) {
        return qpa.Delete(pa);
    }

    public List<QuizParticipation> getAll() {
        return qpa.getAll();
    }
}
