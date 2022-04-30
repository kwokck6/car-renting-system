package query;
import java.sql.*;

public class QueryHandler {
    private Connection con;

    public QueryHandler(){
        this.con = LoadDriver.main();
    }

    public int updateDB(String query) throws SQLException{
        Statement stmt = this.con.createStatement();
        int result = stmt.executeUpdate(query);
        return result;
    }

    public int updateDB_pstmt(PreparedStatement pstmt) throws SQLException{
        int result = pstmt.executeUpdate();
        return result;
    }

    public ResultSet queryDB(String query) throws SQLException{
        Statement stmt = this.con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public ResultSet queryDB_pstmt(PreparedStatement pstmt) throws SQLException{
            ResultSet rs = pstmt.executeQuery();
            return rs;
    }

    public PreparedStatement get_pstmt(String psql){
        try {
            PreparedStatement pstmt = this.con.prepareStatement(psql);
            return pstmt;
        } catch (SQLException e){
            return null;
        }
    }


}
