package store.stock;

import java.io.IOException;

import utilites.XMLParsing;

public class ItemWithDiscount extends Item {

	private float discount; //must be expressed in percent //TODO: setter and getter
	
	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	//for saving
	public ItemWithDiscount(String name, float price) throws IOException {
		super(name, price);
		// TODO Auto-generated constructor stub
	}
	
	//for loading
	public ItemWithDiscount(int ID) throws IOException {
		super(ID);
		// TODO Auto-generated constructor stub
	}
	
	public ItemWithDiscount(Item cloneDiscount,float discount)
	{
		super(cloneDiscount);
		this.discount = discount;
	}
	
	public float calculateDiscount()
	{
		return
				getPrice() * this.discount;
	}
	@Override
	public float getFinalPrice() {
		return 
				getPrice() - calculateDiscount();
	}
	
}
