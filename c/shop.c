#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char *customerA = "../customerA.csv"; // This customer is correct and works out
char *customerB = "../customerB.csv"; // This customer has to little money
char *customerC = "../customerC.csv"; // This customer is looking for to many of one product.



struct Product {
	char* name;
	double price;
};

struct ProductStock {
	struct Product product;
	int quantity;
};

struct Shop {
	double cash;
	struct ProductStock stock[20];
	int index;
};

struct Customer {
	char* name;
	double budget;
	double custotal;
	struct ProductStock shoppingList[10];
	int index;
};

void printProduct(struct Product p)
{
	printf("PRODUCT NAME: %s \nPRODUCT PRICE: %.2f\n", p.name, p.price);
}

double find(struct Shop s,char* name){
	for (int i=0; i<s.index; i++){
		if (strcmp(name, s.stock[i].product.name)==0){
			return s.stock[i].product.price;
		}
	}
	return -1;
}

int findQ(struct Shop s,char* name){
	for (int i=0; i<s.index; i++){
		if (strcmp(name, s.stock[i].product.name)==0){
			return s.stock[i].quantity;
		}
	}
	return -1;
}

void printCustomer(struct Customer c)
{
	printf("CUSTOMER NAME: %s \nCUSTOMER BUDGET: %.2f\n", c.name, c.budget);
	printf("-------------\n");
	for(int i = 0; i < c.index; i++)
	{
		printProduct(c.shoppingList[i].product);	
		printf("%s ORDERS %d OF ABOVE PRODUCT\n", c.name, c.shoppingList[i].quantity);
		double cost = c.shoppingList[i].quantity * c.shoppingList[i].product.price; 
		printf("The cost to %s will be €%.2f\n", c.name, cost);
	}
}

struct Shop createAndStockShop()
{
    FILE * fp;
    char * line = NULL;
    size_t len = 0;
    ssize_t read;

    fp = fopen("../stock.csv", "r");
    if (fp == NULL)
        exit(EXIT_FAILURE);

	read = getline(&line, &len, fp);
	float cash = atof(line);
	// printf("cash in shop is %.2f\n", cash);
	
	struct Shop shop = { cash };

    while ((read = getline(&line, &len, fp)) != -1) {
        // printf("Retrieved line of length %zu:\n", read);
        // printf("%s IS A LINE", line);
		char *n = strtok(line, ",");
		char *p = strtok(NULL, ",");
		char *q = strtok(NULL, ",");
		int quantity = atoi(q);
		double price = atof(p);
		char *name = malloc(sizeof(char) * 50);
		strcpy(name, n);
		struct Product product = { name, price };
		struct ProductStock stockItem = { product, quantity };
		shop.stock[shop.index++] = stockItem;
		// printf("NAME OF PRODUCT %s PRICE %.2f QUANTITY %d\n", name, price, quantity);
    }
	
	return shop;
}

void printShop(struct Shop s)
{
	printf("Shop has %.2f in cash\n", s.cash);
	for (int i = 0; i < s.index; i++)
	{
		printProduct(s.stock[i].product);
		printf("The shop has %d of the above\n", s.stock[i].quantity);
	}
}

struct Customer createAndListCustomer(char *customerSelect){
	FILE * fp;
	char * line = NULL;
	size_t len = 0;
	ssize_t read;

	fp = fopen(customerSelect,"r");
	if (fp == NULL)
		exit(EXIT_FAILURE);

	read = getline(&line,&len, fp);
	// Now split the first line to extract the name and budget.
	char *n = strtok(line, ",");
	char *b = strtok(NULL, ",");
	
	char *name = malloc(sizeof(char) * 50);
	strcpy(name, n);

	double budget= atof(b);
	// struct Customer dominic = { "Eoghan", 100.0};
	
	
	struct Shop shop = createAndStockShop();
	
	char *c ="0.0";
	double custotal= atof(c);
    
	struct Customer customer = { name, budget };

    printf("Welcome to the shop %s\nMister money bags! €%.2f in your pocket!\nLets see whats on your list?\n", n, budget);


	while ((read = getline(&line, &len, fp)) != -1) {
		char *k = strtok(line, ",");
		char *q = strtok(NULL, ",");
			
		int quantity = atoi(q);
		char *name = malloc(sizeof(char) * 50);
		strcpy(name, k);

		double price = find(shop, name);
		int shopQuant = findQ(shop, name);	
		struct Product product = { name };
			
		struct ProductStock shoppingList = { product, quantity };
		customer.shoppingList[customer.index++] = shoppingList;
		
		double amount = price * quantity;
		if (quantity >= shopQuant){
			printf("Sorry, we dont have enough %s,You want %d we only have %d, all or nothing!\n", name, quantity, shopQuant);
		}
		else {
			custotal += amount;
			printf("NAME OF PRODUCT %s PRICE €%.2f QUANTITY %d\nAmount:€%.2f, SHOP has %d\n", name, price, quantity, amount, shopQuant);
		}
	}
	printf("Customer Total: €%.2f\n", custotal);

