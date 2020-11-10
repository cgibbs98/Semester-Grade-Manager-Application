package utilities;

import java.awt.*;
import javax.swing.*;

public class misc{

	public static void errorMessage(String message){
		
		//Create error message to user
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
		
	}//End of errorMessage
	
	public static int savePrompt(){
		
		//Create prompt for user to save
		int result = JOptionPane.showConfirmDialog((Component) null, "You have unsaved data. Do you want to save grades before proceeding?", "Unsaved Data", JOptionPane.YES_NO_CANCEL_OPTION);
	    return result;
		
	}//End of savePrompt
	
	public static boolean isANumber(String value){
		
		//Return false if an error occurs, otherwise true
		boolean b = true;
		double v = 0;
		try {
			v = Double.parseDouble(value);
		} catch (Exception e) {
			return b = false;
		}//End of try catch
		return b;
		
	}//End of isANumber

}//End of misc