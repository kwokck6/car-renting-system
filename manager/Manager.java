package manager;

import java.sql.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import query.LoadDriver;

public class Manager{
    public static void rentCarCopy(String uid, String callnum, int copynum) throws SQLException {
        int car_unavailable = 0;
        int num_rent = 0;
        int car_exist = 0;
        int max = 0;
        int period = 0;
        Connection con = LoadDriver.main();
        Statement stmt = con.createStatement();
        ResultSet result1 = stmt.executeQuery("select callnum from rent where callnum = \"" + callnum + "\" AND copynum = \"" + copynum + "\" AND return_date IS NULL");
        while (result1.next()) {
            car_unavailable = 1;
        }
        result1.close();
        if (car_unavailable == 1) {
            System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: The car was already rent.");
        } else {
            ResultSet result2 = stmt.executeQuery("select max, period from user,user_category where user.ucid = user_category.ucid AND user.uid = \"" + uid + "\"");
            while (result2.next()) {
                max = result2.getInt("max");
                period = result2.getInt("period");
            }
            result2.close();
            ResultSet result3 = stmt.executeQuery("select callnum, copynum from rent where uid = \"" + uid + "\" AND return_date IS NULL ");
            while (result3.next()) {
                num_rent += 1;
            }
            result3.close();
            ResultSet result4 = stmt.executeQuery("select callnum, copynum from copy where callnum = \"" + callnum + "\" AND copynum = \"" + copynum + "\"");
            while (result4.next()) {
                car_exist = 1;
            }
            result4.close();
            if (max - num_rent < 1 && max != 0 && car_exist != 0) {
                System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: Car rent exceed limit.");
            } else if (max != 0 && car_exist != 0) {
                DateTimeFormatter current_time = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();
                stmt.executeUpdate("insert into rent values (\"" + callnum + "\"," + copynum + ",\"" + uid + "\"," + "str_to_date(\"" + current_time.format(now) + "\", \"%d/%m/%Y\"), null)");
                System.out.println("car renting performed " + "\u001B[32m" + "successfully" + "\u001B[0m" + ".");
            } else if (max == 0) {
                System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: User ID not exist.");
            } else if (car_exist == 0) {
                System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: No Matching car copy found.");
            }
        }
    }

    public static void returnCar(String uid, String callnum, int copynum) throws SQLException{
        int car_record_exist = 0;
        int time_rent = 0;
        Connection con = LoadDriver.main();
        Statement stmt = con.createStatement();
        ResultSet result1 = stmt.executeQuery("select callnum from rent where callnum = \"" + callnum + "\" AND copynum = \"" + copynum + "\" AND uid = \"" + uid + "\" AND return_date IS NULL");
        while(result1.next()) {
            car_record_exist = 1;
        }
        result1.close();
        if(car_record_exist == 1){
            DateTimeFormatter current_time= DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime now = LocalDateTime.now();
            stmt.executeUpdate("update rent set return_date = str_to_date(\"" + current_time.format(now) + "\", \"%d/%m/%Y\") where callnum = \"" + callnum + "\"AND copynum = " + copynum + " AND uid = \"" + uid + "\"");
            ResultSet result2 = stmt.executeQuery("select time_rent from car where callnum = \"" + callnum + "\"");
            while(result2.next()){
                time_rent = result2.getInt("time_rent");
            }
            time_rent += 1;
            stmt.executeUpdate("update car set time_rent = \"" + time_rent + "\" where callnum = \"" + callnum + "\"");
            System.out.println("Car returning performed " + "\u001B[32m" + "successfully" + "\u001B[0m" + ".");
        }else{
            System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: " + "\u001B[31m" + "No" + "\u001B[0m" + " Matching car record is found.");
        }
    }

    public static void list(String start_date, String end_date) throws SQLException {
        int copy = 0;
        int found = 0;
        String uid = "";
        String callnum = "";
        String checkout = "";
        int copynum = 0;
        Connection con = LoadDriver.main();
        Statement stmt = con.createStatement();
        ResultSet result1=stmt.executeQuery("select uid, callnum, copynum, checkout from rent where checkout >= str_to_date(\"" + start_date + "\", \"%d/%m/%Y\") AND checkout <= str_to_date(\"" + end_date + "\", \"%d/%m/%Y\")"+ " AND return_date is NULL ORDER BY checkout DESC");
        while(result1.next()) {
            found += 1;
            if(found == 1){
                System.out.println("List of UnReturned Cars:");
                System.out.println("|UID|CallNum|CopyNum|Checkout|");
            }
            uid = result1.getString("uid");
            callnum = result1.getString("callnum");
            copynum = result1.getInt("copynum");
            checkout = result1.getString("checkout");
            System.out.println("|" + uid + "|" + callnum + "|" + copynum + "|" + checkout + "|" );
        }
        result1.close();
        if(found > 0){
            System.out.println("End of Query");
        }
        if(found == 0){
            System.out.println("No record Found");
        }
    }
}
