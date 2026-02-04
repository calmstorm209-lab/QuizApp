package Service;

import java.util.List;
import Model.Option;
import MySQLConnection.OptionDaoImpl;

public class OptionService {

    private OptionDaoImpl dao = new OptionDaoImpl();

    public int save(Option op) {
        return dao.save(op);
    }

    public int update(Option op) {
        return dao.Update(op);
    }

    public boolean delete(Option op) {
        return dao.Delete(op);
    }

    public List<Option> getAll() {
        return dao.getAll();
    }
}
