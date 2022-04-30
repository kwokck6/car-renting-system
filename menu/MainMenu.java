package menu;
import java.util.Scanner;
import query.QueryHandler;


public class MainMenu {
    String newLine = System.getProperty("line.separator");
    private AdminMenu admin_menu;
    private menu.UserMenu user_menu;
    private ManagerMenu manager_menu;
    private QueryHandler handler;
    public String menu_template;
    public MainMenu parent;
    public Scanner scanner;
    public final String SUCCESS_CLR = "\u001B[32m";
    public final String ERROR_CLR = "\u001B[31m";
    public final String INFO_CLR = "\u001B[36m";
    public final String NORMAL_CLR = "\u001B[0m";

    public MainMenu(){
        if (this.getClass() == MainMenu.class){
            this.handler = new QueryHandler();
            this.admin_menu = new AdminMenu(this, this.handler);
            this.user_menu = new menu.UserMenu(this);
            this.manager_menu = new ManagerMenu(this);
        }
        this.menu_template = String.join(this.newLine,
                "-----Main menu-----",
                "What kinds of operations would you like to perform?",
                "1. Operations for Administrator",
                "2. Operations for User",
                "3. Operations for Manager",
                "4. Exit this program");
        this.parent = null;
        this.scanner = new Scanner(System.in);
    }

    public MainMenu run(){
        String choice;
        System.out.println(this.menu_template);
        System.out.print("Enter Your Choice: ");
        choice = this.scanner.nextLine();
        return this.action(choice);
    }

    protected MainMenu action(String choice){
        switch (choice) {
            case "1": {
                return this.admin_menu;
            }
            case "2": {
                return this.user_menu;
            }
            case "3": {
                return this.manager_menu;
            }
            case "4": {
                return this.parent;
            }
            default: {
                System.out.println("Please enter valid inputs.");
                return this;
            }
        }
    }

    protected void print_error(String exception_msg){
        System.out.println(String.format("[%sError%s]: %s", this.ERROR_CLR, this.NORMAL_CLR, exception_msg));
    }

    protected String set_info_clr(String msg){
        return String.format("%s%s%s", this.INFO_CLR, msg, this.NORMAL_CLR);
    }

    protected String set_success_clr(String msg){
        return String.format("%s%s%s", this.SUCCESS_CLR, msg, this.NORMAL_CLR);
    }
}

