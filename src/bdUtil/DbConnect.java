package bdUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
public static Connection openConnection() {
	Connection conn=null;

    String dbURL = "jdbc:mysql://localhost:3306/eco-jb";
    String username = "root";
    String password = "";
    
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
conn=DriverManager.getConnection(dbURL, username, password);
    } catch (SQLException ex) {
        ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
    }
    return conn;
}
}

