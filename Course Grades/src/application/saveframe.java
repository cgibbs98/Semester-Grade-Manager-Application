package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class saveframe extends JFrame{

	public saveframe(int code){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/3.5);
		int h = (int) (screenSize.getHeight()/7.5);
		setSize(w, h);
		
		//Properties
		setTitle("Unsaved Data");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Components
		Container pane = getContentPane();
		JLabel savelabel = new JLabel("  " + "You may have unsaved data; Do you want to save current grades?");
		pane.add(savelabel, BorderLayout.NORTH);
		JPanel buttonspanel = new JPanel();
		JButton yesbutton = new JButton("Yes");
		JButton nobutton = new JButton("No");
		JButton cancelbutton = new JButton("Cancel");
		buttonspanel.add(yesbutton);
		buttonspanel.add(nobutton);
		buttonspanel.add(cancelbutton);
		pane.add(buttonspanel, BorderLayout.SOUTH);
		
		//Action Listeners
		yesbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				frameactions.updateAndSave();
				dispose();
				performAction(code);
			}  
		});
		nobutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				dispose();
				performAction(code);
			}  
		});
		cancelbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				dispose();
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
	}//End of aboutframe
	
	public void performAction(int code) {
		
		//Perform a possible that could happen after saving data
		
		//0 for quitting application
		if(code == 0){
			System.exit(0);
		}//End of if
		
	}//End of performAction
	
	public static void main(int code) {
		
		//Launch new instance of frame
		saveframe frame = new saveframe(code);
		frame.setVisible(true);

	}//End of main

}//End of saveframe
