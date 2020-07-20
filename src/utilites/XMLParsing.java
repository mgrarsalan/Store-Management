package utilites;

import java.io.IOException;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import store.stock.*;

/**
 * A class for parsing xml documents eg. saving information and reading it
 * @author Arsalan Jafari
 *
 */
public class XMLParsing {
	private static final Path filePath = Paths.get("Resources\\data.xml"); // Ad hoc: only works in this file and in this program
	private String content; // The entire file of data.xml in one string
	private String tag; // Contains the specific tag that is going to be processed
	
	public static String getContent() throws IOException
	{
		return Files.readString(filePath);
	}
	
	/**
	 * Overloaded constructor specified for items and baskets
	 * @param type "item" for Items or "basket" for StoreBaskets
	 * @param userName
	 * @throws IOException
	 */
	public XMLParsing(int ID,String type) throws IOException //if user does not exist(maybe in password method)
	{
		content = Files.readString(filePath); //should this be in a static block
		if (type.equals("item"))
		getItemTag(ID);
		else
			getBasketTag(ID);
	}
	/**
	 * Overloaded constructor specified for checking login information
	 * @param userName
	 * @throws IOException
	 */
	public XMLParsing(String userName) throws IOException //if user does not exist(maybe in password method)
	{
		content = Files.readString(filePath);
		getUserTag(userName);
	}
	/**
	 * Getting an entire tag of the XML document which has the item with speciefic id in it
	 * @param ID of the Item
	 */
	private void getItemTag(int ID)
	{
		String stocksTag = //Getting the entire <Stocks></Stocks> tag
				content.substring( 
				content.indexOf("<Stocks>"),
				content.indexOf("</Stocks>"));
		int indexOfID =
				stocksTag.indexOf("ID = \"" + ID);
		if (indexOfID == -1)
			throw new IllegalArgumentException(); //XXX:no such item exists. must be handled in GUI level;
		this.tag =
				stocksTag.substring(stocksTag.lastIndexOf('>',indexOfID),indexOfID) //start of <Item>
				// (which is from the '>' previous item)
				+ stocksTag.substring(indexOfID,stocksTag.indexOf("/>",indexOfID)); //till the end of item
	}
	public String getItemName()
	{
		int indexOfName =
				tag.indexOf("name = \"") + 8;
		return
				tag.substring(indexOfName,tag.indexOf('\"',indexOfName));
	}
	public float getItemPrice()
	{
		int indexOfPrice = 
				tag.indexOf("price = \"") + 9;
		return
				Float.parseFloat(
						tag.substring(indexOfPrice,tag.indexOf('\"',indexOfPrice)));
	}
	
	
	/**
	 * Finding what is the type of the item in <item> tag 
	 * @return 1 for discount </br>
	 * 2 for discountIndependence </br>
	 * 3 for BuyMorePayLess </br>
	 * 4 for ItemTakeItAll
	 */
	public byte getItemType()
	{
		int indexOfType = 
				tag.indexOf("type = \"") + 8;
		String type =
				tag.substring(indexOfType,tag.indexOf('\"',indexOfType));
		if (type.equals("discount"))
			return 1;
		else if (type.equals("discountIndependence"))
			return 2;
		else if (type.equals("BMPL"))
			return 3;
		else
			return 4;
	}
	/**
	 * @return Last ID saved which is also the greatest id saved
	 * @throws IOException 
	 */
	public static int getLastItemID() throws IOException
	{
		//Items are stored in order of their ID
		//getting ID of last user
		String content = Files.readString(filePath);
		int indexOfID = content.lastIndexOf("ID = \"") + 6;
		return Integer.parseInt(
				content.substring(indexOfID,content.indexOf('\"', indexOfID)));
	}
	
	public static void addItem(Item item, String type) throws IOException
	{
		String addTag = //the new tag for <item>
				"<Item type = \"" + type +
				"\" ID = \"" + item.getID() +
				"\" name = \"" + item.getName() +
				"\" price = \"" + item.getPrice() + 
				"\" />\n";
		String content = Files.readString(Paths.get("Resources\\data.xml"));
		int indexOfStock = //Index of </Stocks> since item must be placed before this tag
				content.indexOf("</Stocks>");
		Utility.insertDataToFile("Resources\\data.xml", indexOfStock, addTag);
	}
	
	public void getBasketTag(int ID)
	{
		String users = //From <Users> till </Users> since baskets are int this range
				content.substring(
						content.indexOf("<Users>"),
						content.indexOf("</Users>")
						);
		String basket;
		int indexOfID =
				users.indexOf("BasketID = \"" + ID);
		if (indexOfID == -1)
			throw new IllegalArgumentException(); //XXX:no such item exists. must be handled in GUI level;
		this.tag =
				users.substring(users.lastIndexOf('>',indexOfID) + 1,indexOfID)
				//from > in previous <Basket> till Basketid
				+ users.substring(indexOfID,users.indexOf("</Basket>",indexOfID));
				// and from BasketID till the end
	}
	
