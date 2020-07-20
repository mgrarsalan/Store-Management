package store.stock;

import java.io.IOException;

import utilites.XMLParsing;

public abstract class Item //since we don't have no discount Item
{
	private String name;
	private int ID;
	private float price;
	
	public int getID() {
		return ID;
	}
	public String getName()
	{
		return this.name;
	}
	public float getPrice()
	{
		return this.price;
	}
	//constructor for saving new item
	public Item(String name,float price) throws IOException 
	{
		this.name = name;
		this.price = price;
		this.ID = XMLParsing.getLastItemID() + 1; //increasement for new Item
		 //XXX
		
	}
	
	//constructor for loading new item
	public Item(int ID) throws IOException
	{
		this.ID = ID;//XXX
		XMLParsing objParsing = new XMLParsing(ID,"item");
		this.price = objParsing.getItemPrice();
		this.name = objParsing.getItemName();
	}
	
	public Item(Item cloneItem)
	{
		this.name = cloneItem.getName();
		this.price = cloneItem.getPrice();
		this.ID = cloneItem.getID();
	}
	
	/**
	 * Overriding equals method from Object class
	 * so that if two items have the same name then they are identical
	 * 
	 */
	@Override
	public boolean equals(Object clone)
	{
		return (((Item)clone).getName() == this.name) ? true : false;
	}
	
	/**
	 * @return The final price of an item which customer should pay
	 */
	public abstract float getFinalPrice();
	
	public String printInformation()
	{
		String lineSeperator = System.getProperty("line.separator"); //\n for output in file
		return "name = \"" + this.name + "\" " + lineSeperator +
				"finalprice = \"" + this.getFinalPrice() + "\" " + lineSeperator + 
				"ItemID = \"" + this.ID  + "\" " + lineSeperator + 
				"Type = \"" + this.getClass().getSimpleName() + "\" " + lineSeperator; //XXX:also override for other properties
	}
	
	@Override
	public String toString()
	{
		return this.name + " " + this.price + " " + this.getClass().getSimpleName(); // + stock total + name +...
	}
	
}
