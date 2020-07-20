/**
 * 
 */
package utilites;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * A class full of static methods for common used mechanisms to avoid code duplication and readability
 * @author Arsalan Jafari
 *
 */
public class Utility {
	
	
	public static void insertDataToFile(String filename,int offset,String content) throws IOException 
	{
		  RandomAccessFile r = new RandomAccessFile(new File(filename), "rw");
		  RandomAccessFile rtemp = new RandomAccessFile(new File(filename + "~"), "rw");
		  long fileSize = r.length();
		  FileChannel sourceChannel = r.getChannel();
		  FileChannel targetChannel = rtemp.getChannel();
		  sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
		  sourceChannel.truncate(offset);
		  r.seek(offset);
		  r.writeBytes(content);
		  long newOffset = r.getFilePointer();
		  targetChannel.position(0L);
		  sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
		  sourceChannel.close();
		  targetChannel.close();
	}
	public static void replacingDataToFile(String filename,int offset,String content) throws IOException
	{
		String originalString = Files.readString(Paths.get(filename));
		String beforeContent = originalString.substring(0, offset);
		String newContentString = 
				beforeContent +
				content +
				originalString.substring(offset + content.length());
		Writer out = new BufferedWriter(new OutputStreamWriter(
			    new FileOutputStream(filename), "UTF-8"));
			try {
			    out.write(newContentString);
			} finally {
			    out.close();
			}
	}
	/**
	 * Showing a MessageDialog to prompt exiting the program
	 * @param frame The frame which exit request is given
	 * @throws IOException
	 */
	public static void exitDialog(JFrame frame) throws IOException
	{
		INIParsing objIniParsing = new INIParsing("Utility");
		String message = 
				objIniParsing.getComponentText("exitMessage");
		String Caption = 
				objIniParsing.getComponentText("exitCaption");
		 if (JOptionPane.showConfirmDialog(frame, 
		            message, Caption, 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            System.exit(0);
		        }
	}
	/**
	 * showing a fatal error and closing the program because of I/O problems
	 * @param frame The gui frame which I/O error happend in it
	 * @param fileName The file which I/O error is associated with
	 * @param fatalError is it a fatal error? if it is then app must be terminated
	 */
	public static void IOError(JFrame frame,String fileName, boolean fatalError)
	{
		JOptionPane.showMessageDialog(frame,
				"I/O ERROR AT " + fileName + " : PLEASE RE-INSTALL THE PROGRAM","ERROR",JOptionPane.ERROR_MESSAGE);
		if (fatalError)
			System.exit(1);
	}
	
	/**
	 * A simple method to dispose and dissapear a frame (form)
	 * @param frame
	 */
	public static void closeForm(JFrame frame)
	{
		frame.dispose(); // Telling JVM to dispose the frame
		frame.setVisible(false);
	}
}
