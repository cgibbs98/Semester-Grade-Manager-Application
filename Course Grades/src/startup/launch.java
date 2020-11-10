package startup;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class launch extends JFrame{

	public JLabel quicklabel = new JLabel("  " + "Quicklaunch Semester - ");
	public JButton button1 = new JButton("Quicklaunch");
	public JButton button3 = new JButton("Open Existng Semester");
	public JButton button4 = new JButton("Manage Semesters");
	public static String quickname = "";
	public static boolean skipcheck = false;
	
	public launch(){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/1.875);
		int h = (int) (screenSize.getHeight()/6.875);
		setSize(w, h);
		
		//Properties
		setTitle("Semester Grade Manager Application - Startup");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Components
		Container pane = getContentPane();
		Box launchbox = Box.createVerticalBox();
		JLabel welcomelabel = new JLabel("  " + "Welcome to the application! Please select an action below.");
		launchbox.add(welcomelabel);
		launchbox.add(quicklabel);
		pane.add(launchbox, BorderLayout.CENTER);
		JPanel buttonpanel = new JPanel();
		JButton button2 = new JButton("Create New Semester");
		JButton button5 = new JButton("Exit Application");
		button1.setEnabled(false);
		buttonpanel.add(button1);
		buttonpanel.add(button2);
		buttonpanel.add(button3);
		buttonpanel.add(button4);
		buttonpanel.add(button5);
		pane.add(buttonpanel, BorderLayout.SOUTH);
		checkDirectories();
		checkQuicklaunch();
		
		//Action Listeners
		button1.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				quickLaunch();
			}  
			});
		button2.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			dispose();
			createnew.main(null);
		}  
		});
		button3.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			fileactions.openSemester();
			String name = fileactions.getFileName();
			if(name != null){
				dispose();
				application.appframe.main(name, false);
			}//End of if
		}  
		});
		button4.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			dispose();
			filemanager.main(null);
		}  
		});
		button5.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			System.exit(0);
		}  
		});
		
	}//End of launch
	
	
	
	
	
	public void checkQuicklaunch(){
		
		//Opens quicklaunch file and checks if a class can be launched without selecting the filechooser
		Scanner filescan;
		try {
			
			//Scan quicklaunch file to see which class can be launched and if it can be skipped right away
			filescan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			String filename = filescan.nextLine();
			quicklabel.setText(quicklabel.getText() + filescan.nextLine());
			skipcheck = Boolean.parseBoolean(filescan.nextLine());
			filescan.close();
			quickname = filename;
			button1.setEnabled(true);
			
			//If the program is relaunching set program to false and delete dummy file
			File relaunchfile = new File("relaunch.txt");
			if(relaunchfile.exists()){
				skipcheck = false;
				relaunchfile.delete();
			}//End of if
			
		} catch (Exception e) {
			quicklabel.setText(quicklabel.getText() + "N/A");
		}//End of try catch
		
	}//End of checkQuicklaunch
	
	public void checkDirectories(){
		
		//Load all semesters
		File directory = new File(System.getProperty("user.dir") + "/savedsemesters");
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
		File[] folderset = directory.listFiles(filter);
		
		//Disable opening and managing semesters if none are present
		if(folderset.length == 0){
			button1.setEnabled(false);
			button3.setEnabled(false);
			button4.setEnabled(false);
		}//End of folderset
		
	}//End of checkDirectories
	
	public void quickLaunch() {
		dispose();
		application.appframe.main(quickname, false);
	}//End of quickLaunch
	
	public static void main(String[] args){
		
		//Launch new instance of frame
		launch frame = new launch();
	    if(skipcheck){
			application.appframe.main(quickname, true);
		}//End of if
	    else{
	    	frame.setVisible(true);
	    }//End of else
	    
	}//End of main

}//End of launch
