package user;

import java.sql.*;
import query.LoadDriver;

public class User {
    public static void searchCars(String criterion, String keyword) throws SQLException {
        String psql_view = "CREATE OR REPLACE VIEW count_view AS SELECT callnum, COUNT(*) as count_copy FROM copy GROUP BY callnum";
        String psql_query = "SELECT car.callnum, car.name, ccname, cname, " +
                "count_view.count_copy - IFNULL(SUM(!ISNULL(checkout) & ISNULL(return_date)), 0) as copy_count " +
                "FROM car natural join car_category natural join produce natural join copy natural join count_view " +
                "LEFT JOIN rent ON copy.callnum = rent.callnum AND copy.copynum = rent.copynum " +
                "WHERE car.callnum = ? OR car.name REGEXP ? OR cname REGEXP ? GROUP BY car.callnum ORDER BY car.callnum";
        String callnum = null;
        String name = null;
        String cname = null;
        switch (criterion) {
            case "1": {
                callnum = keyword;
                break;
            }
            case "2": {
                name = keyword;
                break;
            }
            case "3": {
                cname = keyword;
                break;
            }
            default: {
                System.out.printf("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: %s is not a valid search mode.%n", criterion);
                return;
            }
        }
        Connection con = LoadDriver.main();
        PreparedStatement pstmt_view = con.prepareStatement(psql_view);
        PreparedStatement pstmt_query = con.prepareStatement(psql_query);
        pstmt_query.setString(1, callnum);
        pstmt_query.setString(2, name);
        pstmt_query.setString(3, cname);
        pstmt_view.executeUpdate();
        ResultSet result = pstmt_query.executeQuery();
        if (!result.isBeforeFirst())
            System.out.println("No records found.");
        else {
            System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
            while (result.next()) {
                callnum = result.getString("callnum");
                name = result.getString("name");
                String ccname = result.getString("ccname");
                cname = result.getString("cname");
                int count = result.getInt("copy_count");
                System.out.println("|" + callnum + "|" + name + "|" + ccname + "|" + cname + "|" + count + "|");
            }
            result.close();
            System.out.println("End of Query");
        }
    }

    public static void showRentingRecords(String userID) throws SQLException {
        String psql = "SELECT *, name, cname FROM rent NATURAL JOIN car NATURAL JOIN produce WHERE rent.uid = ? order by checkout desc";
        Connection con = LoadDriver.main();
        PreparedStatement pstmt = con.prepareStatement(psql);
        pstmt.setString(1, userID);

        ResultSet result = pstmt.executeQuery();
        if (!result.isBeforeFirst())
            System.out.println("No records found.");
        else {
            System.out.println("|CallNum|CopyNum|Name|Company|Check-out|Returned?|");
            while (result.next()) {
                String callnum = result.getString("callnum");
                String copynum = result.getString("copynum");
                String name = result.getString("name");
                String company = result.getString("cname");
                String checkout = result.getString("checkout");
                String returned = "";
                if (result.getString("return_date") != null)
                    returned = "Yes";
                else
                    returned = "No";
                System.out.println("|" + callnum + "|" + copynum + "|" + name + "|" + company + "|" + checkout + "|" + returned + "|");
            }
            result.close();
            System.out.println("End of Query");
        }
    }
}
