package store.user;

import java.io.IOException;

import javax.sound.sampled.Line;

import utilites.XMLParsing;

public abstract class User {
	private String name,surname,userName,address;
	private int id;
	private long phoneNumber;
	private char[] password;
	//TODO: Usage of setters are mostly because of option forms 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	
	//This constructor is used for loading the user. should i make one with id?
	public User(String username) throws IOException
	{
		XMLParsing objParsing = new XMLParsing(username);
		///XXX: if user does not exist. for that i think i have to handle it in xmlparsing constructor
		this.name = objParsing.getName();
		this.surname = objParsing.getSurname();
		this.userName = username;
		this.address = objParsing.getAddress();
		this.phoneNumber = objParsing.getPhoneNumber();
		//this.password for chaning password and preventing harm?
		
	}
	
	public String printInfo()
	{	
		return "Name = \"" + this.name + 
		"\"\nSurname = \"" + this.surname + 
		"\"\nID = \"" + this.id +
		"\"\nUsername = \"" + this.userName + 
		"\"\nAddress = \"" + this.address + 
		"\"\nPhoneNumber = \"" + this.phoneNumber;
	}
	
}
