from dataclasses import dataclass, field
from typing import List
import csv

@dataclass
class Product:
    name: str
    price: float = 0.0

@dataclass 
class ProductStock:
    product: Product
    quantity: int

@dataclass 
class Shop:
    cash: float = 0.0
    stock: List[ProductStock] = field(default_factory=list)

@dataclass
class Customer:
    name: str = ""
    budget: float = 0.0
    shopping_list: List[ProductStock] = field(default_factory=list)
    cust_total: float = 0.0

def create_and_stock_shop():
    s = Shop()
    with open('../stock.csv') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        first_row = next(csv_reader)
        s.cash = float(first_row[0])
        for row in csv_reader:
            p = Product(row[0], float(row[1]))
            ps = ProductStock(p, float(row[2]))
            s.stock.append(ps)
            #print(ps)
    return s
    
def read_customer(file_path):
    with open(file_path) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        first_row = next(csv_reader)
        c = Customer(first_row[0], float(first_row[1]))
        for row in csv_reader:
            name = row[0]
            quantity = float(row[1])
            p = Product(name)
            ps = ProductStock(p, quantity)
            c.shopping_list.append(ps)
        return c 
        

def print_product(p):
    print(f'\nPRODUCT NAME: {p.name} \nPRODUCT PRICE: {p.price}')

def print_customer(c,s):

    print(f"Welcome to the shop {c.name}\nMister money bags! €{c.budget} in your pocket!\nLets see whats on your list?\n")
    cust_total = 0
    for item in c.shopping_list:
        for prod in s.stock:
            if item.product.name==prod.product.name:
                shop_price = prod.product.price
                shop_quant = int(prod.quantity)
        
        cost = item.quantity * shop_price
        if item.quantity >= shop_quant:
            print(f"Sorry, we dont have enough {item.product.name},You want {item.quantity } we only have {shop_quant}, all or nothing!\n")
        else:
            cust_total+=cost
            print(f"NAME OF PRODUCT {item.product.name} PRICE €{shop_price} QUANTITY {item.quantity}\nAmount:€{cost}, SHOP has {shop_quant}\n")
    print(f'Customer Total: €{cust_total}')
    Customer.cust_total = cust_total
    Customer.budget = c.budget
    return Customer
        
def print_shop(s):
    print(f'Shop has {s.cash} in cash')
    for item in s.stock:
        print_product(item.product)
        print(f'The Shop has {item.quantity} of the above')
        
def derping(s, c):
    for item in c.shopping_list:
        for prod in s.stock:
            if item.product.name==prod.product.name:
                print(item)

def find_price(s, c):
    for item in c.shopping_list:
        for prod in s.stock:
            if item.product.name==prod.product.name:
                item.product.price==prod.product.price

def evaluateCustomer(c, s):
    if c.cust_total == 0:
        print("We have nothing you wanted! What a waist of time!\n")
    elif c.cust_total<c.budget:
        print("Looks good\n")
        print(f'Your bill total is €{c.cust_total}, how much did you have again? €{c.budget} \n')
        newshoptotal = s.cash + c.cust_total
        newcusttotal = c.budget-c.cust_total
        print(f"Shop float is now €{newshoptotal}, while {c.name} you now have €{newcusttotal}\n")
        s.cash = newshoptotal
    elif c.cust_total>c.budget:
        print(f'Your bill total is €{c.cust_total}, how much did you have again? €{c.budget} \n')
        print("You don't have enough money! CLEAR OFF!\n")

def askName(s):
    name = input('Whats your name?\n')
    budget = int(input('What is your budget?\n'))
    qtyList = int(input('How many items on your list?\n'))
    print(f'Glad to meet you, Welcome to you and to your €{budget}, {name} - What can i get you?{qtyList} items\n')
    c = Customer(name, budget)
    cust_total = 0
    for _ in range(qtyList):
        prod_name = input('What would you like?\n')
        prod_qty = int(input('How many would you like?\n'))
        p = Product(prod_name)
        ps = ProductStock(p, prod_qty)

        c.shopping_list.append(ps)

        for prod in s.stock:
            if prod_name==prod.product.name:
                shop_price = prod.product.price
                shop_quant = prod.quantity

        
        cost = prod_qty * shop_price
        if prod_qty >= shop_quant:
            print(f"Sorry, we dont have enough {prod_name},You want {prod_qty } we only have {shop_quant}, all or nothing!\n")
        else:
            cust_total+=cost
            print(f"NAME OF PRODUCT {prod_name} PRICE €{shop_price} QUANTITY {prod_qty}\nAmount:€{cost}, SHOP has {shop_quant}\n")
    print(f'Customer Total: €{cust_total}')
    c.cust_total = cust_total
    c.budget = c.budget

    return c



s = create_and_stock_shop()

a = read_customer("../customerB.csv")
b = read_customer("../customerC.csv")
c = read_customer("../customerA.csv")


menu = "************\nPlease choose an option below:\n     1: Customer dose not have enough money.\n     2: Customer is looking for to much of one or more products\n     3: Customer is just right\n     4: Manually enter your order\n     0: To break\n"


def main():
    num = int(input(menu))
    if num == 1:
        answer = print_customer(a, s)
        evaluateCustomer(answer, s)
        main()
    elif num == 2:
        answer = print_customer(b, s)
        evaluateCustomer(answer, s)
        main()
    elif num == 3:
        answer = print_customer(c, s)
        evaluateCustomer(answer, s)
        main()
    elif num == 4:
        Manu_cust = askName(s)
        evaluateCustomer(Manu_cust, s)
        main()
    else:
        print("Bye Bye!")


if __name__ == "__main__":
    main()