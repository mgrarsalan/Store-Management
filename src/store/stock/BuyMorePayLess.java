package store.stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import utilites.XMLParsing;

public class BuyMorePayLess extends Item {
	private byte buyCondition , payBonus;
	private float discount;
	
	//for saving
	public BuyMorePayLess(String name, float price) throws IOException {
		super(name, price);
		//XXX:Checking
		
	}
	
	//for loading
	public BuyMorePayLess(int ID) throws IOException {
		super(ID);
		
	}
	
	public BuyMorePayLess(BuyMorePayLess clone)
	{
		super(clone);
		this.buyCondition = clone.getBuyCondition();
		this.payBonus = clone.getPayBonus();
	}
	
	public void calculateDiscount(int itemCount) {
		if (itemCount >= buyCondition)
		this.discount = (float)(payBonus) / this.buyCondition;
		else
			this.discount = 1;
	 //it is zero then
	}
	
	public byte getBuyCondition() {
		return buyCondition;
	}

	public void setBuyCondition(byte buyCondition) {
		this.buyCondition = buyCondition;
	}

	public byte getPayBonus() {
		return payBonus;
	}

	public void setPayBonus(byte payBonus) {
		this.payBonus = payBonus;
	}
	

	@Override
	public float getFinalPrice() {
		//XXX:Zero
		return this.discount * this.getPrice();
	}
	
	
}
