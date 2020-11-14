package application;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class pointsneeded extends JDialog{

	public JTextField pointsfield = new JTextField("");
	public JLabel needlabel = new JLabel("");
	
	public pointsneeded(){
	
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/2);
		int h = (int) (screenSize.getHeight()/7);
		setSize(w, h);
				
		//Properties
		setTitle("Points Needed Calculator");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		//Components
		Container pane = getContentPane();
		JLabel instructionslabel = new JLabel("Type in the overall grade to know how much points you'll need to achieve it in this class.");
		JPanel fieldpanel = new JPanel();
		JLabel fieldlabel = new JLabel("Overall grade: ");
		pane.add(needlabel, BorderLayout.WEST);
		
		pointsfield.setPreferredSize(new Dimension(20, pointsfield.getPreferredSize().height));
		pointsfield.setHorizontalAlignment(SwingConstants.RIGHT);
		fieldpanel.add(instructionslabel);
		fieldpanel.add(new JLabel("     "));
		fieldpanel.add(fieldlabel);
		fieldpanel.add(pointsfield);
		pane.add(fieldpanel, BorderLayout.NORTH);
		JPanel buttonpanel = new JPanel();
		JButton calculatebutton = new JButton("Calculate Points Needed");
		JButton closebutton = new JButton("Close Window");
		buttonpanel.add(calculatebutton);
		buttonpanel.add(closebutton);
		pane.add(buttonpanel, BorderLayout.SOUTH);
		
		//Action Listeners
		calculatebutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				calculatePoints();
			}  
		});
		closebutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				pointsfield.setText("");
				dispose();
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				pointsfield.setText("");
				dispose();
		}
		});
		
	}//End of points needed
	
	public void calculatePoints(){
		
		//Checks if input is a number and throw error message if not a number
		if(utilities.misc.isANumber(pointsfield.getText())){
			
			//Iterate loop until grade is calculated or an empty file is found
			double grade = 0.0;
			double known = 0.0;
			double unknown = 0.0;
			DefaultTableModel valuemodel = (DefaultTableModel) appframe.valuetable.getModel();
			for(int i = 0; i < appframe.categorylist.size(); i++){
				
				//If current category is loaded already
				if(i == appframe.categorycombo.getSelectedIndex()){
					if(valuemodel.getValueAt(6, 2).equals("N/A")){
						unknown += Double.parseDouble(appframe.categorylist.get(i).getWeight());
					}//End of if
					else{
						known += Double.parseDouble(valuemodel.getValueAt(6, 2).toString());
					}//End of else
				}//End of if
				
				//If current category needs to be opened
				else{
					
					try {
						
						//If file is empty
						Scanner sc = new Scanner(new File(appframe.categorylist.get(i).getFilepath()));
						if(!(sc.hasNext())){
							sc.close();
							unknown += Double.parseDouble(appframe.categorylist.get(i).getWeight());
						}//End of if
						
						//If file has atleast 1 grade
						else{
							double earned = 0.0;
							double possible = 0.0;
							while(sc.hasNext()){
								String[] graderow = sc.nextLine().split(",");
								earned += Double.parseDouble(graderow[1]);
								possible += Double.parseDouble(graderow[2]);
							}//End of while
							double percent = earned/possible;
							double weighted = percent*Double.parseDouble(appframe.categorylist.get(i).getWeight());
							known += weighted;
						}//End of else
						sc.close();
						
					} catch (Exception e) {
						
					}//End of try catch
					
				}//End of else
				
			}//End of for
			
			//Calculate grade needed
			known += Double.parseDouble(appframe.scalefield.getText());
			grade = (Double.parseDouble(pointsfield.getText())-known)/unknown;
			needlabel.setText("  " + "Percentage of points needed in categories without grades to get the above grade in course: " + grade*100 + "%");
			
		}//End of if
		else{
			utilities.misc.errorMessage("Cannot calculate grade(s) needed, Value is blank or not a number.");
		}//End of else
		
	}//End of calculatePoints
	
	public static void main(String[] args){
		
		//Launch new instance of frame
		pointsneeded frame = new pointsneeded();
	    frame.setVisible(true);
	    
	}//End of main

}//End of pointsneeded