	customer.custotal = custotal;
	
	return customer;
}

void evaluateCustomer(struct Customer c, struct Shop s){
	if(c.custotal == 0.00){
		printf("We have nothing you wanted! What a waist of time!\n");
	} else if (c.custotal<c.budget){
		printf("Looks good\n");
		printf("Your bill total is €%.2f, how much did you have again? €%.2f \n", c.custotal, c.budget);
		double newtotal = s.cash + c.custotal;
		double newcustotal = c.budget - c.custotal;
		printf("Shop float is now €%.2f, while %s you now have €%.2f\n", newtotal, c.name, newcustotal);
		s.cash = newtotal;
	} else if (c.custotal > c.budget){
		printf("Your bill total is €%.2f, how much did you have again? €%.2f \n", c.custotal, c.budget);
		printf("You dont have enough money! CLEAR OFF!\n");
	};
};


struct Customer askName(){
    char n[10];
	double a;
	int pro;
	char name[50];
	int quantity;
	fflush(stdout);

	printf("What is your name?\n");
	scanf("%s", n);
	printf("What is your budget?\n");
	scanf("%lf", &a);
	struct Customer customer = { n, a };

	printf("How many items on your list?\n");
	scanf("%d", &pro);
	printf("Glad to meet you, Welcome to you and to your €%.2lf, %s - What can i get you?%d items\n", a, n, pro); 
		
	// struct ProductStock shoppingList = { product, quantity };

	
	struct Shop shop = createAndStockShop();
	
	char *c ="0.0";
	double custotal= atof(c);

	for(int i = 0; i < pro; i++)
	{
		printf("What would you like and how many?%d\n", i);
		scanf("%5s %d", name, &quantity);
		fflush(stdout);

		//int quantity = atoi(q);
		//char *name = malloc(sizeof(char) * 50);
		//strcpy(name, k);
		
		double price = find(shop, name);
		int shopQuant = findQ(shop, name);
		struct Product product = { name };

		struct ProductStock shoppingList = { product, quantity };
		customer.shoppingList[customer.index++] = shoppingList;
		
		double amount = price * quantity;
		if (quantity >= shopQuant){
			printf("Sorry, we dont have enough %s,You want %d we only have %d, all or nothing!\n", name, quantity, shopQuant);
		}
		else {
			custotal += amount;
			printf("NAME OF PRODUCT %s PRICE €%.2f QUANTITY %d\nAmount:€%.2f, SHOP has %d\n", name, price, quantity, amount, shopQuant);
		}
	}
	printf("Customer Total: €%.2f\n", custotal);

	customer.custotal = custotal;
	
	return customer;
}

int main(void) 
{	
	int choice =-1;

	struct Shop shop = createAndStockShop();
	//printShop(shop);


	while(choice !=0 ){
		fflush(stdin);
		printf("************\nPlease choose an option below:\n    1: Customer dose not have enough money.\n    2: Customer is looking for to much of one or more products\n    3: Customer is just right\n    4: Manually enter your order\n    0: To break\n");
		scanf("%d",&choice);

		if(choice == 1){
			struct Customer customer = createAndListCustomer(customerB);
			evaluateCustomer(customer, shop);
		}
		else if (choice == 2){
			struct Customer customer = createAndListCustomer(customerC);
			evaluateCustomer(customer, shop);
		}
		else if (choice == 3){
			struct Customer customer = createAndListCustomer(customerA);
			evaluateCustomer(customer, shop);
		}
		else if (choice == 4){
			struct Customer customer = askName();
			//printShop(shop);
			evaluateCustomer(customer, shop);
		}
		else if (choice == 0){
			printf("Bye");
			return 0;
		}
	}
	printf("Bye");
	
	//printf("***** LETS HAVE A LOOK AT THE SHOP ***********\n");
	//struct Shop shop = createAndStockShop();
	//printShop(shop);
	
	//printf("***** CAN I HELP YOU? ***********\n");
	//struct Customer customer = createAndListCustomer(customerB);
	
	//printf("***** REVIEW THE ORDER ***********\n");
	//evaluateCustomer(customer, shop);
    return 0;
}