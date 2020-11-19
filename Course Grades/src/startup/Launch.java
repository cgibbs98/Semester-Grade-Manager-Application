package startup;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Launch extends JFrame{

	public JLabel quickLabel = new JLabel("  " + "Quicklaunch Semester - ");
	public JButton button1 = new JButton("Quicklaunch");
	public JButton button3 = new JButton("Open Existng Semester");
	public JButton button4 = new JButton("Manage Semesters");
	public static String quickName = "";
	public static boolean skipCheck = false;
	
	public Launch(){
		
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
		Box launchBox = Box.createVerticalBox();
		JLabel welcomeLabel = new JLabel("  " + "Welcome to the application! Please select an action below.");
		launchBox.add(welcomeLabel);
		launchBox.add(quickLabel);
		pane.add(launchBox, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		JButton button2 = new JButton("Create New Semester");
		JButton button5 = new JButton("Exit Application");
		button1.setEnabled(false);
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
		buttonPanel.add(button4);
		buttonPanel.add(button5);
		pane.add(buttonPanel, BorderLayout.SOUTH);
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
			CreateNew.main(null);
		}  
		});
		button3.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			FileActions.openSemester();
			String name = FileActions.getFileName();
			if(name != null){
				dispose();
				application.AppFrame.main(name, false);
			}//End of if
		}  
		});
		button4.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			dispose();
			FileManager.main(null);
		}  
		});
		button5.addActionListener(new ActionListener(){  
		public void actionPerformed(ActionEvent e){  
			System.exit(0);
		}  
		});
		
	}//End of Launch
	
	
	
	
	
	public void checkQuicklaunch(){
		
		//Opens quicklaunch file and checks if a class can be launched without selecting the filechooser
		Scanner fileScan;
		try {
			
			//Scan quicklaunch file to see which class can be launched and if it can be skipped right away
			fileScan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			String fileName = fileScan.nextLine();
			quickLabel.setText(quickLabel.getText() + fileScan.nextLine());
			skipCheck = Boolean.parseBoolean(fileScan.nextLine());
			fileScan.close();
			quickName = fileName;
			button1.setEnabled(true);
			
			//If the program is relaunching set program to false and delete dummy file
			File relaunchFile = new File("relaunch.txt");
			if(relaunchFile.exists()){
				skipCheck = false;
				relaunchFile.delete();
			}//End of if
			
		} catch (Exception e) {
			quickLabel.setText(quickLabel.getText() + "N/A");
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
		File[] folderSet = directory.listFiles(filter);
		
		//Disable opening and managing semesters if none are present
		if(folderSet.length == 0){
			button1.setEnabled(false);
			button3.setEnabled(false);
			button4.setEnabled(false);
		}//End of if
		
	}//End of checkDirectories
	
	public void quickLaunch() {
		dispose();
		application.AppFrame.main(quickName, false);
	}//End of quickLaunch
	
	public static void main(String[] args){
		
		//Launch new instance of frame
		Launch frame = new Launch();
	    if(skipCheck){
			application.AppFrame.main(quickName, true);
		}//End of if
	    else{
	    	frame.setVisible(true);
	    }//End of else
	    
	}//End of main

}//End of Launch
