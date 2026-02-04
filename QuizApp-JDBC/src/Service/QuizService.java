package Service;

import java.util.List;
import Model.Quiz;
import MySQLConnection.Quizdaoimpl;

public class QuizService {

    private Quizdaoimpl dao = new Quizdaoimpl();

    public int save(Quiz quiz) {
        return dao.save(quiz);
    }

    public int update(Quiz quiz) {
        return dao.Update(quiz);
    }

    public boolean delete(Quiz quiz) {
        return dao.Delete(quiz);
    }

    public List<Quiz> getAll() {
        return dao.getAll();
    }
}
