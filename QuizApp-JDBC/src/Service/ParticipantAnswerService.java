package Service;

import java.util.List;
import Model.ParticipantAnswer;
import MySQLConnection.ParticipantAnswerDaoimpl;

public class ParticipantAnswerService {

    private ParticipantAnswerDaoimpl dao = new ParticipantAnswerDaoimpl();

    public int save(ParticipantAnswer pa) {
        return dao.save(pa);
    }

    public int update(ParticipantAnswer pa) {
        return dao.Update(pa);
    }

    public boolean delete(ParticipantAnswer pa) {
        return dao.Delete(pa);
    }

    public List<ParticipantAnswer> getAll() {
        return dao.getAll();
    }
}
