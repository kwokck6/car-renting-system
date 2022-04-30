package query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestLoadDriver {
    public static Connection main() {
        String dbAddress = "jdbc:mysql://localhost:3306/csci3170?useSSL=False";
        String dbUsername = "root";
        String dbPassword = "123456Aa";
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return con;
    }
}
