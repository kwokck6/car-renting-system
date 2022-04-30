package admin;
import query.QueryHandler;
import java.io.*;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Scanner;


public class Admin {
    private QueryHandler handler;
    private String[] data_files = {"user_category.txt", "user.txt", "car_category.txt", "car.txt", "rent.txt"};
    private String[] tables = {"user_category", "user", "car_category", "car", "produce", "copy", "rent"};
    private static String create_table_userCat = String.join("",
            "CREATE TABLE user_category(",
            "ucid INT(1) UNSIGNED NOT NULL,",
            "max INT(1) UNSIGNED NOT NULL,",
            "period INT(2) UNSIGNED NOT NULL,",
            "PRIMARY KEY (ucid))");
    private static String create_table_user = String.join("",
            "CREATE TABLE user(",
            "uid VARCHAR(12) NOT NULL,",
            "name VARCHAR(25) NOT NULL,",
            "age INT(2) UNSIGNED NOT NULL,",
            "occupation VARCHAR(20) NOT NULL,",
            "ucid INT(1) UNSIGNED NOT NULL,",
            "PRIMARY KEY (uid),",
            "FOREIGN KEY (ucid) REFERENCES user_category(ucid))");
    private static String create_table_carCat = String.join("",
            "CREATE TABLE car_category(",
            "ccid INT(1) UNSIGNED NOT NULL,",
            "ccname VARCHAR(20) NOT NULL,",
            "PRIMARY KEY (ccid))");
    private static String create_table_car = String.join("",
            "CREATE TABLE car(",
            "callnum VARCHAR(8) NOT NULL,",
            "name VARCHAR(10) NOT NULL,",
            "manufacture DATE NOT NULL,",
            "time_rent INT(2) UNSIGNED NOT NULL,",
            "ccid INT(1) UNSIGNED NOT NULL,",
            "PRIMARY KEY (callnum),",
            "FOREIGN KEY (ccid) REFERENCES car_category(ccid))");
    private static String create_table_produce = String.join("",
            "CREATE TABLE produce(",
            "cname VARCHAR(25) NOT NULL,",
            "callnum VARCHAR(8) NOT NULL,",
            "FOREIGN KEY (callnum) REFERENCES car(callnum),",
            "PRIMARY KEY (cname, callnum))");
    private static String create_table_copy = String.join("",
            "CREATE TABLE copy(",
            "callnum VARCHAR(8) NOT NULL,",
            "copynum INT(1) UNSIGNED NOT NULL,",
            "FOREIGN KEY (callnum) REFERENCES car(callnum),",
            "PRIMARY KEY (callnum, copynum))");
    private static String create_table_rent = String.join("",
            "CREATE TABLE rent(",
            "callnum VARCHAR(8) NOT NULL,",
            "copynum INT(1) UNSIGNED NOT NULL,",
            "uid VARCHAR(12) NOT NULL,",
            "checkout DATE NOT NULL,",
            "return_date DATE,",
            "FOREIGN KEY (uid) REFERENCES user(uid),",
            "FOREIGN KEY (callnum, copynum) REFERENCES copy(callnum, copynum),",
            "PRIMARY KEY (uid, callnum, copynum, checkout),",
            "CHECK (return_date >= checkout OR return_date is NULL))");
    public Admin(QueryHandler handler){
        this.handler = handler;
    }

    public int create_tables() throws SQLException{
        String[] init_statements = {
                create_table_userCat,
                create_table_user,
                create_table_carCat,
                create_table_car,
                create_table_produce,
                create_table_copy,
                create_table_rent
        };
        int result = 0;
        for (String s : init_statements){
            result = this.handler.updateDB(s);
        }
        return result;
    }

    public int delete_tables() throws SQLException {
        int result = 0;
        String delete_query = "DROP TABLE %s;";
        for (int counter = this.tables.length - 1; counter >= 0;counter--){
            String table = this.tables[counter];
            result = this.handler.updateDB(String.format(delete_query, table));
        }
        return result;
    }

