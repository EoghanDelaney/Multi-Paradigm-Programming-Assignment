import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.*;

public class ShopA {

	private double cash;
	private ArrayList<ProductStock> stock;

	public ShopA(String fileName) {
		stock = new ArrayList<>();
		List<String> lines = Collections.emptyList();
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
			cash = Double.parseDouble(lines.get(0));
			// i am removing at index 0 as it is the only one treated differently
			lines.remove(0);
			for (String line : lines) {
				String[] arr = line.split(",");
				String name = arr[0];
				double price = Double.parseDouble(arr[1]);
				int quantity = Integer.parseInt(arr[2].trim());
				Product p = new Product(name, price);
				ProductStock s = new ProductStock(p, quantity);
				stock.add(s);
			}
		}

		catch (IOException e) {
			System.out.println("The program could not find the CSV file with the shop information in it");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private ProductStock find(String name){
		for(ProductStock ps : stock){
			if (name.equals(ps.getName())){
				return ps;
			}
		}
		return null;
	}
	
	public void processOrder(Customer c){
		double costToCustomer = 0;
		System.out.println("\u20ac");
		System.out.println("Welcome to the shop " + c.getName());
		System.out.println("Mister money bags! \u20ac" + c.getBudget() + " in your pocket!");
		System.out.println("Lets see whats on your list?");
		// look through the customer order
		for(ProductStock ps : c.getShoppingList()){
			ProductStock shopPS = find(ps.getName());
			// retreive the unit price
			double unitPrice = shopPS.getUnitPrice();
			// set the price on the product held by customer
			ps.getProduct().setPrice(unitPrice);
			// this way we can use the ps method to calc cost;

			if (ps.getQuantity()>shopPS.getQuantity()){
				System.out.println("Sorry, we dont have enough "+ ps.getName() +",You want "+ps.getQuantity()+ " we only have "+ shopPS.getQuantity()+", all or nothing!\n");
			} else{
				costToCustomer += ps.getCost();
				System.out.println("NAME OF PRODUCT " + ps.getName() + " PRICE \u20ac" + ps.getUnitPrice() + " QUANTITY " + ps.getQuantity());
				System.out.println("Amount \u20ac" + ps.getCost() + " SHOP has " + shopPS.getQuantity());
			}
			

		}
		System.out.println("Customer Total: \u20ac" + costToCustomer);
	
		if (costToCustomer ==0){
			System.out.println("We have nothing you wanted! What a waist of time!\n");
		} else if(costToCustomer<c.getBudget()){
			System.out.println("Looks good\n");
			System.out.println("You bill total is " + costToCustomer + ", how much did you say you have again? " + c.getBudget() + "\n");
			double revShopTotal = cash + costToCustomer;
			double revCustTotal = c.getBudget() - costToCustomer;
			System.out.println("Shop flaot is now " + revShopTotal+", while " + c.getName() + " you now have " + revCustTotal + "\n");
		} else if(costToCustomer>c.getBudget()){
			System.out.println("Your bill total is "+ costToCustomer +", how much did you have again? €"+ c.getBudget() +" \n");
			System.out.println("You don't have enough money! CLEAR OFF!\n");
		}
	}
	public void processOrderAsk(AskOrder c){
			double costToCustomer = 0;
			System.out.println("\u20ac");
			System.out.println("Welcome to the shop " + c.getName());
			System.out.println("Mister money bags! \u20ac" + c.getBudget() + " in your pocket!");
			System.out.println("Lets see whats on your list?");
			// look through the customer order
			for(ProductStock ps : c.getShoppingList()){
				ProductStock shopPS = find(ps.getName());
				// retreive the unit price
				double unitPrice = shopPS.getUnitPrice();
				// set the price on the product held by customer
				ps.getProduct().setPrice(unitPrice);
				// this way we can use the ps method to calc cost;

				if (ps.getQuantity()>shopPS.getQuantity()){
					System.out.println("Sorry, we dont have enough "+ ps.getName() +",You want "+ps.getQuantity()+ " we only have "+ shopPS.getQuantity()+", all or nothing!\n");
				} else{
					costToCustomer += ps.getCost();
					System.out.println("NAME OF PRODUCT " + ps.getName() + " PRICE \u20ac" + ps.getUnitPrice() + " QUANTITY " + ps.getQuantity());
					System.out.println("Amount \u20ac" + ps.getCost() + " SHOP has " + shopPS.getQuantity());
				}
				

			}
			System.out.println("Customer Total: \u20ac" + costToCustomer);
		
			if (costToCustomer ==0){
				System.out.println("We have nothing you wanted! What a waist of time!\n");
			} else if(costToCustomer<c.getBudget()){
				System.out.println("Looks good\n");
				System.out.println("You bill total is " + costToCustomer + ", how much did you say you have again? " + c.getBudget() + "\n");
				double revShopTotal = cash + costToCustomer;
				double revCustTotal = c.getBudget() - costToCustomer;
				System.out.println("Shop flaot is now " + revShopTotal+", while " + c.getName() + " you now have " + revCustTotal + "\n");
			} else if(costToCustomer>c.getBudget()){
				System.out.println("Your bill total is "+ costToCustomer +", how much did you have again? €"+ c.getBudget() +" \n");
				System.out.println("You don't have enough money! CLEAR OFF!\n");
			}
	}
	@Override
	public String toString() {
		return "Shop [cash=" + cash + ", stock=" + stock + "]";
	}

	public static void main(String[] args) {

	}

}
