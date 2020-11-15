package help;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class HelpFrame  extends JDialog{

	public JTextArea textArea = new JTextArea("");
	
	public HelpFrame(){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/2);
		int h = (int) (screenSize.getHeight()/2);
		setSize(w, h);
		
		//Properties
		setTitle("Help Topics");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		Container pane = getContentPane();
		
		//Tree construction
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Help Topics");   
		
		//Root's children
		DefaultMutableTreeNode rootC1 = new DefaultMutableTreeNode("Startup");
		DefaultMutableTreeNode rootC2 = new DefaultMutableTreeNode("Application");
		root.add(rootC1);
		root.add(rootC2);
		
		//Startup Children
		DefaultMutableTreeNode startC1 = new DefaultMutableTreeNode("Quicklaunch Utility");
		DefaultMutableTreeNode startC2 = new DefaultMutableTreeNode("Creating New Semester");
		DefaultMutableTreeNode startC3 = new DefaultMutableTreeNode("Opening Exisitng Semesters");
		DefaultMutableTreeNode startC4 = new DefaultMutableTreeNode("Managing Semesters");
		rootC1.add(startC1);
		rootC1.add(startC2);
		rootC1.add(startC3);
		rootC1.add(startC4);
		
		//Application Children
		DefaultMutableTreeNode appC1 = new DefaultMutableTreeNode("Layout & Design");
		DefaultMutableTreeNode appC2 = new DefaultMutableTreeNode("Adding & Removing Grades");
		DefaultMutableTreeNode appC3 = new DefaultMutableTreeNode("Updating & Saving Grades");
		DefaultMutableTreeNode appC4 = new DefaultMutableTreeNode("Checking Points Needed");
		DefaultMutableTreeNode appC5 = new DefaultMutableTreeNode("File Menu");
		DefaultMutableTreeNode appC6 = new DefaultMutableTreeNode("Help Menu");
		rootC2.add(appC1);
		rootC2.add(appC2);
		rootC2.add(appC3);
		rootC2.add(appC4);
		rootC2.add(appC5);
		rootC2.add(appC6);
		
		//Add tree to layout
		Border border = BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK);
		JTree tree = new JTree(root);
		tree.setBorder(border);
		tree.setPreferredSize(new Dimension(221, tree.getPreferredSize().height));
		pane.add(tree, BorderLayout.WEST);
		
		//Text Area takes up entire right side of program
		JScrollPane textPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setCaretPosition(0);
		textArea.setMargin(new Insets(10, 10, 10, 10));
		textPane.setPreferredSize(new Dimension(w-tree.getPreferredSize().width-15, textArea.getPreferredSize().height));
		pane.add(textPane, BorderLayout.EAST);
		setHelpText("Help Topics");
		
		//Tree Listener
		tree.addTreeSelectionListener(new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
		    	
		    	//Gets currently selected node and reutrn if the selection is null
		    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		    	if (node == null){ 
		    		return;
			    };
			    setHelpText(node.getUserObject().toString());
			    
		    }
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//System.out.println(tree.getPreferredSize());
				dispose();
			}
		});
		
	}//End of HelpFrame
	
	
	
	
	
	public void setHelpText(String name){
		
		//Help Topics, Startup, and Application nodes
		if(name.equals("Help Topics")){
			textArea.setText(name + ":\n\n");
			textArea.append("This application is designed to help you manage your grades in any given semester to know where you stand in each class. This help window aims to explain how the program works and how to use it correctly.");
			textArea.append("\n\n" + "Pick an item from either of the folders below to learn more about it.");
			return;
		}//End of if
		else if(name.equals("Startup")){
			textArea.setText(name + ":\n\n");
			textArea.append("The items in the Startup folder will help you understand the features for the launch functionality that occurs when you 1st startup the program.");
			textArea.append("\n\n" + "Pick an item from this folder below to learn more about it.");
			return;
		}//End of if
		else if(name.equals("Application")){
			textArea.setText(name + ":\n\n");
			textArea.append("The items in the Application folder will help you understand the features for the main application that manages your grades for a selected semester.");
			textArea.append("\n\n" + "Pick an item from this folder below to learn more about it.");
			return;
		}//End of if
		
		//Child nodes from Startup and Application
		else{
			
			//Maps current child node with correct file
			String fileName = "";
			switch(name){
				case "Quicklaunch Utility": 
					fileName = "startC1"; 
					break;
				case "Creating New Semester": 
					fileName = "startC2"; 
					break;
				case "Opening Exisitng Semesters": 
					fileName = "startC3"; 
					break;
				case "Managing Semesters": 
					fileName = "startC4"; 
					break;
				case "Layout & Design": 
					fileName = "appC1"; 
					break;
				case "Adding & Removing Grades": 
					fileName = "appC2"; 
					break;
				case "Updating & Saving Grades": 
					fileName = "appC3"; 
					break;
				case "Checking Points Needed": 
					fileName = "appC4"; 
					break;
				case "File Menu": 
					fileName = "appC5"; 
					break;
				case "Help Menu": 
					fileName = "appC6"; 
					break;
			}//End of switch
			
			//Read from respective text file and displays contents to user
			textArea.setText(name + ":\n\n");
			Scanner fileScan = null;
			try {
				fileScan = new Scanner(new File(System.getProperty("user.dir") + "/helptopics/" + fileName + ".txt"));
			} catch (Exception e) {
				
			}//End of try catch
			while(fileScan.hasNext()){
				textArea.append(fileScan.nextLine() + "\n");
			}//End of while
			fileScan.close();
			
		}//End of else
		
	}//End of setHelpText
	
	public static void main(String[] args){
		
		//Launch new instance of frame
		HelpFrame frame = new HelpFrame();
		frame.setVisible(true);
		
	}//End of main
	
}//End of HelpFrame