	public String getCashierName()
	{
		int index = tag.indexOf("cashiername = \"") + 15;
		return tag.substring(index, tag.indexOf('\"',index));
	}
	public float getTotalAmount()
	{
		int index = tag.indexOf("total = \"") + 9;
		return Float.parseFloat( 
				tag.substring(index, tag.indexOf('\"',index)));
	}
	public float getNetAmount()
	{
		int index = tag.indexOf("netamount = \"") + 13;
		return Float.parseFloat( 
				tag.substring(index, tag.indexOf('\"',index)));
	}
	public int getBonusPoint()
	{
		int index = tag.indexOf("bonuspoint = \"") + 14;
		return Integer.parseInt(
				tag.substring(index, tag.indexOf('\"',index)));
	}
	public String getTimeOfPurchase()
	{
		int index = tag.indexOf("timeOfPurchase = \"") + 18;
		return tag.substring(index, tag.indexOf('\"',index));
	}
	public static String getAddressOfStore() throws IOException
	{
		String content = Files.readString(filePath);
		int index = content.indexOf("addressOfStore = \"") + 18;
		return content.substring(index, content.indexOf('\"',index));
	}
	public float getVAT()
	{
		int index = tag.indexOf("VAT = \"") + 7;
		return Float.parseFloat( 
				tag.substring(index, tag.indexOf('\"',index)));
	}
	public String getCustomer() //will return the username of the customer who bought the basket
	{
		int indexOfTag = 
				content.indexOf(tag);
		int indexOfUsername = content.lastIndexOf("username = \"", indexOfTag) + 12; //XXX check it
		return content.substring(
				indexOfUsername,content.indexOf('\"', indexOfUsername)
				);
	}
	
	/**
	 * @return an ArrayList consisting of all items in the basket
	 * @throws IOException 
	 */
	public ArrayList<Item> getBasketItems() throws IOException
	{
		int index = 0;
		int indexOfID, ID, indexOfType; 
		String type; //Type of Item
		ArrayList<Item> listofPurchasedItems = new ArrayList<Item>();
		Item currentPurchasedItem = null;
		while (true)
		{
			index = this.tag.indexOf("<Item",index) + 2;
			if (index == 1)  //1 = -1 +2
				break;
			indexOfID = this.tag.indexOf("ItemID = \"", index) + 10;
			ID = Integer.parseInt(
					tag.substring(indexOfID, tag.indexOf('\"',indexOfID)));
			indexOfType = this.tag.indexOf("Type = \"", index) + 8;
			type =
					tag.substring(indexOfType, tag.indexOf('\"',indexOfType));
			if (type.equals("ItemWithDiscount") == true)
			{
				listofPurchasedItems.add((new ItemWithDiscount(ID)));
			}
			else if (type.equals("BuyMorePayLess") == true) {
				
				listofPurchasedItems.add((new BuyMorePayLess(ID)));
			}
			else if (type.equals("ItemTakeItAll") == true) {
				
				listofPurchasedItems.add((new ItemTakeItAll(ID)));
			}
			else { //discountIndependence
				
				listofPurchasedItems.add((new DiscountIndependence(ID)));
			}
		}
		return listofPurchasedItems;
	}
	
	/**
	 * @return The ID of last (greatest ID) of store basket
	 * @throws IOException 
	 */
	public static int getLastBasketID() throws IOException
	{
		String content = Files.readString(filePath);
		String subString = 
				content.substring(0, content.indexOf("</Users>")); //till the </Users>
		int indexOfID = 0;
		int lastID = 0;
		int currentID = 0;
		do {
			indexOfID = 
					subString.indexOf("BasketID = \"",indexOfID) + 12;
			if (indexOfID !=  11) // 11 = -1 + 12
			{
				currentID = 
						Integer.parseInt(
								subString.substring(indexOfID, subString.indexOf('\"', indexOfID)));
				if (currentID > lastID)
					lastID = currentID;
			}
			else
				break;
		} while (true);
		return lastID;
	}
	
	/**
	 * saves the entire basket data to data.xml
	 * @throws IOException 
	 */
	public void saveBasket(String xmlData) throws IOException
	{
		int offset = 
				content.indexOf(tag.substring(tag.indexOf("<Baskets>"))) + 12;
		Utility.insertDataToFile("Resources\\data.xml", offset,xmlData);
	}
	
	/**
	 * @return All baskets points from a user
	 */
	public int calculateAllBasketPoints()
	{
		int indexOfPoint = 0,basketPoint,sum = 0;
		do {
			indexOfPoint = //index of bonuspoint attribute
					(this.tag.indexOf("bonuspoint = \"",indexOfPoint) + 14); 
			if (indexOfPoint !=  13) // 13 = -1 + 14
			{
				basketPoint = Integer
						.parseInt(this.tag.substring(indexOfPoint, this.tag.indexOf('\"', indexOfPoint)));
				sum += basketPoint; //sum of points
			}
			else 
				break;
		} while (true);
		return sum;
		
	}
	
