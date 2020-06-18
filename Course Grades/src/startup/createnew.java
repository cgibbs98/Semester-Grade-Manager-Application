package startup;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class createnew extends JFrame{
	
	public static Box centerbox = Box.createVerticalBox();
	public static JTextField createfield = new JTextField("");
	public static int indexhold = 0;
	public static int classcount = 0;
	
	public createnew(){
		
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
		JPanel createpanel = new JPanel();
		JLabel createlabel = new JLabel("Semester Name: ");
		createfield.setPreferredSize(new Dimension(150, createfield.getPreferredSize().height));
		JButton createbutton = new JButton("Create New Semester");
		createpanel.add(createlabel);
		createpanel.add(createfield);
		createpanel.add(createbutton);
		pane.add(createpanel, BorderLayout.NORTH);
		
		JScrollPane centerpane = new JScrollPane(centerbox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    centerpane.setPreferredSize(new Dimension(centerbox.getPreferredSize().width+17, centerbox.getPreferredSize().height));
	    pane.add(centerpane, BorderLayout.CENTER);
	    newactions.newClassPanel(true);
		
		//Button Actions
		JPanel actionspanel = new JPanel();
		JButton addcategorybutton = new JButton("Add New Category");
		JButton addclassbutton = new JButton("Add New Class");
		JButton deletecategorybutton = new JButton("Delete Last Category");
		JButton deleteclassbutton = new JButton("Delete Last Class");
		actionspanel.add(addcategorybutton);
		actionspanel.add(addclassbutton);
		actionspanel.add(deletecategorybutton);
		actionspanel.add(deleteclassbutton);
		pane.add(actionspanel, BorderLayout.SOUTH);
		
		//Action Listeners
		addclassbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				newactions.newClassPanel(false);
			}  
		});
		addcategorybutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				newactions.newCategoryPanel(false);
			}  
		});
		deleteclassbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				newactions.deleteLastClass();
			}  
		});
		deletecategorybutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				newactions.deleteLastCategory();
			}  
		});
		createbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				fileactions.saveSemester();
				String name = fileactions.getFileName();
				if(name != null){
					dispose();
					application.appframe.main(name, false);
				}//End of if
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	newactions.removeAllComponents();
            	createfield.setText("");
            	dispose();
            	launch.main(null);
            }
        });
		
	}//End of createnew
	
	public static void main(String[] args) {
		
		//Launch new instance of frame
		createnew frame = new createnew();
		frame.setVisible(true);

	}//End of main

}//End of createnew
