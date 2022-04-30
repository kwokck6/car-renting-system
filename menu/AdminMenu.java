package menu;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import query.QueryHandler;
import admin.Admin;

public class AdminMenu extends menu.MainMenu{
    private Admin admin;

    public AdminMenu(MainMenu parent, QueryHandler handler){
        this.parent = parent;
        this.menu_template = String.join(this.newLine,
                "-----Operations for administrator menu-----",
                "What kind of operation would you like to perform?",
                "1. Create all tables",
                "2. Delete all tables",
                "3. Load from datafile",
                "4. Show number of records in each table",
                "5. Return to the main menu");
        this.admin = new Admin(handler);
    }

    @Override
    protected MainMenu action(String choice) {
        switch (choice) {
            case "1": {
                try {
                    this.admin.create_tables();
                    System.out.println("Processing...Done. Database is initialized.");
                } catch (SQLException e) {
                    this.print_error(e.getMessage());
                }
                return this;
            }
            case "2": {
                try {
                    this.admin.delete_tables();
                    System.out.println("Processing...Done. Database is removed.");
                } catch (SQLException e) {
                    this.print_error(e.getMessage());
                }
                return this;
            }
            case "3": {
                System.out.print("Type in the Source Data Folder Path: ");
                String fp = scanner.nextLine();
                try {
                    this.admin.load_data(fp);
                    System.out.println("Processing...Done. Validated data is inputted to the database.");
                } catch (SQLException e) {
                    this.print_error(e.getMessage());
                } catch (IOException e){
                    this.print_error(e.getMessage());
                }
                return this;
            }
            case "4": {
                try {
                    HashMap<String, Integer> number_of_records = this.admin.count_records();
                    System.out.println("Number of records in each table:");
                    for (String key : number_of_records.keySet()) {
                        Integer cnt = number_of_records.get(key);
                        System.out.println(String.format("%s: %d", key, cnt));
                    }
                } catch (SQLException e){
                    this.print_error(e.getMessage());
                }
                return this;
            }
            case "5": {
                return this.parent;
            }
            default: {
                System.out.println("Please enter valid inputs.");
                return this;
            }
        }
    }
}
