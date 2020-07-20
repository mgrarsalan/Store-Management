/**
 * 
 */
package utilites;

import java.awt.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.*;

/**
 * A class for parsing an INI file (in this case: lang.ini)
 * @author Arsalan Jafari
 *
 */
public class INIParsing {
	private String language; //Will determine whether the chosen language is Persian (Fa) or English (En)
	private final String sectionData; //Will hold all the data in a certain section in lang.ini
	public static String getCurrentLanguageString() throws IOException
	{
		String langContent = 
				Files.readString(Paths.get("Resources\\lang.ini"));
		return (langContent.charAt(6) == '0') ? "En" : "Fa";
		
	}
	public String getLanguage()
	{
		return this.language;
	}
	public void setLanguage(String language,Object className)
	{
		this.language = language;
		
	}
	
	/**
	 * When you want to set texts of all components in a frame.
	 * this overloaded version which has language parameter <b> is for changing the language</b>
	 * @param className which holds the name of the class from an object class
	 * @param language Fa for persian, En for English
	 * @throws IOException 
	 */
	public INIParsing(String className, String language) throws IOException
	{
		String langContent = 
				Files.readString(Paths.get("Resources\\lang.ini")); //has all the contents in lang.ini
		
		this.language = language;
		//Saving new language prefrences:
		Utility.replacingDataToFile("Resources\\lang.ini", 6, (this.language == "En") ? "0" : "1");
		int sectionIndex = 
				langContent.indexOf("[" +
						className + this.language +"]"); //index of the section which correspond to chosen class (form)
				sectionData =
						langContent.substring(sectionIndex,langContent.indexOf("[",sectionIndex + 1));
		
	}
	
	/** <b> used for read only purposes </b>
	 * @param className
	 * @throws IOException
	 */
	public INIParsing(String className) throws IOException
	{
		String langContent = 
				Files.readString(Paths.get("Resources\\lang.ini")); //has all the contents in lang.ini
		
		this.language = (langContent.charAt(6) == '0') ? "En" : "Fa"; // Read the preferred setting for language from the file
		int sectionIndex = 
				langContent.indexOf("[" +
						className + this.language +"]"); //index of the section which correspond to chosen class (form)
				sectionData =
						langContent.substring(sectionIndex,langContent.indexOf("[",sectionIndex + 1));
	}
	/**
	 * @param componentName The name of the object which its text is subject to change
	 * @return The text of the component based on prefrences (English or Persian)
	 */
	public String getComponentText(String componentName)
	{
		int indexOfComp = 
				sectionData.indexOf(componentName);
		return sectionData.substring(indexOfComp + componentName.length() + 1 // + 1 is for '='
				,sectionData.indexOf("\n",indexOfComp));
	}
	
	
}
