package store.forms;

import java.awt.Component;
import java.io.IOException;

import javax.swing.*;
import utilites.INIParsing;
import utilites.Utility;
import utilites.XMLParsing;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The opening form of the program when the manager enters his/her pin to start the program
 *  
 *
 */
public final class PinForm {
	private JFrame frame = new JFrame();
	private JTextField txtPin;
	private static JLabel lblPin;
	private JButton btnStart; // one of the main reason we didn't declare main in here is to prevent getting these component
	// static access modifier thus having a better memory efficient program
	private INIParsing objIniParsing;
	private JRadioButton rdbtnEnglish;
	private JRadioButton rdbtnPersian;
	public PinForm()
	{
		
		frame.setIconImage(LoginForm.formLogo.getImage()); 
		try {
			objIniParsing = new INIParsing(this.getClass().getSimpleName()); //read only
		} catch (IOException e) {
			Utility.IOError(frame,"data.xml",true);
		}
		
		
		frame.setSize(350,350);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		lblPin = new JLabel();
		lblPin.setBounds(69, 74, 205, 13);
		frame.getContentPane().add(lblPin);
		
		txtPin = new JTextField();
		txtPin.setBounds(69, 134, 96, 19);
		frame.getContentPane().add(txtPin);
		txtPin.setColumns(10);
		rdbtnEnglish = new JRadioButton("English / \u0627\u0646\u06AF\u0644\u06CC\u0633\u06CC");
		
		rdbtnEnglish.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					if (e.getStateChange() == ItemEvent.SELECTED) //English is chosen
						objIniParsing = new INIParsing("PinForm","En"); //reading and choosing English
					setComponentsText();
					rdbtnPersian.setSelected(!rdbtnEnglish.isSelected()); //swapping radiobutton status
				} catch (IOException e1) {
					Utility.IOError(frame,"lang.ini",true);
				}
			}
		});
		rdbtnPersian = new JRadioButton("Persian / \u0641\u0627\u0631\u0633\u06CC");
		rdbtnPersian.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					try {
						objIniParsing = new INIParsing("PinForm","Fa");
					} catch (IOException e1) {
						Utility.IOError(frame,"lang.ini",true);
					}
				setComponentsText();
				rdbtnEnglish.setSelected(!rdbtnPersian.isSelected()); //swapping radiobutton status
			}
		});
		btnStart = new JButton();
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					checkPin();
				} catch (IOException e1) {
					Utility.IOError(frame,"lang.ini",true);
				}
			}
		});
		btnStart.setBounds(69, 190, 113, 21);
		frame.getContentPane().add(btnStart);
		
		JLabel lblNewLabel = new JLabel("Language / \u0632\u0628\u0627\u0646:");
		lblNewLabel.setBounds(10, 276, 96, 13);
		frame.getContentPane().add(lblNewLabel);
		
		
		rdbtnEnglish.setBounds(104, 272, 120, 21);
		frame.getContentPane().add(rdbtnEnglish);
		
		
		rdbtnPersian.setBounds(226, 272, 120, 21);
		
		frame.getContentPane().add(rdbtnPersian);
		setComponentsText(); //setting components texts
		
		//Actions when user try to close the program:
		frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		        try {
					Utility.exitDialog(frame);
				} catch (IOException e) {
					Utility.IOError(frame, "lang.ini", false); //not a fatal error
				}
		        }
		    });
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(false);
		frame.setVisible(true);
		
	}
	private void setComponentsText() 
	{
			frame.setTitle(objIniParsing.getComponentText("frame"));
			btnStart.setText(objIniParsing.getComponentText("btnStart"));
			lblPin.setText(objIniParsing.getComponentText("lblPin"));
			//choosing which radiobutton is selected in default
			rdbtnEnglish.setSelected((objIniParsing.getLanguage() == "En"));
			rdbtnPersian.setSelected(!rdbtnEnglish.isSelected());
	}
	private void checkPin() throws IOException
	{
		if (XMLParsing.getPin().equals(txtPin.getText()))
		{
			new LoginForm(); //calling the next form
			Utility.closeForm(frame);
		}
		else {
			String message =
					objIniParsing.getComponentText("wrongPin");
			String caption = 
					objIniParsing.getComponentText("wrongPinCaption");
			JOptionPane.showMessageDialog(frame, message, caption, JOptionPane.ERROR_MESSAGE);

		}
	}
	
}