	/**
	 * Getting an entire tag of the XML document which has the username in it
	 * @param userName
	 * @param password passWord as an array of characters because of security reasons
	 */
	private void getUserTag(String userName)
	{
		
		int indexOfUsername =
				content.indexOf(userName);
		boolean compeleteUser = content.charAt(indexOfUsername + userName.length() + 1) != '\"'; // to test if username is a complete word
		//for example test is correct while tes is not
		//after that it must be "
		if (indexOfUsername == -1 || !content.substring(indexOfUsername - 12,indexOfUsername).equals("username = \"") //before entry must be checked 
			 || compeleteUser == false) 
			throw new IllegalArgumentException(); //XXX:no such user exists. must be handled in loginform;
		this.tag =
				content.substring(content.lastIndexOf('>', indexOfUsername),indexOfUsername)
				+ content.substring(indexOfUsername,content.indexOf("</User>", indexOfUsername));
	}
	/**
	 * To check validation of entered username and password
	 * @param userName
	 * @param passWord as an array of characters because of security reasons
	 * @return As byte: 0 - if username and password is incompatible </br>
	 * 1- if username and password are correct and belongs to a customer</br>
	 * 2- if username and password are correct and belongs to a cashier</br>
	 * 3- if username and password are correct and belongs to a manager
	 */
	public byte checkLogin(char[] passWord)
	{
		if (!Arrays.equals(getUserPassword(), passWord)) 
			return 0; //Password is incorrect 
		if (getUserType().contains("Manager"))
			return 3;
		else if (getUserType().contains("Customer"))
			return 1;
		else if (getUserType().contains("Cashier"))
			return 2;
		else
			throw new IllegalArgumentException(); //XXX:
	}
	/**
	 * @return the value of "type" attributes in a tag
	 */ 
	public String getUserType() //XXX: exception handling in case of missing type attribute
	{
		int indexOfType = tag.indexOf("type = \"") + 8;
		return tag.substring(indexOfType, tag.indexOf('\"',indexOfType));
	}
	
	public String getName()
	{
		int index = tag.indexOf("name = \"") + 8;
		return tag.substring(index, tag.indexOf('\"',index));
	}
	public String getSurname()
	{
		int index = tag.indexOf("surname = \"") + 11;
		return tag.substring(index, tag.indexOf('\"',index));
	}
	public String getAddress()
	{
		int index = tag.indexOf("address = \"") + 11;
		return tag.substring(index, tag.indexOf('\"',index));
	}
	public long getPhoneNumber()
	{
		int index = tag.indexOf("phone = \"") + 9;
		return Long.parseLong(tag.substring(index, tag.indexOf('\"',index)));
	}
	public int getUserID()
	{
		int index = tag.indexOf("ID = \"") + 6;
		return Integer.parseInt((tag.substring(index, tag.indexOf('\"',index))));
	}
	public char[] getUserPassword()
	{
		int index = tag.indexOf("password = \"") + 12;
		return tag.substring(index, tag.indexOf('\"',index)).toCharArray();
	}
	public int getInternalPhoneNumber()
	{
		int index = tag.indexOf("internalPhoneNumber = \"") + 23;
		return Integer.parseInt(
				tag.substring(index, tag.indexOf('\"',index)));
	}
	public float getBaseSalary()
	{
		int index = tag.indexOf("baseSalary = \"") + 14;
		return Float.parseFloat(
				tag.substring(index, tag.indexOf('\"',index)));
	}
	public float getHoursOfWork()
	{
		int index = tag.indexOf("hoursOfWork = \"") + 15;
		return Float.parseFloat(
				tag.substring(index, tag.indexOf('\"',index)));
	}
	
	/**
	 * will set the new hours of work to data.xml of the related user
	 * @param hoursOfWork
	 * @throws IOException 
	 */
	public void saveHoursOfWork(float hoursOfWork) throws IOException
	{
		int offsetOfUser =
				content.indexOf(tag); //offset till user tag
		int offset = //offset of hoursOfWork = "NUMBER"
				tag.indexOf("hoursOfWork = \"") + 15;
		Utility.replacingDataToFile("Resources\\data.xml",offsetOfUser + offset, String.valueOf(hoursOfWork)+"\" >");
	}
	/**
	 * @param pin
	 * @return true if the entered pin is correct. will return false otherwise.
	 * @throws IOException 
	 */
	public static String getPin() throws IOException
	{
		//FIXME:check if pin is ""
		String content = Files.readString(filePath);
		int index = content.indexOf("pin = \"") + 7;
		return content.substring(index,content.indexOf('\"', index));
	}
	
}

