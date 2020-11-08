package application;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;



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
		
		//Prevent interactions with main app frame
		appframe.gradestable.setEnabled(false);
		appframe.categorycombo.setEnabled(false);
		appframe.classcombo.setEnabled(false);
		appframe.addbutton.setEnabled(false);
		appframe.removebutton.setEnabled(false);
		appframe.updatebutton.setEnabled(false);
		appframe.needbutton.setEnabled(false);
		appframe.scalefield.setEnabled(false);
		
		//Action Listeners
		yesbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				frameactions.updateAndSave();
				dispose();
				appframe.savecheck = true;
				//Reallow interactions with main app frame
				appframe.gradestable.setEnabled(true);
				appframe.categorycombo.setEnabled(true);
				appframe.classcombo.setEnabled(true);
				appframe.addbutton.setEnabled(true);
				appframe.removebutton.setEnabled(true);
				appframe.updatebutton.setEnabled(true);
				DefaultTableModel valuemodel = (DefaultTableModel) appframe.valuetable.getModel();
				if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
					appframe.needbutton.setEnabled(true);
				}//End of if
				appframe.scalefield.setEnabled(true);
				performAction(code);
			}  
		});
		nobutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				dispose();
				appframe.savecheck = true;
				//Reallow interactions with main app frame
				appframe.gradestable.setEnabled(true);
				appframe.categorycombo.setEnabled(true);
				appframe.classcombo.setEnabled(true);
				appframe.addbutton.setEnabled(true);
				appframe.removebutton.setEnabled(true);
				appframe.updatebutton.setEnabled(true);
				DefaultTableModel valuemodel = (DefaultTableModel) appframe.valuetable.getModel();
				if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
					appframe.needbutton.setEnabled(true);
				}//End of if
				appframe.scalefield.setEnabled(true);
				performAction(code);
			}  
		});
		cancelbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				dispose();
				appframe.savecheck = true;
				//Reallow interactions with main app frame
				appframe.gradestable.setEnabled(true);
				appframe.categorycombo.setEnabled(true);
				appframe.classcombo.setEnabled(true);
				appframe.addbutton.setEnabled(true);
				appframe.removebutton.setEnabled(true);
				appframe.updatebutton.setEnabled(true);
				DefaultTableModel valuemodel = (DefaultTableModel) appframe.valuetable.getModel();
				if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
					appframe.needbutton.setEnabled(true);
				}//End of if
				appframe.scalefield.setEnabled(true);
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				appframe.savecheck = true;
				//Reallow interactions with main app frame
				appframe.gradestable.setEnabled(true);
				appframe.categorycombo.setEnabled(true);
				appframe.classcombo.setEnabled(true);
				appframe.addbutton.setEnabled(true);
				appframe.removebutton.setEnabled(true);
				appframe.updatebutton.setEnabled(true);
				DefaultTableModel valuemodel = (DefaultTableModel) appframe.valuetable.getModel();
				if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
					appframe.needbutton.setEnabled(true);
				}//End of if
				appframe.scalefield.setEnabled(true);
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
