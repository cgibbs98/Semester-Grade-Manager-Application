package help;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class propertiesframe extends JDialog{

	public propertiesframe(String filename){
		
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
		JPanel filepanel = new JPanel();
		JLabel filelabel = new JLabel("Semester Filepath: " + System.getProperty("user.dir") + "/savedsemesters/" + filename);
		filepanel.add(filelabel);
		pane.add(filepanel, BorderLayout.NORTH);
		
		//Properties
		Box propbox = Box.createVerticalBox();
		propbox.add(new JLabel(" "));
		long foldersize = getFolderSize(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename));
		propbox.add(new JLabel("  " + "Folder Size:                " + foldersize + " bytes (" + (foldersize/(double)1024) + " KB)"));
		FileTime creation;
		try {
			creation = (FileTime) Files.getAttribute(Paths.get(System.getProperty("user.dir") + "/savedsemesters/" + filename), "creationTime");
		} catch (Exception e) {
			utilities.misc.errorMessage("An error has occured, Please try again later.");
			dispose();
			return;
		}
		FileTime modified;
		try {
			modified = (FileTime) Files.getAttribute(Paths.get(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/master.txt"), "lastModifiedTime");
		} catch (Exception e) {
			utilities.misc.errorMessage("An error has occured, Please try again later.");
			dispose();
			return;
		}
	    SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss aa");
	    propbox.add(new JLabel("  " + "File Creation Time:   " + dateformat.format(creation.toMillis())));
	    propbox.add(new JLabel("  " + "Last Modified Time: " + dateformat.format(modified.toMillis())));
	    propbox.add(new JLabel("  " + "Total Table Cells:      " + getTotalCells(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename))));
		pane.add(propbox, BorderLayout.WEST);
		
		//Close button
		JPanel buttonpanel = new JPanel();
		JButton openbutton = new JButton("Open Folder");
		JButton closebutton = new JButton("Close Window");
		buttonpanel.add(openbutton);
		buttonpanel.add(closebutton);
		pane.add(buttonpanel, BorderLayout.SOUTH);
		
		//Action Listeners
		openbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				
				//Open current semester folder
				Desktop desktop = Desktop.getDesktop();
		        File open = null;
		        try {
		            open = new File(System.getProperty("user.dir") + "/savedsemesters/" + filename);
		            desktop.open(open);
		        } catch (Exception ex) {
		        	utilities.misc.errorMessage("An error has occured, Please try again later.");
		        }
		        
			}  
		});
		closebutton.addActionListener(new ActionListener(){  
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
		
	}//End of propertiesframe
	
	
	
	
	
	public long getFolderSize(File folder){
		
		//Gets size of current semester folder by adding each individual file and subfolder recursively (Tutorial at: https://www.geeksforgeeks.org/java-program-to-get-the-size-of-a-directory/)
		long length = 0; 
        File[] files = folder.listFiles(); 
        for(int i = 0; i < files.length; i++) { 
            if (files[i].isFile()) { 
                length += files[i].length(); 
            }//End of if
            else { 
                length += getFolderSize(files[i]); 
            }//End of else
        }//End of for
        return length; 
		
	}//End of getFolderSize
	
	public int getTotalCells(File folder){
		
		//Gets total number of cells in a table if all categories of each class were put in 1 table (Based on above approach)
		int rows = 0;
		File[] files = folder.listFiles(); 
        for(int i = 0; i < files.length; i++) { 
            
        	//If a directory is found, go through each category file and count how many rows are in it 
        	if(files[i].isDirectory()) {
                File[] categories = files[i].listFiles();
                for(int j = 0; j < categories.length; j++) {
                	Scanner rowscanner = null;
					try {
						rowscanner = new Scanner(categories[j]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	while(rowscanner.hasNext()){
                		rowscanner.nextLine();
                		rows++;
                	}//End of while
                	rowscanner.close();
                }//End of for
                
            }//End of if
        	
        }//End of for
        return rows*4; 
		
	}//End of getFolderCount
	
	public static void main(String filename){
		
		//Launch new instance of frame
		propertiesframe frame = new propertiesframe(filename);
		
	}//End of main
	
}//End of propertiesframe
