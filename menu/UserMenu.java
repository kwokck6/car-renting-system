package menu;
import java.sql.*;
import user.User;

public class UserMenu extends menu.MainMenu {
    private final String search_criterion_template;
    public UserMenu(MainMenu parent){
        this.parent = parent;
        this.menu_template = String.join(this.newLine,
                "-----Operations for user menu-----",
                "What kind of operation would you like to perform?",
                "1. Search for Cars",
                "2. Show loan record of a user",
                "3. Return to the main menu");
        this.search_criterion_template = String.join(this.newLine,
                "Choose the Search criterion:",
                "1. call number",
                "2. name",
                "3. company");
    }

    @Override
    protected MainMenu action(String choice) {
        switch(choice) {
            case "1": {
                System.out.println(this.search_criterion_template);
                System.out.print("Choose the search criterion: ");
                String criterion = this.scanner.nextLine();
                System.out.print("Type in the Search Keyword: ");
                String keyword = this.scanner.nextLine();
                try {
                    User.searchCars(criterion, keyword);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return this;
            }
            case "2": {
                System.out.print("Enter The cuser ID: ");
                String userID = this.scanner.nextLine();
                System.out.println("Loan Record:");
                try {
                    User.showRentingRecords(userID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return this;
            }
            case "3": {
                return this.parent;
            }
            default: {
                System.out.println("Please enter valid inputs.");
                return this;
            }
        }
    }
}
