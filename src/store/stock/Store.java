package store.stock;

import java.io.IOException;
import java.util.ArrayList;

import utilites.XMLParsing;

public final class Store {
	private ArrayList<StoreBasket> servedBaskets;
	
	public Store() throws IOException
	{
		int lastBasketID = XMLParsing.getLastBasketID();
		servedBaskets = new ArrayList<StoreBasket>(lastBasketID);
		StoreBasket basket;
		for(int i = 1; i <= lastBasketID; i++)
		{
			basket = new StoreBasket(i);
			servedBaskets.add(basket);
		}
	}
	
	public final String getBasketHistory() throws IOException
	{
		String historyString = "";
		for (int i = 1; i < servedBaskets.size(); i++) {
			historyString += 
					StoreBasket.printInformation(i)
					+ System.getProperty("line.separator");
		}
		return historyString;
	}
}
