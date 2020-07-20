package store.user;

import java.io.IOException;

import utilites.XMLParsing;

public class Manager extends User {
	private int internalPhoneNumber,PIN;
	public Manager() throws IOException {
		super("admin"); //username of manager is "admin"
		XMLParsing objParsing = new XMLParsing("admin");
		this.internalPhoneNumber = objParsing.getInternalPhoneNumber();
		this.PIN =
				Integer.parseInt(
				XMLParsing.getPin());
	}
	
	@Override
	public String printInfo()
	{
		return super.printInfo()
				+ "\"\nInternal Phone Number = \"" + this.internalPhoneNumber
				+ "\"\nPin = \"" + this.PIN;
	}
}
