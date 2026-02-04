package Service;

import java.util.List;
import Model.Question;
import MySQLConnection.Questiondaoimpl;

public class QuestionService {

    private Questiondaoimpl dao = new Questiondaoimpl();

    public int save(Question q) {
        return dao.save(q);
    }

    public int update(Question q) {
        return dao.Update(q);
    }

    public boolean delete(Question q) {
        return dao.Delete(q);
    }

    public List<Question> getAll() {
        return dao.getAll();
    }
}
