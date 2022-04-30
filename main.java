import menu.MainMenu;
import java.util.concurrent.TimeUnit;


public class main {
    public static void main(String args[]){
        MainMenu menu = new MainMenu();
        System.out.println("Welcome to Car Renting System!");
        while (true){
            System.out.println();
            menu = menu.run();
            if (menu == null){
                break;
            }
        }
    }
}
