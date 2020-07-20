package store.stock;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import store.user.Cashier;
import store.user.Customer;
import store.user.User;
import utilites.Utility;
import utilites.XMLParsing;

public class StoreBasket implements store.Payable {
	private int ID;
	private float netAmount,totalAmount,VAT;
	private final String addressOfStore; //XXX?
	private ArrayList<Item> listOfItems = new ArrayList<Item>();
	private User customer;
	private int bonusPoint;
	private String nameOfCashier;
	private String timeOfPruchase;
	private boolean xmassOff = false; //10% off for Xmass with default value of false
	public StoreBasket(Cashier seller,User customer,float vat) throws IOException 
	{
		this.nameOfCashier = seller.getName();
		this.customer = customer;
		this.VAT = vat; //XXX
		this.ID = getID();
		this.addressOfStore = XMLParsing.getAddressOfStore();
	}
	public void setXmassOff(boolean xmassOff) {
		this.xmassOff = xmassOff;
	}
	public int getID() throws IOException
	{
		return (XMLParsing.getLastBasketID()) + 1; //increasement of ID
	}
	//constructor for loading inofrmation
	public StoreBasket(int ID) throws IOException
	{
		XMLParsing objParsing = new XMLParsing(ID, "basket");
		this.addressOfStore = XMLParsing.getAddressOfStore();
		this.bonusPoint = objParsing.getBonusPoint();
		this.customer = new Customer(objParsing.getCustomer());
		this.ID = ID;
		this.nameOfCashier = objParsing.getCashierName();
		this.netAmount = objParsing.getNetAmount();
		this.timeOfPruchase = objParsing.getTimeOfPurchase();
		this.totalAmount = objParsing.getTotalAmount();
		this.VAT = objParsing.getVAT();
		this.listOfItems = objParsing.getBasketItems();
		
	}
	public void clearItems() //clear items of array list of items
	{
		listOfItems.clear();
	}
	public void addItem(Item item)
	{
		listOfItems.add(item);
	}
	public void removeItem(Item item)
	{
		listOfItems.remove(item);
	}
	
	
	public void calculatePoints()
	{
		if (customer instanceof Customer) //if customer is just a customer
		{
			if (!((Customer) customer).isLoyal()) //and is not loyal
				this.bonusPoint = 0;
			else {
				this.bonusPoint = (int)(this.getTotalAmount() / 5); //and is loyal
			}
		}
		if (customer instanceof Cashier) //if customer is an employee
		{
			this.bonusPoint = (int)(this.getTotalAmount() / 15);
		}
		
	}
	//for saving basket to a file
	public void saveBasket() throws IOException
	{
		calculatePoints(); 
		DateTimeFormatter dateFormat  = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
		this.timeOfPruchase = LocalDateTime.now().format(dateFormat);
		this.ID = getID(); //new information must get read again from the file
		String xmlTag =
				"<Basket cashiername = \"" + nameOfCashier + //and so forth
				"\" total = \"" + getTotalAmount() + 
				"\" VAT = \"" + getVAT() + 
				 "\" netamount = \"" + calculatePayment() +
				"\" bonuspoint = \"" + this.bonusPoint +
				"\" BasketID = \"" + this.ID +
				"\" timeOfPurchase = \"" + this.timeOfPruchase +
				"\" addressOfStore = \"" + this.addressOfStore +
				"\" >\n";
		//array for generating <item> tags in xml document 
		ArrayList<String> itemTags = new ArrayList<String>(listOfItems.size()); 
		String itemTagString= "";
		
		for (Item item : listOfItems) {
			itemTagString += "<Item " + item.printInformation() + "/>" + "\n";
		}
		XMLParsing objParsing = new XMLParsing(customer.getUserName());
		objParsing.saveBasket((xmlTag + itemTagString + "</Basket>\n"));
		
	}
	public void setVAT(float vAT) {
		VAT = vAT;
	}
	public float getVAT() {
		return VAT;
	}
	private void calculateTotalAmount()
	{
		int frequency;
		for (Item item : listOfItems) {
			frequency = 0;
			if (item instanceof BuyMorePayLess)
			{
				frequency = Collections.frequency(listOfItems, item);
				if (frequency > 10)
					throw new IllegalArgumentException(); //Maximum should be 10 items
				((BuyMorePayLess) item).calculateDiscount(frequency);
			}
			else if (item instanceof ItemTakeItAll) {
				frequency = Collections.frequency(listOfItems, item);
				((ItemTakeItAll) item).calculateDiscount(frequency);
			}	
		}
		
		totalAmount = 
				(float) listOfItems.stream().mapToDouble(x ->  x.getFinalPrice()).sum();
	}
	public float getTotalAmount()
	{
		calculateTotalAmount();
		return totalAmount;
	}
	
	@Override
	public float calculatePayment() throws IOException //=Final price
	{
		int bonus;
		if (customer instanceof Customer)
			bonus = ((Customer)customer).calculateBonus();
		else 
			bonus = ((Cashier)customer).calculateBonus();
		float finPrice = 
				getTotalAmount() * (xmassOff == true ? 0.9F : 1) //0.9 refers to 10% off (0.1-0.01)
				- bonus + this.VAT;
		return (finPrice  > 0) ? finPrice : 0; //Negative number is illogical. at worst a basket is free
	}
	
	public static String printInformation(int ID) throws IOException //for all baskets
	//static cause it should print other baskets by their id too
	{
		String lineSeperator = System.getProperty("line.separator"); //\n for output in file
		XMLParsing objParsing = new XMLParsing(ID,"basket");
		String information = "Customer = \"" + objParsing.getCustomer() + lineSeperator + 
		"\"Name of the cashier = \"" + objParsing.getCashierName() + lineSeperator +
		"\"ID = \"" + ID + lineSeperator + 
		"\"Total Amount = \"" + objParsing.getTotalAmount() + lineSeperator + 
		"\"VAT = \"" + objParsing.getVAT() + lineSeperator +
		"\"Net Amount = \"" + objParsing.getNetAmount() + lineSeperator +
		"\"Address of the store = \"" + XMLParsing.getAddressOfStore() + lineSeperator +
		"\"Bonus Point = \"" + objParsing.getBonusPoint() + lineSeperator +
		"\"Time of purchase = \"" + objParsing.getTimeOfPurchase() + lineSeperator + 
		"\"List of Items:" + lineSeperator +
		objParsing.getBasketItems().stream().map(e -> e.printInformation() + lineSeperator).reduce("", String::concat) + lineSeperator;
		//XXX
		return information;
		
	}
	

	
}
