package utilities;

import javax.swing.*;

public class misc{

	public static void errorMessage(String message){
		
		//Create error message to user
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
		
	}//End of error message
	
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
		
	}//End of error message

}//End of misc