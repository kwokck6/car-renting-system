package menu;
import manager.Manager;

import java.sql.SQLException;

public class ManagerMenu extends menu.MainMenu{
    public ManagerMenu(MainMenu parent){
        this.parent = parent;
        this.menu_template = String.join(this.newLine,
                "-----Operations for manager menu-----",
                "What kind of operation would you like to perform?",
                "1. Car Renting",
                "2. Car Returning",
                "3. List all un-returned car copies which are checked-out within a period",
                "4. Return to the main menu");
    }

    @Override
    protected MainMenu action(String choice) {
        switch(choice){
            case "1": {
                System.out.print("Enter The User ID: ");
                String uid = this.scanner.nextLine();
                System.out.print("Enter The Call Number: ");
                String callnum = this.scanner.nextLine();
                System.out.print("Enter The Copy Number: ");
                String copynum_string = this.scanner.nextLine();
                try{
                    int copynum = Integer.parseInt(copynum_string);
                    Manager.rentCarCopy(uid, callnum, copynum);
                }
                catch(NumberFormatException e){
                    System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: Invalid Input of copy number");
                }
                catch(SQLException e){
                    System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: " + e.getMessage());
                }
                return this;
            }
            case "2": {
                System.out.print("Enter The User ID: ");
                String uid = this.scanner.nextLine();
                System.out.print("Enter The Call Number: ");
                String callnum = this.scanner.nextLine();
                System.out.print("Enter The Copy Number: ");
                String copynum_string = this.scanner.nextLine();
                try{
                    int copynum = Integer.parseInt(copynum_string);
                    Manager.returnCar(uid, callnum, copynum);
                }
                catch(NumberFormatException e){
                    System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: Invalid Input of copy number");
                }
                catch(SQLException e){
                    System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: " + e.getMessage());
                }
                return this;
            }
            case "3": {
                System.out.print("Type in the " + this.INFO_CLR + "starting" + this.NORMAL_CLR +
                        " date [dd/mm/yyyy]: ");
                String start_date = this.scanner.nextLine();
                System.out.print("Type in the ending date [dd/mm/yyyy]: ");
                String end_date = this.scanner.nextLine();
                try{
                    Manager.list(start_date, end_date);
                }
                catch(SQLException e){
                    System.out.println("[" + "\u001B[31m" + "Error" + "\u001B[0m" + "]: " + e.getMessage());
                }
                return this;
            }
            case "4" : {
                return this.parent;
            }
            default: {
                System.out.println("Please enter valid inputs.");
                return this;
            }
        }
    }
}
