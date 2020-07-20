package store.user;

import java.io.IOException;

import utilites.Utility;
import utilites.XMLParsing;

public class Cashier extends User implements store.Payable {
	private int internalPhoneNumber;
	private float baseSalary;
	private float hoursOfWork; //the decimal part must be converted into minutes. also time stuff coding
	private XMLParsing objParsing;
	//for loading the cashier
	public Cashier(String username) throws Exception {
		super(username);
		objParsing = new XMLParsing(username);
		this.internalPhoneNumber = objParsing.getInternalPhoneNumber();
		this.baseSalary = objParsing.getBaseSalary();
		this.hoursOfWork = objParsing.getHoursOfWork();
	}

	public int getInternalPhoneNumber() {
		return internalPhoneNumber;
	}

	public void setInternalPhoneNumber(int internalPhoneNumber) {
		this.internalPhoneNumber = internalPhoneNumber;
	}

	public float getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(float baseSalary) {
		this.baseSalary = baseSalary;
	}

	public float getHoursOfWork() { //throw?
		return objParsing.getHoursOfWork();
	}

	public void setHoursOfWork(float hoursOfWork) throws Exception {
		if (hoursOfWork < 0)
			throw new IllegalArgumentException();
		this.hoursOfWork = hoursOfWork;
		objParsing.saveHoursOfWork(this.hoursOfWork);
	}
	public int calculateBonus() throws IOException
	{
		int overWork = (int)getHoursOfWork() - 8;
		return 
				(overWork > 0 ? overWork : 0);
	}
	
	/**
	 * 
	 * @return final salary of the cashier
	 * @throws IOException 
	 */
	public float finalSalary() throws IOException
	{
		return getBaseSalary() + calculateBonus();
	}

	@Override
	public String printInfo() {
		return super.printInfo() //basic informations of parent class
				+ 
				"\"\nInternal Phone Number = \"" + this.internalPhoneNumber +
				"\"\nBase Salary = \"" + this.baseSalary +
				"\"\nHours Of Work = \"" + this.hoursOfWork;
	}

	@Override
	public float calculatePayment() throws IOException {
		XMLParsing objParsing = new XMLParsing(this.getUserName());
		return this.baseSalary +
				(this.hoursOfWork > 8 ? ((int)this.hoursOfWork - 8) : 0); 
	}
	
}
