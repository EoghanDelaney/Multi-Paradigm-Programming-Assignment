import java.util.Scanner;  
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AskOrder {
    private String name;
	private double budget;
	private ArrayList<ProductStock> shoppingList;

    private String ProdName;
    private int ProdQty;

    

    public AskOrder(String test) {

        shoppingList = new ArrayList<>();
        
        Scanner check = new Scanner(System.in);  
        //Scanner budget = new Scanner(System.in);
        // number = new Scanner(System.in);
        
        System.out.println("Whats your name?");
        String CustName = check.nextLine(); 
        
        System.out.println("What is your budget?");
        double CustBudget = check.nextDouble(); 
        
        System.out.println("How many items on your list?");
        int CustNumber = check.nextInt(); 

        name = CustName;
        budget = CustBudget;

        //System.out.println("Name is: " + CustName + CustBudget);
        for (int i = 0; i < CustNumber; i++) {

            Scanner podcheck = new Scanner(System.in);

            System.out.println("What would you like?");
            ProdName = podcheck.nextLine(); 
            
            System.out.println("How many would you like?");
            ProdQty = podcheck.nextInt(); 
            

            //System.out.println(i);
            Product p = new Product(ProdName, 0);
		    DiscountedProductStock s = new DiscountedProductStock(p, ProdQty, 2, 0.1);
            shoppingList.add(s);
            //System.out.println("HERE IS THE SHOPPING LIST: " + shoppingList);
        }
    //System.out.println(AskOrder);
    }
    public String getName() {
		return name;
	}


	public double getBudget() {
		return budget;
	}


	public ArrayList<ProductStock> getShoppingList() {
		return shoppingList;
	}

	@Override
	public String toString() {
		String ret = "Customer [name=" + name + ", budget=" + budget + ", shoppingList=\n";
		for (ProductStock productStock : shoppingList) {
			ret+= productStock.getName() + " Quantity: " + productStock.getQuantity() + "\n";
		}
		return ret + "]";
	}



    public static void main(String[] args) {
		AskOrder james = new AskOrder("");
	}
}