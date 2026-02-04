package Service;

import java.util.List;
import Model.User;
import MySQLConnection.Userdaoimpl;

public class UserService {

    private Userdaoimpl dao = new Userdaoimpl();

    public int save(User user) {
        return dao.save(user);
    }

    public int update(User user) {
        return dao.Update(user);
    }

    public boolean delete(User user) {
        return dao.Delete(user);
    }

    public List<User> getAll() {
        return dao.getAll();
    }
}
