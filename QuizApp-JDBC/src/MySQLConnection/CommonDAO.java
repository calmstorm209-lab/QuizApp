package MySQLConnection;

import java.util.List;



public interface CommonDAO<T>{
	int save(T object);
	int Update(T object);
	boolean Delete(T object);
	List<T> getAll();

}
