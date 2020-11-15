package startup;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

public class CreateNew extends JFrame{
	
	public static Box centerBox = Box.createVerticalBox();
	public static JTextField createField = new JTextField("");
	public static int indexHold = 0;
	public static int classCount = 0;
	
	public CreateNew(){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/2.125);
		int h = (int) (screenSize.getHeight()/3.875);
		setSize(w, h);
		
		//Properties
		setTitle("Semester Grade Manager Application - Create New Semester");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//Create Course
		Container pane = getContentPane();
		JPanel createPanel = new JPanel();
		JLabel createLabel = new JLabel("Semester Name: ");
		createField.setPreferredSize(new Dimension(150, createField.getPreferredSize().height));
		JButton createButton = new JButton("Create New Semester");
		createPanel.add(createLabel);
		createPanel.add(createField);
		createPanel.add(createButton);
		pane.add(createPanel, BorderLayout.NORTH);
		
		JScrollPane centerPane = new JScrollPane(centerBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    centerPane.setPreferredSize(new Dimension(centerBox.getPreferredSize().width+17, centerBox.getPreferredSize().height));
	    pane.add(centerPane, BorderLayout.CENTER);
	    NewActions.newClassPanel(true);
		
		//Button Actions
		JPanel actionsPanel = new JPanel();
		JButton addCategoryButton = new JButton("Add New Category");
		JButton addClassButton = new JButton("Add New Class");
		JButton deleteCategoryButton = new JButton("Delete Last Category");
		JButton deleteClassButton = new JButton("Delete Last Class");
		actionsPanel.add(addCategoryButton);
		actionsPanel.add(addClassButton);
		actionsPanel.add(deleteCategoryButton);
		actionsPanel.add(deleteClassButton);
		pane.add(actionsPanel, BorderLayout.SOUTH);
		
		//Action Listeners
		addClassButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				NewActions.newClassPanel(false);
			}  
		});
		addCategoryButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				NewActions.newCategoryPanel(false);
			}  
		});
		deleteClassButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				NewActions.deleteLastClass();
			}  
		});
		deleteCategoryButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				NewActions.deleteLastCategory();
			}  
		});
		createButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				FileActions.saveSemester();
				String name = FileActions.getFileName();
				if(name != null){
					dispose();
					application.AppFrame.main(name, true);
				}//End of if
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	File relaunchFile = new File("relaunch.txt");
		    	try {relaunchFile.createNewFile();} catch (Exception ex) {}
            	NewActions.removeAllComponents();
            	createField.setText("");
            	dispose();
            	Launch.main(null);
            }
        });
		
	}//End of CreateNew
	
	public static void main(String[] args) {
		
		//Launch new instance of frame
		CreateNew frame = new CreateNew();
		frame.setVisible(true);

	}//End of main

}//End of CreateNew
