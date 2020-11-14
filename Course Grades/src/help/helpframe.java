package help;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class helpframe  extends JDialog{

	public JTextArea textarea = new JTextArea("");
	
	public helpframe(){
		
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
		DefaultMutableTreeNode rootc1 = new DefaultMutableTreeNode("Startup");
		DefaultMutableTreeNode rootc2 = new DefaultMutableTreeNode("Application");
		root.add(rootc1);
		root.add(rootc2);
		
		//Startup Children
		DefaultMutableTreeNode startc1 = new DefaultMutableTreeNode("Quicklaunch Utility");
		DefaultMutableTreeNode startc2 = new DefaultMutableTreeNode("Creating New Semester");
		DefaultMutableTreeNode startc3 = new DefaultMutableTreeNode("Opening Exisitng Semesters");
		DefaultMutableTreeNode startc4 = new DefaultMutableTreeNode("Managing Semesters");
		rootc1.add(startc1);
		rootc1.add(startc2);
		rootc1.add(startc3);
		rootc1.add(startc4);
		
		//Application Children
		DefaultMutableTreeNode appc1 = new DefaultMutableTreeNode("Layout & Design");
		DefaultMutableTreeNode appc2 = new DefaultMutableTreeNode("Adding & Removing Grades");
		DefaultMutableTreeNode appc3 = new DefaultMutableTreeNode("Updating & Saving Grades");
		DefaultMutableTreeNode appc4 = new DefaultMutableTreeNode("Checking Points Needed");
		DefaultMutableTreeNode appc5 = new DefaultMutableTreeNode("File Menu");
		DefaultMutableTreeNode appc6 = new DefaultMutableTreeNode("Help Menu");
		rootc2.add(appc1);
		rootc2.add(appc2);
		rootc2.add(appc3);
		rootc2.add(appc4);
		rootc2.add(appc5);
		rootc2.add(appc6);
		
		//Add tree to layout
		Border border = BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK);
		JTree tree = new JTree(root);
		tree.setBorder(border);
		tree.setPreferredSize(new Dimension(221, tree.getPreferredSize().height));
		pane.add(tree, BorderLayout.WEST);
		
		//Text Area takes up entire right side of program
		JScrollPane textpane = new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		textarea.setEditable(false);
		textarea.setCaretPosition(0);
		textarea.setMargin(new Insets(10, 10, 10, 10));
		textpane.setPreferredSize(new Dimension(w-tree.getPreferredSize().width-15, textarea.getPreferredSize().height));
		pane.add(textpane, BorderLayout.EAST);
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
		
	}//End of helpframe
	
	
	
	
	
	public void setHelpText(String name){
		
		//Help Topics, Startup, and Application nodes
		if(name.equals("Help Topics")){
			textarea.setText(name + ":\n\n");
			textarea.append("This application is designed to help you manage your grades in any given semester to know where you stand in each class. This help window aims to explain how the program works and how to use it correctly.");
			textarea.append("\n\n" + "Pick an item from either of the folders below to learn more about it.");
			return;
		}//End of if
		else if(name.equals("Startup")){
			textarea.setText(name + ":\n\n");
			textarea.append("The items in the Startup folder will help you understand the features for the launch functionality that occurs when you 1st startup the program.");
			textarea.append("\n\n" + "Pick an item from this folder below to learn more about it.");
			return;
		}//End of if
		else if(name.equals("Application")){
			textarea.setText(name + ":\n\n");
			textarea.append("The items in the Application folder will help you understand the features for the main application that manages your grades for a selected semester.");
			textarea.append("\n\n" + "Pick an item from this folder below to learn more about it.");
			return;
		}//End of if
		
		//Child nodes from Startup and Application
		else{
			
			//Maps current child node with correct file
			String filename = "";
			switch(name){
				case "Quicklaunch Utility": 
					filename = "startc1"; 
					break;
				case "Creating New Semester": 
					filename = "startc2"; 
					break;
				case "Opening Exisitng Semesters": 
					filename = "startc3"; 
					break;
				case "Managing Semesters": 
					filename = "startc4"; 
					break;
				case "Layout & Design": 
					filename = "appc1"; 
					break;
				case "Adding & Removing Grades": 
					filename = "appc2"; 
					break;
				case "Updating & Saving Grades": 
					filename = "appc3"; 
					break;
				case "Checking Points Needed": 
					filename = "appc4"; 
					break;
				case "File Menu": 
					filename = "appc5"; 
					break;
				case "Help Menu": 
					filename = "appc6"; 
					break;
			}//End of switch
			
			//Read from respective text file and displays contents to user
			textarea.setText(name + ":\n\n");
			Scanner filescan = null;
			try {
				filescan = new Scanner(new File(System.getProperty("user.dir") + "/helptopics/" + filename + ".txt"));
			} catch (Exception e) {
				
			}//End of try catch
			while(filescan.hasNext()){
				textarea.append(filescan.nextLine() + "\n");
			}//End of while
			filescan.close();
			
		}//End of else
		
	}//End of setHelpText
	
	public static void main(String[] args){
		
		//Launch new instance of frame
		helpframe frame = new helpframe();
		frame.setVisible(true);
		
	}//End of main
	
}//End of helpframe
