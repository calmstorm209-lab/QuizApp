package MySQLConnection;

import java.sql.*;

public class ConnectionURL {
	 private static final String URL = "jdbc:mysql://localhost:3306/quizapp";
	    private static final String USER = "root";
	    private static final String PASSWORD = "";
	    
	    
	    public static Connection getConnection() throws SQLException {
	    	Connection conn=DriverManager.getConnection(URL, USER, PASSWORD);
			return conn;
	    
}
}
