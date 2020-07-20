package store.forms;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.*;

import utilites.INIParsing;
import utilites.Utility;
import utilites.XMLParsing;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class LoginForm { //declared all form classes final because of least privilege principle
	private JFrame frame = new JFrame();
	private JTextField txtUser;
	private JPasswordField txtPassword;
	private JLabel lblPassword;
	private JButton btnLogin;
	private JLabel lblUser;
	private JRadioButton rdbtnEnglish;
	private JRadioButton rdbtnPersian;
	private INIParsing objIniParsing;
	public static final ImageIcon formLogo = new ImageIcon("Resources\\logo.png"); //logo of the forms
	//declared as static and public so other forms can access to it
	public LoginForm()
	{
		
		frame.setIconImage(formLogo.getImage()); 
		
		try {
			objIniParsing = new INIParsing(this.getClass().getSimpleName());
		} catch (IOException e) {
			Utility.IOError(frame, "data.xml", true);
		}
		
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
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
		frame.getContentPane().setLayout(null);
		
		lblUser = new JLabel();
		lblUser.setBounds(156, 78, 118, 13);
		frame.getContentPane().add(lblUser);
		
		txtUser = new JTextField();
		txtUser.setBounds(134, 101, 96, 19);
		frame.getContentPane().add(txtUser);
		txtUser.setColumns(10);
		
		lblPassword = new JLabel();
		lblPassword.setBounds(156, 138, 79, 13);
		frame.getContentPane().add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(134, 161, 96, 19);
		frame.getContentPane().add(txtPassword);
		
		btnLogin = new JButton();
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				XMLParsing objParsing;
				try {
					objParsing = new XMLParsing(txtUser.getText());
					switch (objParsing.checkLogin(
									txtPassword.getPassword())) {
					case 0:
						throw new IllegalArgumentException();
					case 2: //then it's a cashier
						new PurchaseForm(txtUser.getText());
						Utility.closeForm(frame);
						break;
					case 3:
						new ManagementForm();
						Utility.closeForm(frame);
						break;
					default:
						//throw?
						break;
					}
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} ///XXX:check if it is not empty
				catch (Exception e2) {
					JOptionPane.showMessageDialog(frame, objIniParsing.getComponentText("msgInvalidLogon"),
							objIniParsing.getComponentText("btnLogin"),JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		btnLogin.setBounds(134, 250, 85, 21);
		
		rdbtnEnglish = new JRadioButton("English / \u0627\u0646\u06AF\u0644\u06CC\u0633\u06CC");
		rdbtnEnglish.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					if (e.getStateChange() == ItemEvent.SELECTED) //English is chosen
						objIniParsing = new INIParsing("LoginForm","En");
					setComponentsText();
					rdbtnPersian.setSelected(!rdbtnEnglish.isSelected());
					//setComponentsText();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		rdbtnPersian = new JRadioButton("Persian / \u0641\u0627\u0631\u0633\u06CC");
		rdbtnPersian.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					try {
						objIniParsing = new INIParsing("LoginForm","Fa"); //XXX:should this and upper method be inside another method?
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				setComponentsText();
				rdbtnEnglish.setSelected(!rdbtnPersian.isSelected());
			}
		});
		rdbtnEnglish.setBounds(110, 314, 120, 21);
		frame.getContentPane().add(rdbtnEnglish);
		rdbtnPersian.setBounds(239, 314, 120, 21);
		
		JLabel lblNewLabel = new JLabel("Language / \u0632\u0628\u0627\u0646:");
		lblNewLabel.setBounds(10, 318, 96, 13);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(rdbtnPersian);
		frame.getContentPane().add(btnLogin);
		
		frame.setResizable(false);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null); //making it into center
		setComponentsText();
		frame.setVisible(true);
	}
	
	private void setComponentsText()
	{
		frame.setTitle(objIniParsing.getComponentText("frame"));
		lblUser.setText(objIniParsing.getComponentText("lblUser"));
		lblPassword.setText(objIniParsing.getComponentText("lblPassword"));
		btnLogin.setText(objIniParsing.getComponentText("btnLogin"));
		//choosing which radiobutton is selected in default
		rdbtnEnglish.setSelected((objIniParsing.getLanguage() == "En"));
		rdbtnPersian.setSelected(!rdbtnEnglish.isSelected());
	}
}
