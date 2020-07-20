package store.forms;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;

import utilites.INIParsing;
import utilites.Utility;
import utilites.XMLParsing;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;

import store.stock.BuyMorePayLess;
import store.stock.DiscountIndependence;
import store.stock.Item;
import store.stock.ItemTakeItAll;
import store.stock.ItemWithDiscount;

import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class ManagementForm {
	
	private JFrame frame;
	private INIParsing objIniParsing;
	private JTextField txtItemName;
	private JTextField txtPrice;
	private JLabel lblAddItem,lblPrice,lblItemName,lblType;
	private JRadioButton rdDiscount, rdDiscountIndependence,rdBMPL,rdTakeItAll;
	private ButtonGroup objButtonGroup; // a group of buttons for checking their status
	String type = "discount"; //type of the new item with default value of Discount
	private JButton btnAdd;
	public ManagementForm()
	{
		
		objButtonGroup = new ButtonGroup();
		try {
			objIniParsing = new INIParsing(this.getClass().getSimpleName());
		} catch (IOException e) {
			Utility.IOError(frame, "data.xml", true);
		}
		frame = new JFrame();
		frame.setIconImage(LoginForm.formLogo.getImage()); 
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		frame.setSize(350, 350);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		        try {
					Utility.exitDialog(frame);
				} catch (IOException e) {
					Utility.IOError(frame, "lang.ini", false);
				}
		        }
		    });
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		
		lblAddItem = new JLabel("dd");
		lblAddItem.setBounds(111, 47, 103, 13);
		frame.getContentPane().add(lblAddItem);
		
		lblItemName = new JLabel("New label");
		frame.getContentPane().add(lblItemName);
		
		txtItemName = new JTextField();
		frame.getContentPane().add(txtItemName);
		txtItemName.setColumns(10);
		
		lblPrice = new JLabel("New label");
		
		frame.getContentPane().add(lblPrice);
		
		txtPrice = new JTextField();
		
		frame.getContentPane().add(txtPrice);
		txtPrice.setColumns(10);
		
		lblType = new JLabel("New label");
		frame.getContentPane().add(lblType);
		
		rdDiscount = new JRadioButton("New radio button");
		rdDiscount.setSelected(true);
		rdDiscount.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					type = "discount";
			}
		});
		rdDiscount.setBounds(20, 175, 124, 21);
		frame.getContentPane().add(rdDiscount);
		
		rdDiscountIndependence = new JRadioButton("New radio button");
		rdDiscountIndependence.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					type = "discountIndependence";
			}
		});
		
		
		rdDiscountIndependence.setBounds(169, 175, 171, 21);
		frame.getContentPane().add(rdDiscountIndependence);
		
		rdBMPL = new JRadioButton("New radio button");
		rdBMPL.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					type = "BMPL";
			}
		});
		rdBMPL.setBounds(20, 209, 147, 21);
		frame.getContentPane().add(rdBMPL);
		
		rdTakeItAll = new JRadioButton("New radio button");
		rdTakeItAll.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					type = "ITIA";
			}
		});
		
		rdTakeItAll.setBounds(169, 209, 171, 21);
		frame.getContentPane().add(rdTakeItAll);
		objButtonGroup.add(rdBMPL);
		objButtonGroup.add(rdDiscount);
		objButtonGroup.add(rdDiscountIndependence);
		objButtonGroup.add(rdTakeItAll);
		
		btnAdd = new JButton();
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					addItem();
					JOptionPane.showMessageDialog(frame,
							objIniParsing.getComponentText("msgAdd"), objIniParsing.getComponentText("msgCaption"), JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e1) {
					Utility.IOError(frame, "data.xml", false);
				}
			}
		});
		btnAdd.setBounds(111, 254, 103, 30);
		frame.getContentPane().add(btnAdd);
		setComponentsText();
		frame.setVisible(true);
	}
	private void setComponentsText()
	{
		frame.setTitle(objIniParsing.getComponentText("frame"));
		lblAddItem.setText(objIniParsing.getComponentText("lblAddItem"));
		lblItemName.setText(objIniParsing.getComponentText("lblItemName"));
		lblPrice.setText(objIniParsing.getComponentText("lblPrice"));
		lblType.setText(objIniParsing.getComponentText("lblType"));
		rdDiscount.setText(objIniParsing.getComponentText("Discount"));
		rdBMPL.setText(objIniParsing.getComponentText("rdBMPL"));
		rdDiscountIndependence.setText(objIniParsing.getComponentText("rdDiscountIndependence"));
		rdTakeItAll.setText(objIniParsing.getComponentText("rdTakeItAll"));
		btnAdd.setText(objIniParsing.getComponentText("btnAdd"));
		if (objIniParsing.getLanguage().equals("En"))
		{
		lblItemName.setBounds(43, 78, 65, 13);
		lblPrice.setBounds(43, 116, 65, 13);
		txtItemName.setBounds(143, 75, 96, 19);
		lblType.setBounds(43, 156, 65, 13);
		txtPrice.setBounds(143, 113, 96, 19);
		}
		else if (objIniParsing.getLanguage().equals("Fa"))
		{
			lblItemName.setBounds(251, 78, 85, 13);
			lblPrice.setBounds(251, 116, 65, 13);
			txtItemName.setBounds(118, 75, 96, 19);
			lblType.setBounds(239, 156, 65, 13);
			txtPrice.setBounds(118, 113, 96, 19);
		}
	}
	
	private void addItem() throws IOException
	{//XXX
		String name = txtItemName.getText();
		float price = Float.parseFloat(txtPrice.getText());
		Item addItem;
		if (rdDiscountIndependence.isSelected() == true)
		{
			addItem = new DiscountIndependence(name,price);
			XMLParsing.addItem(addItem, "discountIndependence");
		}
		else if (rdBMPL.isSelected() == true) {
			addItem = new BuyMorePayLess(name,price);
			XMLParsing.addItem(addItem, "BMPL");
		}
		else if (rdDiscount.isSelected() == true)
		{
			addItem = new ItemWithDiscount(name,price);
			XMLParsing.addItem(addItem, "discount");
		}
		else {
			addItem = new ItemTakeItAll(name,price);
			XMLParsing.addItem(addItem, "ITIA");
		}
		
	}
	
}