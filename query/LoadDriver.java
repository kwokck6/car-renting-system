package query;

import java.sql.*;

public class LoadDriver {
    public static Connection main() {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db2";
        String dbUsername = "Group2";
        String dbPassword = "Jason&Oscar&GA";
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
