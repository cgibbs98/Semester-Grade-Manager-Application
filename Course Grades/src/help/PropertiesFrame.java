package help;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class PropertiesFrame extends JDialog{

	public PropertiesFrame(String fileName){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/1.75);
		int h = (int) (screenSize.getHeight()/4.25);
		setSize(w, h);
		
		//Properties
		setTitle("View Properties");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		//Components
		Container pane = getContentPane();
		JPanel filePanel = new JPanel();
		JLabel fileLabel = new JLabel("Semester Filepath: " + System.getProperty("user.dir") + "/savedsemesters/" + fileName);
		filePanel.add(fileLabel);
		pane.add(filePanel, BorderLayout.NORTH);
		
		//Properties
		Box propBox = Box.createVerticalBox();
		propBox.add(new JLabel(" "));
		long folderSize = getfolderSize(new File(System.getProperty("user.dir") + "/savedsemesters/" + fileName));
		propBox.add(new JLabel("  " + "Folder Size:                " + folderSize + " bytes (" + (folderSize/(double)1024) + " KB)"));
		FileTime creation;
		try {
			creation = (FileTime) Files.getAttribute(Paths.get(System.getProperty("user.dir") + "/savedsemesters/" + fileName), "creationTime");
		} catch (Exception e) {
			utilities.Misc.errorMessage("An error has occured, Please try again later.");
			dispose();
			return;
		}
		FileTime modified;
		try {
			modified = (FileTime) Files.getAttribute(Paths.get(System.getProperty("user.dir") + "/savedsemesters/" + fileName + "/master.txt"), "lastModifiedTime");
		} catch (Exception e) {
			utilities.Misc.errorMessage("An error has occured, Please try again later.");
			dispose();
			return;
		}
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss aa");
	    propBox.add(new JLabel("  " + "File Creation Time:   " + dateFormat.format(creation.toMillis())));
	    propBox.add(new JLabel("  " + "Last Modified Time: " + dateFormat.format(modified.toMillis())));
	    propBox.add(new JLabel("  " + "Total Table Cells:      " + getTotalCells(new File(System.getProperty("user.dir") + "/savedsemesters/" + fileName))));
		pane.add(propBox, BorderLayout.WEST);
		
		//Close button
		JPanel buttonPanel = new JPanel();
		JButton openButton = new JButton("Open Folder");
		JButton closeButton = new JButton("Close Window");
		buttonPanel.add(openButton);
		buttonPanel.add(closeButton);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		
		//Action Listeners
		openButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				
				//Open current semester folder
				Desktop desktop = Desktop.getDesktop();
		        File open = null;
		        try {
		            open = new File(System.getProperty("user.dir") + "/savedsemesters/" + fileName);
		            desktop.open(open);
		        } catch (Exception ex) {
		        	utilities.Misc.errorMessage("An error has occured, Please try again later.");
		        }
		        
			}  
		});
		closeButton.addActionListener(new ActionListener(){  
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
		setVisible(true);
		
	}//End of PropertiesFrame
	
	
	
	
	
	public long getfolderSize(File folder){
		
		//Gets size of current semester folder by adding each individual file and subfolder recursively (Tutorial at: https://www.geeksforgeeks.org/java-program-to-get-the-size-of-a-directory/)
		long length = 0; 
        File[] files = folder.listFiles(); 
        for(int i = 0; i < files.length; i++) { 
            if (files[i].isFile()) { 
                length += files[i].length(); 
            }//End of if
            else { 
                length += getfolderSize(files[i]); 
            }//End of else
        }//End of for
        return length; 
		
	}//End of getfolderSize
	
	public int getTotalCells(File folder){
		
		//Gets total number of cells in a table if all categories of each class were put in 1 table (Based on above approach)
		int rows = 0;
		File[] files = folder.listFiles(); 
        for(int i = 0; i < files.length; i++) { 
            
        	//If a directory is found, go through each category file and count how many rows are in it 
        	if(files[i].isDirectory()) {
                File[] categories = files[i].listFiles();
                for(int j = 0; j < categories.length; j++) {
                	Scanner rowScanner = null;
					try {
						rowScanner = new Scanner(categories[j]);
					} catch (Exception e) {
						
					}
                	while(rowScanner.hasNext()){
                		rowScanner.nextLine();
                		rows++;
                	}//End of while
                	rowScanner.close();
                }//End of for
                
            }//End of if
        	
        }//End of for
        return rows*4; 
		
	}//End of getFolderCount
	
	public static void main(String fileName){
		
		//Launch new instance of frame
		PropertiesFrame frame = new PropertiesFrame(fileName);
		
	}//End of main
	
}//End of PropertiesFrame