    public int load_data(String fp) throws SQLException, IOException{
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path input_fp = Paths.get(fp);
        Path dir_fp;
        if (input_fp.isAbsolute()){
            dir_fp = input_fp;
        } else {
            dir_fp = Paths.get(currentPath.toString(), fp);
        }
        for (String f_name : this.data_files) {
            String data_fp = Paths.get(dir_fp.toString(), f_name).toString();
            File file = new File(data_fp);
            Scanner sc = new Scanner(file);
            String line;
            switch (f_name) {
                case "user_category.txt": {
                    String query_template = "INSERT INTO user_category VALUES(%d, %d, %d);";
                    String pattern = "^(\\d)[\\s\\t]*(\\d)[\\s\\t]*(\\d{1,2})$";
                    Pattern r = Pattern.compile(pattern);
                    while (sc.hasNextLine()){
                        line = sc.nextLine();
                        Matcher m = r.matcher(line);
                        if (m.find()) {
                            int ucid = Integer.parseInt(m.group(1));
                            int max = Integer.parseInt(m.group(2));
                            int period = Integer.parseInt(m.group(3));
                            String query = String.format(query_template, ucid, max, period);
                            try {
                                this.handler.updateDB(query);
                            } catch (SQLException e){
                                this.print_error(String.format("Skipping user category entry with ucid=%d due to %s", ucid, e.getMessage()));
                            }
                        } else {
                            throw new IllegalArgumentException(String.format("%s contains invalid row", f_name));
                        }
                    }
                    break;
                }
                case "user.txt": {
                    String query_template = "INSERT INTO user VALUES (\"%s\", \"%s\", %d, \"%s\", %d);";
                    String pattern = "^(.{12})[\\s\\t]*([^0-9]{1,25}\\s)[\\s\\t]*(\\d{2})[\\s\\t]*([^0-9]{1,20}\\s)[\\s\\t]*(\\d)$";
                    Pattern r = Pattern.compile(pattern);
                    while (sc.hasNextLine()){
                        line = sc.nextLine();
                        Matcher m = r.matcher(line);
                        if (m.find()) {
                            String uid = m.group(1);
                            String name = m.group(2).strip();
                            int age = Integer.parseInt(m.group(3));
                            String occupation = m.group(4).strip();
                            int ucid = Integer.parseInt(m.group(5));
                            String query = String.format(query_template, uid, name, age, occupation, ucid);
                            try {
                                this.handler.updateDB(query);
                            } catch (SQLException e){
                                this.print_error(String.format("Skipping user entry with uid=%s due to %s", uid, e.getMessage()));
                            }
                        } else {
                            throw new IllegalArgumentException(String.format("%s contains invalid row", f_name));
                        }
                    }
                    break;
                }
                case "car_category.txt": {
                    String query_template = "INSERT INTO car_category VALUES(%d, \"%s\");";
                    String pattern = "^(\\d)[\\s\\t]*([^0-9]{1,20})$";
                    Pattern r = Pattern.compile(pattern);
                    while (sc.hasNextLine()){
                        line = sc.nextLine();
                        Matcher m = r.matcher(line);
                        if (m.find()) {
                            int ccid = Integer.parseInt(m.group(1));
                            String ccname = m.group(2).strip();
                            String query = String.format(query_template, ccid, ccname);
                            try {
                                this.handler.updateDB(query);
                            } catch (SQLException e){
                                this.print_error(String.format("Skipping car category entry with ccid=%d due to %s", ccid, e.getMessage()));
                            }
                        } else {
                            throw new IllegalArgumentException(String.format("%s contains invalid row", f_name));
                        }
                    }
                    break;
                }
                case "car.txt": {
                    String query_template_car = "INSERT INTO car VALUES(\"%s\", \"%s\", \"%s\", %d, %d);";
                    String query_template_produce = "INSERT INTO produce VALUES(\"%s\", \"%s\");";
                    String query_template_copy = "INSERT INTO copy VALUES(\"%s\", %d);";
                    String pattern = "^(.{8})[\\s\\t]*(\\d)[\\s\\t]*(.{1,10}\\s)[\\s\\t]*(.{1,25}\\s)[\\s\\t]*([\\d]{4}-[\\d]{2}-[\\d]{2})[\\s\\t]*(\\d{2})[\\s\\t]*(\\d)$";
                    Pattern r = Pattern.compile(pattern);
                    while (sc.hasNextLine()){
                        line = sc.nextLine();
                        Matcher m = r.matcher(line);
                        if (m.find()) {
                            String callnum = m.group(1);
                            int copynum = Integer.parseInt(m.group(2));
                            String name = m.group(3).strip();
                            String cname = m.group(4).strip();
                            String manufacture = m.group(5);
                            int time_rent = Integer.parseInt(m.group(6));
                            int ccid = Integer.parseInt(m.group(7));
                            String query_car = String.format(query_template_car, callnum, name, manufacture, time_rent, ccid);
                            try {
                                this.handler.updateDB(query_car);
                            } catch (SQLException e){
                                this.print_error(String.format("Skipping car entry with callnum=%s copynum=%d due to %s", callnum, copynum, e.getMessage()));
                            }
                            String query_produce = String.format(query_template_produce, cname, callnum);
                            try {
                                this.handler.updateDB(query_produce);
                            } catch (SQLException e){
                                this.print_error(String.format("Skipping produce entry with callnum=%s due to %s", callnum, e.getMessage()));
                            }
                            for (int cnt = 1; cnt <= copynum; cnt++){
                                String query_copy = String.format(query_template_copy, callnum, cnt);
                                try {
                                    this.handler.updateDB(query_copy);
                                } catch (SQLException e){
                                    this.print_error(String.format("Skipping copy entry with callnum=%s copynum=%d due to %s", callnum, copynum, e.getMessage()));
                                }
                            }
                        } else {
                            throw new IllegalArgumentException(String.format("%s contains invalid row", f_name));
                        }
                    }
                    break;
                }
                case "rent.txt": {
                    String query_template = "INSERT INTO rent VALUES(\"%s\", %d, \"%s\", \"%s\", %s);";
                    String pattern = "^(.{8})[\\s\\t]*(\\d)[\\s\\t]*(.{12})[\\s\\t]*([\\d]{4}-[\\d]{2}-[\\d]{2})[\\s\\t]*([\\d]{4}-[\\d]{2}-[\\d]{2}|NULL)$";
                    Pattern r = Pattern.compile(pattern);
                    while (sc.hasNextLine()){
                        line = sc.nextLine();
                        Matcher m = r.matcher(line);
                        if (m.find()) {
                            String callnum = m.group(1);
                            int copynum = Integer.parseInt(m.group(2));
                            String uid = m.group(3).strip();
                            String checkout = m.group(4).strip();
                            String returned = m.group(5).strip();
                            if (!Objects.equals(returned, "NULL")){
                                returned = String.format("\"%s\"", returned);
                            }
                            String query = String.format(query_template, callnum, copynum, uid, checkout, returned);
                            try {
                                this.handler.updateDB(query);
                            } catch (SQLException e){
                                this.print_error(String.format("Skipping rent entry with callnum=%s, copynum=%d, checkout=%s due to %s", callnum, copynum, checkout, e.getMessage()));
                            }
                        } else {
                            throw new IllegalArgumentException(String.format("%s contains invalid row", f_name));
                        }
                    }
                    break;
                }
            }
        }
        return 0;
    }

    public HashMap<String, Integer> count_records() throws SQLException {
        int cnt = 0;
        HashMap<String, Integer> number_of_records = new HashMap<>();
        String query_template = "SELECT COUNT(*) FROM %s;";
        for (String table : this.tables){
            ResultSet rs = this.handler.queryDB(String.format(query_template, table));
            rs.next();
            cnt = rs.getInt(1);
            number_of_records.put(table, cnt);
        }
        return number_of_records;
    }

    protected void print_error(String exception_msg){
        System.out.println(String.format("[%sError%s]: %s", "\u001B[31m", "\u001B[0m", exception_msg));
    }

}
