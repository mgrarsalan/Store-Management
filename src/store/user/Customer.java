package store.user;

import java.io.IOException;

import utilites.INIParsing;
import utilites.Utility;
import utilites.XMLParsing;

public class Customer extends User {
	private boolean loyal;
	
	public boolean isLoyal()
	{
		return loyal;
	}
	
	//for loading customer
	public Customer(String username) throws IOException {
		super(username);
		XMLParsing obParsing = new XMLParsing(username);
		this.loyal = (obParsing.getUserType().equals("LoyalCustomer")) ? true : false;
		
		// TODO Auto-generated constructor stub
	}
	@Override
	public String printInfo() {
		String info = "";
		try {
			if (INIParsing.getCurrentLanguageString() == "En")
			{
				info = "Loyal customer? : " + this.loyal + "\n"  +
			" User Name: " + this.getUserName() + "\n"
				+ " Name: " + this.getName() + "\n" 
				+ " surname: " + this.getSurname() + "\n"
			+ " ID: " + this.getId() + "\n" 
				+ " Address: " + this.getAddress()  + "\n"  +
				" Phone Number: " + this.getPhoneNumber() + "\n" 
				+ " Bonus Point: " +  this.calculateBonus();
			}
			else {
				info = "مشتری همیشگی؟:" + (this.loyal ? "بله" : "خیر") + "\n" +  
						" نام کاربری: " + this.getUserName() + "\n" 
				+ " نام: " + this.getName() + "\n" 
						+ " نام خانوادگی: " + this.getSurname() + "\n"
						+ " شناسه: " + this.getId() + "\n" 
				+ " آدرس: " + this.getAddress() + "\n" +
				" شماره تلفن: " + this.getPhoneNumber() + "\n" + 
				" امتیاز: " +  this.calculateBonus();
			}
		} catch (IOException e) {
			Utility.IOError(null, "iniParsing", false);
		}
		return info;
	}
	public int calculateBonus() throws IOException
	{
		if (!loyal)
			return 0; //it's a simple customer
		else {
			XMLParsing objParsing = new XMLParsing(this.getUserName());
			int bonus = objParsing.calculateAllBasketPoints();
			return (bonus == 0) ? 0 : bonus / 10; //handling zero division
		}
		
	}
	
}
