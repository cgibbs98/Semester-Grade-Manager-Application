package application;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class PointsNeeded extends JDialog{

	public JTextField pointsField = new JTextField("");
	public JLabel needLabel = new JLabel("");
	
	public PointsNeeded(){
	
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
		JLabel instructionsLabel = new JLabel("Type in the overall grade to know how much points you'll need to achieve it in this class.");
		JPanel fieldPanel = new JPanel();
		JLabel fieldLabel = new JLabel("Overall grade: ");
		pane.add(needLabel, BorderLayout.WEST);
		
		pointsField.setPreferredSize(new Dimension(20, pointsField.getPreferredSize().height));
		pointsField.setHorizontalAlignment(SwingConstants.RIGHT);
		fieldPanel.add(instructionsLabel);
		fieldPanel.add(new JLabel("     "));
		fieldPanel.add(fieldLabel);
		fieldPanel.add(pointsField);
		pane.add(fieldPanel, BorderLayout.NORTH);
		JPanel buttonPanel = new JPanel();
		JButton calculateButton = new JButton("Calculate Points Needed");
		JButton closeButton = new JButton("Close Window");
		buttonPanel.add(calculateButton);
		buttonPanel.add(closeButton);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		
		//Action Listeners
		calculateButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				calculatePoints();
			}  
		});
		closeButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				pointsField.setText("");
				dispose();
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				pointsField.setText("");
				dispose();
		}
		});
		
	}//End of PointsNeeded
	
	public void calculatePoints(){
		
		//Checks if input is a number and throw error message if not a number
		if(utilities.Misc.isANumber(pointsField.getText())){
			
			//Iterate loop until grade is calculated or an empty file is found
			double grade = 0.0;
			double known = 0.0;
			double unknown = 0.0;
			DefaultTableModel valueModel = (DefaultTableModel) AppFrame.valueTable.getModel();
			for(int i = 0; i < AppFrame.categoryList.size(); i++){
				
				//If current category is loaded already
				if(i == AppFrame.categoryCombo.getSelectedIndex()){
					if(valueModel.getValueAt(6, 2).equals("N/A")){
						unknown += Double.parseDouble(AppFrame.categoryList.get(i).getWeight());
					}//End of if
					else{
						known += Double.parseDouble(valueModel.getValueAt(6, 2).toString());
					}//End of else
				}//End of if
				
				//If current category needs to be opened
				else{
					
					try {
						
						//If file is empty
						Scanner sc = new Scanner(new File(AppFrame.categoryList.get(i).getFilePath()));
						if(!(sc.hasNext())){
							sc.close();
							unknown += Double.parseDouble(AppFrame.categoryList.get(i).getWeight());
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
							double weighted = percent*Double.parseDouble(AppFrame.categoryList.get(i).getWeight());
							known += weighted;
						}//End of else
						sc.close();
						
					} catch (Exception e) {
						
					}//End of try catch
					
				}//End of else
				
			}//End of for
			
			//Calculate grade needed
			known += Double.parseDouble(AppFrame.scaleField.getText());
			grade = (Double.parseDouble(pointsField.getText())-known)/unknown;
			needLabel.setText("  " + "Percentage of points needed in categories without grades to get the above grade in course: " + grade*100 + "%");
			
		}//End of if
		else{
			utilities.Misc.errorMessage("Cannot calculate grade(s) needed, Value is blank or not a number.");
		}//End of else
		
	}//End of calculatePoints
	
	public static void main(String[] args){
		
		//Launch new instance of frame
		PointsNeeded frame = new PointsNeeded();
	    frame.setVisible(true);
	    
	}//End of main

}//End of PointsNeeded
