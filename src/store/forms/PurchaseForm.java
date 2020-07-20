package store.forms;

import java.awt.BorderLayout;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import store.stock.*;
import store.user.*;
import utilites.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class PurchaseForm {
	private INIParsing objIniParsing;
	private JFrame frame = new JFrame();
	private Icon addIcon = new ImageIcon("Resources\\add.png");
	private Icon removeIcon = new ImageIcon("Resources\\remove.png");
	private Icon newBasketIcon = new ImageIcon("Resources\\new.png");
	private Icon printIcon = new ImageIcon("Resources\\print.png");
	private JButton removeBtn,btnNew,addBtn,btnPrint,btnPay,btnBasketHistory,btnUserInfo;
	private JList basketList;
	private DefaultListModel stockListModel,basketListModel;
	private JScrollPane stockScrollPane,basketJScrollPane;
	private JPanel panel = new JPanel(new BorderLayout());
	private JPanel basketPanel = new JPanel(new BorderLayout());
	private JList stockList;
	private Cashier buyerCashier;
	private JLabel lblAdd,lblRemove;
	private JLabel lblNew;	
	private JLabel lblPrint;
	private JLabel lblTime;
	private JLabel lblTimer;
	private JCheckBox chkXmass;
	private User currentCustomer; //The customer who cashier is buying for
	private byte hour = 0 ,minutes = 0;
	private boolean isLogOn = false; //checking whether the manager has logged on
	StoreBasket currentBasket;
	public PurchaseForm(String userName)
	{
		
		frame.setIconImage(LoginForm.formLogo.getImage()); 
		
		try {
			objIniParsing = new INIParsing(this.getClass().getSimpleName());
			buyerCashier = new Cashier(userName);
			} catch (IOException e) {
				Utility.IOError(frame, "lang.ini", false); //not fatal
			}
		catch (Exception e) {
			Utility.IOError(frame, "data.xml", true); // fatal error related to user
		}
		
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		       exitingForm();
		    }});
		
		frame.getContentPane().setLayout(null);
		
		addBtn = new JButton(addIcon);
		frame.getContentPane().add(addBtn);
		removeBtn = new JButton(removeIcon);
		frame.getContentPane().add(removeBtn);
		
		stockListModel = new DefaultListModel();
		stockListModel.addAll(listStockList()); // Add all items from data.xml stock tag
		stockList = new JList(stockListModel);
		stockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		stockList.setBounds(10, 110, 177, 179);
		stockScrollPane = new JScrollPane();
		stockScrollPane.setViewportView(stockList);
		stockList.setLayoutOrientation(JList.VERTICAL);
		panel.setBounds(10, 110, 177, 179);
		panel.add(stockScrollPane);
		frame.getContentPane().add(panel);
		
		basketListModel = new DefaultListModel();
		basketJScrollPane = new JScrollPane();
		basketList = new JList(basketListModel);
		basketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		basketJScrollPane.setViewportView(basketList);
		basketList.setBounds(346, 110, 177, 179);
		basketList.setLayoutOrientation(JList.VERTICAL);
		basketPanel.setBounds(346, 110, 177, 179);
		basketPanel.add(basketJScrollPane);
		frame.getContentPane().add(basketPanel);
		
		frame.setResizable(false);
		frame.setSize(550, 550);
		frame.setLocationRelativeTo(null); //making it into center

		lblTime = new JLabel();
		frame.getContentPane().add(lblTime);
		
		lblAdd = new JLabel();
		lblRemove = new JLabel();
		frame.getContentPane().add(lblRemove);

		lblNew = new JLabel();
		frame.getContentPane().add(lblAdd);
		
		btnNew = new JButton(newBasketIcon);
		frame.getContentPane().add(btnNew);
		
		frame.getContentPane().add(lblNew);
		
		btnPrint = new JButton(printIcon);
		frame.getContentPane().add(btnPrint);
		
		lblPrint = new JLabel();
		
		frame.getContentPane().add(lblPrint);
		
		chkXmass = new JCheckBox();
		chkXmass.setBounds(356, 295, 167, 21);
		frame.getContentPane().add(chkXmass);
		
		
		btnPay = new JButton();
		btnPay.setBounds(346, 322, 177, 21);
		frame.getContentPane().add(btnPay);
		
		lblTimer = new JLabel();
		lblTimer.setBounds(250, 501, 45, 13);
		frame.getContentPane().add(lblTimer);
		lblTimer.setText("00:00");
		
		btnBasketHistory = new JButton();
		btnBasketHistory.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
				 String fileName = "log" + LocalDateTime.now().format(formatter).toString() + ".txt"; //file name which includes time
				try {
					Store historyStore = new Store();
					PrintWriter log = new PrintWriter(fileName);
					log.print(historyStore.getBasketHistory());
					log.close();
					File file = new File(fileName);
					Desktop.getDesktop().open(file); //opening file from native operation system
				} catch (IOException e1) {
					Utility.IOError(frame, fileName, false);
				}
				
			}
		});
		btnBasketHistory.setBounds(10, 322, 215, 21);
		frame.getContentPane().add(btnBasketHistory);
		
		btnUserInfo = new JButton();
		btnUserInfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				JOptionPane.showMessageDialog(frame, currentCustomer.printInfo(), objIniParsing.getComponentText("btnUserInfo"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnUserInfo.setBounds(10, 295, 215, 21);
		frame.getContentPane().add(btnUserInfo);

		Timer TimeTimer = new Timer(60000, new ActionListener() { //60000 mil = 1 minute
			
			@Override
			public void actionPerformed(ActionEvent e) {
				minutes++;
				if (minutes == 60)
				{
					minutes = 0;
					hour++;
				}
				lblTimer.setText((hour > 9 ? hour : "0" + hour) //for controlling 00:00 format
						+ ":" + (minutes > 9 ? minutes : "0" + minutes));
			}
		});
	    TimeTimer.setRepeats(true);
	    TimeTimer.start();
	    
		setComponentsText();
		
		frame.setVisible(true);
		
		reserveBasket();
		try {
			currentBasket = new StoreBasket(buyerCashier,currentCustomer,setVAT());
		} catch (IOException e2) {
			Utility.IOError(frame, "data.xml", true);
		}
		addBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				buyItem();
			}
		});
		removeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				/*Interesting: at first i swapped these two lines and it got me in a trouble
				/ because getSelectedValue changes before getting removed;
				 */
				currentBasket.removeItem((Item)(basketList.getSelectedValue()));
				basketListModel.removeElement(basketList.getSelectedValue());
				
			}
		});
		btnNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				freshBasket();
			}
		});
		btnPay.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					currentBasket.setXmassOff(chkXmass.isSelected()); //setting the Xmass off status
					currentBasket.saveBasket();
					freshBasket();
					// or assaign basket in reverse basket. currentbasket = new ....
					
				} catch (IOException e1) {
					Utility.IOError(frame, "data.xml", true);
				}
				catch (IllegalArgumentException e2) { //if the user purchased more than 10 items
					JOptionPane.showMessageDialog(frame, 
							objIniParsing.getComponentText("msgExceeded"),
							objIniParsing.getComponentText("msgError"),JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		btnPrint.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				int ID = Integer.parseInt(
						JOptionPane.showInputDialog(frame,objIniParsing.getComponentText("msgBasketID"),
								objIniParsing.getComponentText("msgIDCaption"), JOptionPane.QUESTION_MESSAGE));
				///XXX if (id > 0 && id < getlastbasketID)
				try {
					JOptionPane.showMessageDialog(frame,
							StoreBasket.printInformation(ID),
							objIniParsing.getComponentText("msgIDCaption"), JOptionPane.QUESTION_MESSAGE);
				} catch (IOException e1) {
					Utility.IOError(frame, "data.xml", false);
				}
			}
		});
		
	}
	private void setComponentsText()
	{
		frame.setTitle(objIniParsing.getComponentText("frame"));
		lblAdd.setText(objIniParsing.getComponentText("addBtn"));
		lblRemove.setText(objIniParsing.getComponentText("removeBtn"));
		lblNew.setText(objIniParsing.getComponentText("lblNew"));
		lblPrint.setText(objIniParsing.getComponentText("lblPrint"));
		btnPay.setText(objIniParsing.getComponentText("btnPay"));
		lblTime.setText(objIniParsing.getComponentText("lblTime"));
		chkXmass.setText(objIniParsing.getComponentText("chkXmass"));
		btnUserInfo.setText(objIniParsing.getComponentText("btnUserInfo"));
		btnBasketHistory.setText(objIniParsing.getComponentText("btnBasketHistory"));
		//For alignment of the components (RTL or LTR)
		if (objIniParsing.getLanguage() == "En") 
		{
			addBtn.setBounds(194, 110, 35, 35);
			removeBtn.setBounds(194, 155, 35, 35);
			lblAdd.setBounds(239, 122, 93, 13);
			lblRemove.setBounds(239, 165, 81, 13);
			lblNew.setBounds(239, 210, 81, 13);
			btnNew.setBounds(194, 200, 35, 35);
			lblPrint.setBounds(239, 258, 105, 13);
			btnPrint.setBounds(194, 245, 35, 35);
			lblTime.setBounds(10, 501, 125, 13);
		}
		else {
			lblAdd.setBounds(208, 121, 93, 13);
			lblRemove.setBounds(237, 166, 64, 13);
			addBtn.setBounds(301, 110, 35, 35);
			removeBtn.setBounds(301, 155, 35, 35);
			btnNew.setBounds(301, 198, 35, 35);
			lblNew.setBounds(255, 209, 52, 13);
			lblRemove.setBounds(239, 165, 81, 13);
			lblPrint.setBounds(255, 253, 52, 13);
			btnPrint.setBounds(301, 243, 35, 35);
			lblTime.setBounds(418, 501, 125, 13);
			
		}
		
	}
	/**
	 * @return an array list containing all items available in the stock
	 */
	private ArrayList<Item> listStockList()
	{
		ArrayList<Item> listOfItems = new ArrayList<Item>();
		try {
			int lastID = 
					XMLParsing.getLastItemID(); //listing from ID = 1 till the last ID saved
			XMLParsing itemParsing;
			Item stockItem;
			//XXX
			for (int i = 0; i <= lastID; i++) {
				itemParsing = new XMLParsing(i,"item");
				switch (itemParsing.getItemType()) {
				case 1:
					stockItem = new ItemWithDiscount(i);
					break;
				case 2:
					stockItem = new DiscountIndependence(i);
					break;
				case 3:
					stockItem = new BuyMorePayLess(i);
					break;
				case 4:
				default:
					stockItem = new ItemTakeItAll(i);
					break;
				}
				listOfItems.add(stockItem);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//an error indication file is missing. not fatal
			e.printStackTrace();
		}
		return listOfItems;
	}
	
	/**
	 * a method for reserving a basket for a user
	 * @return a user or any instance of its subclass object
	 */
	private void reserveBasket()
	{
		boolean loop = true; //to control the loop. this loop must persist till cashier enters a vaild user
		while (loop == true)
		{
		try {
			String message = 
					objIniParsing.getComponentText("reserveMessage");
			String title = 
					objIniParsing.getComponentText("reserveCaption");
			String userName = 
					JOptionPane.showInputDialog(frame, message , title, JOptionPane.QUESTION_MESSAGE);
			if (userName == null || (userName != null && "".equals(userName)))
				exitingForm(); //if user cancels the operation then it means closing the application
			if (userName.equals(buyerCashier.getUserName())) // checking if customer and cashier is the same
			{
				JOptionPane.showMessageDialog(frame,
						objIniParsing.getComponentText("msgSameCashier"),
						objIniParsing.getComponentText("msgError"),
						JOptionPane.ERROR_MESSAGE);
				throw new Exception();
			}
			XMLParsing objParsing = new XMLParsing(userName);
			// User should be created
			if (objParsing.getUserType().contains("Customer")) // if it's a customer.
			{
				currentCustomer = new Customer(userName);
			}
			else if (objParsing.getUserType().equals("Cashier")) {
				currentCustomer = new Customer(userName);
			}
			else {
				throw new IllegalArgumentException();
			}
			loop = false;
		} catch (IOException e) {
			Utility.IOError(frame, "data.xml", true);
		}
		catch (IllegalArgumentException e) { //invalid user
			JOptionPane.showMessageDialog(frame, 
					objIniParsing.getComponentText("msgInvalidUser"),
					objIniParsing.getComponentText("msgError"),JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e)
		{
			
		}
		}
	}
	
	/**
	 * The core method of purchasing the choosen item for the user
	 */
	private void buyItem()
	{
		Item purchasedItem = null; //Item that will end up in the basket
		while (isLogOn == false) //manager must log in
		{
			String username = 
					JOptionPane.showInputDialog(frame, objIniParsing.getComponentText("msgManagerLogin"),
					objIniParsing.getComponentText("msgCaption"), JOptionPane.QUESTION_MESSAGE);
			JPanel passPanel = new JPanel();
			JLabel label = new JLabel(objIniParsing.getComponentText("msgManagerPassword"));
			JPasswordField pass = new JPasswordField(10);
			passPanel.add(label);
			passPanel.add(pass);
			String[] options = new String[]{"OK", "Cancel"};
			int chosenValue = 
					JOptionPane.showOptionDialog(frame,
							passPanel, objIniParsing.getComponentText("msgCaption"),
							JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE,
							null,options, options[0]);
			if (chosenValue == 0)
			{
				if (Arrays.equals(pass.getPassword(),("admin".toCharArray())))
					isLogOn = true;
			}
		}
		Item selectedItem = (Item) stockList.getSelectedValue(); // selected item
		//if islogon (manger didn't get online) then log on and do these unless first logon
		//selectedItem must go through processes to become purchasedItem:
		// The order of if statements are important. they should be coherent with Class hireachy
		if (selectedItem instanceof ItemTakeItAll)
		{
			if (((ItemTakeItAll) selectedItem).getDiscount() == 0) //or -1
			{
				float discount = 
						Float.parseFloat(
						JOptionPane.showInputDialog(frame, objIniParsing.getComponentText("msgDiscount"),
								objIniParsing.getComponentText("msgCaption"), JOptionPane.QUESTION_MESSAGE)); //XXX checking and message changing
				((ItemTakeItAll) selectedItem).setDiscount(discount);
				byte np =
						Byte.parseByte(
						JOptionPane.showInputDialog("Enter discount")); //XXX checking and message changing
						((ItemTakeItAll) selectedItem).setNparameter(np);
			}
			purchasedItem = new ItemTakeItAll((ItemTakeItAll)selectedItem);
		}
		else if(selectedItem instanceof DiscountIndependence)
		{
			float discount = 
					Float.parseFloat(
							JOptionPane.showInputDialog(frame, objIniParsing.getComponentText("msgDiscount"),
									objIniParsing.getComponentText("msgCaption"), JOptionPane.QUESTION_MESSAGE)); //XXX checking and message changing
			float independence =
					Float.parseFloat(
						JOptionPane.showInputDialog(frame, objIniParsing.getComponentText("msgIndependence"),
						objIniParsing.getComponentText("msgCaption"), JOptionPane.QUESTION_MESSAGE)); //XXX checking and message changing
			purchasedItem = new DiscountIndependence(selectedItem,discount,independence);
		}
		else if (selectedItem instanceof ItemWithDiscount)
		{
			float discount = 
					Float.parseFloat(
							JOptionPane.showInputDialog(frame, objIniParsing.getComponentText("msgDiscount"),
									objIniParsing.getComponentText("msgCaption"), JOptionPane.QUESTION_MESSAGE));
			purchasedItem = new ItemWithDiscount(selectedItem,discount);
		}
		
		else if (selectedItem instanceof BuyMorePayLess) { //BMPL
			if (((BuyMorePayLess) selectedItem).getBuyCondition() == 0) 
				//setting the buy and bonus condition for this item and future alike items
			{
				byte buyCondition,payBonus;
				buyCondition =
						Byte.parseByte(
							JOptionPane.showInputDialog(frame, objIniParsing.getComponentText("msgBuyCondition"),
							objIniParsing.getComponentText("msgCaption"), JOptionPane.QUESTION_MESSAGE) 
						);
				((BuyMorePayLess) selectedItem).setBuyCondition(buyCondition);
				payBonus =
						Byte.parseByte(
						JOptionPane.showInputDialog(frame,objIniParsing.getComponentText("msgPayBonus"),
								objIniParsing.getComponentText("msgCaption"),JOptionPane.QUESTION_MESSAGE) 
						);
						((BuyMorePayLess) selectedItem).setPayBonus(payBonus);
			}
			purchasedItem = new BuyMorePayLess((BuyMorePayLess)selectedItem);
		}
		if (purchasedItem != null) //If user didn't cancel the purchase
		{
			basketListModel.addElement(purchasedItem);
			currentBasket.addItem(purchasedItem);
		}
		
	}
	private void freshBasket()
	{
		basketListModel.clear();
		currentBasket.clearItems();
		stockListModel.clear(); 
		stockListModel.addAll(listStockList()); //this and upper line
		// is for making fresh refrences to stock objects (therefore having fresh "settings")
		reserveBasket();
		currentBasket.setVAT(setVAT());
	}
	
	/**
	 * Asking VAT From user with GUI components
	 * @return VAT defined by the user
	 */
	private float setVAT()
	{
		float VAT = 0; //No VAT. default value
		while (true)
		{
		try {
			VAT = Float.parseFloat(
					JOptionPane.showInputDialog(frame,
					objIniParsing.getComponentText("msgVAT"), 
					objIniParsing.getComponentText("lblNew"), JOptionPane.QUESTION_MESSAGE)
					);
			if (VAT < 0)
				throw new Exception();
			break;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					objIniParsing.getComponentText("msgInvalidAmount"), 
					objIniParsing.getComponentText("msgError"), JOptionPane.ERROR_MESSAGE);
		}
		}
		return VAT;
		
	}
	
	/**
	 * when user wants to exit the form
	 */
	private void exitingForm()
	{
		 try {
				float hoursOfWork = hour +
						(float)minutes / 60;
				buyerCashier.setHoursOfWork(buyerCashier.getHoursOfWork() +
						hoursOfWork); //Adding the previous hoursofWork with current one
				Utility.exitDialog(frame);
			} catch (Exception e) {
				Utility.IOError(frame, "data.xml", false);
			}
	}
}
