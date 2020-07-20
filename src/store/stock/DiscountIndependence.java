package store.stock;

import java.io.IOException;

public class DiscountIndependence extends ItemWithDiscount {
	
	private float independence;
	
	//for loading
	public DiscountIndependence(int ID) throws IOException {
		super(ID);
		// TODO Auto-generated constructor stub
	}
	
	public DiscountIndependence(String name, float price) throws IOException {
		super(name, price);
		// for saving
	}
	
	public DiscountIndependence(Item clone,float discount,float independence)
	{
		super(clone,discount);
		this.independence = independence; //XXX
	}
	
	public float getIndependence() {
		return independence;
	}

	public void setIndependence(float independence) {
		this.independence = independence;
	}

	@Override
	public float getFinalPrice() {
		return super.getFinalPrice() - this.independence;
	}
	
}
