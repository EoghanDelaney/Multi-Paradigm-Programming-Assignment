import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.*;
import java.util.*;

// The below code was modified from the below link
// https://www.daniweb.com/programming/software-development/threads/13786/java-code-for-menu-selection-using-scanner-class

public class Menu {
  public void display_menu() {
    System.out.println ( "************\nPlease choose an option below:\n     1: Customer dose not have enough money.\n     2: Customer is looking for to much of one or more products\n     3: Customer is just right\n     4: Manually enter your order\n     0: To break\n" );
    //System.out.print ( "Selection: " );
  }
  
  public Menu() {
    Scanner in = new Scanner ( System.in );
    ShopA shop = new ShopA("../stock.csv");
    display_menu();
    switch ( in.nextInt() ) {
      case 1:
		
        Customer custA = new Customer("../customerB.csv");
        shop.processOrder(custA);
        break;
      case 2:
        Customer custB = new Customer("../customerC.csv");
        shop.processOrder(custB);
        break;
      case 3:
        Customer custC = new Customer("../customerA.csv");
        //System.out.println(custC);
        shop.processOrder(custC);
        break;      
      case 4:
        AskOrder james = new AskOrder("");
        shop.processOrderAsk(james);
        break;
      default:
        System.err.println ( "Bye Bye!" );
        break;
    }
  }
  
  public static void main ( String[] args ) {
    new Menu();
  }
}