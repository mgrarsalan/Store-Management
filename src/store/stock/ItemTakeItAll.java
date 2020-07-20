package store.stock;

import java.io.IOException;

public class ItemTakeItAll extends ItemWithDiscount {
	private byte Nparameter;
	private float discountPerItem; 
	public ItemTakeItAll(String name, float price) throws IOException {
		super(name, price);
		//XXX:
		// TODO Auto-generated constructor stub
	}
	public ItemTakeItAll(int ID) throws IOException {
		super(ID);
	}
	
	public ItemTakeItAll(ItemTakeItAll clone)
	{
		super((ItemWithDiscount)clone,clone.getDiscount());
		
		this.Nparameter = clone.getNparameter();
	}
	public byte getNparameter() {
		return Nparameter;
	}
	public void setNparameter(byte nparameter) {
		Nparameter = nparameter;
	}
	/**
	 * @param numberOfItemsPurchased
	 * @return true if minimum number is purchased otherwise returns false
	 */
	public void calculateDiscount(int numberOfItemsPurchased)
	{
		if (numberOfItemsPurchased < Nparameter)
			discountPerItem = 0;
		else
			discountPerItem = this.getDiscount();
	}
	
	@Override
	public float getFinalPrice()
	{
		return (1 - discountPerItem) * getPrice();
	}
	
}
