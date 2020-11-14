package startup;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import javax.swing.*;

import application.appframe;

public class filemanager extends JFrame{
	
	//Inner class for copying folders (Tutorial at: https://www.codejava.net/java-se/file-io/java-nio-copy-file-or-directory-examples)
	public class copyFolder extends SimpleFileVisitor<Path> {
		private Path sourceDir;
		private Path targetDir;
		public copyFolder(Path sourceDir, Path targetDir) {
	        this.sourceDir = sourceDir;
	        this.targetDir = targetDir;
	    }
	 
	    @Override
	    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
	        try {
	            Path targetFile = targetDir.resolve(sourceDir.relativize(file));
	            Files.copy(file, targetFile);
	        } catch (IOException ex) {
	            System.err.println(ex);
	        }
	 
	        return FileVisitResult.CONTINUE;
	    }
	 
	    @Override
	    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) {
	        try {
	            Path newDir = targetDir.resolve(sourceDir.relativize(dir));
	            Files.createDirectory(newDir);
	        } catch (IOException ex) {
	            System.err.println(ex);
	        }
	 
	        return FileVisitResult.CONTINUE;
	    }
	}//End of copyFolder
	
	
	
	
	
	public JComboBox semestercombo;
	public JTextField filenamefield;
	
	public filemanager(){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/2.875);
		int h = (int) (screenSize.getHeight()/4.875);
		setSize(w, h);
				
		//Properties
		setTitle("Semester Grade Manager Application - Semester Manager");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				
		//Components
		Container pane = getContentPane();
		JPanel buttonpanel = new JPanel();
		JButton southbutton1 = new JButton("Perform Action");
		JButton southbutton2 = new JButton("Open Folder");
		JButton southbutton3 = new JButton("Close Window");
		buttonpanel.add(southbutton1);
		buttonpanel.add(southbutton2);
		buttonpanel.add(southbutton3);
		pane.add(buttonpanel, BorderLayout.SOUTH);
		
		//Westbox
		Box westbox = Box.createVerticalBox();
		JLabel westlabel = new JLabel("  " + "File Actions:");
		ButtonGroup buttongroup = new ButtonGroup();
		JRadioButton button1 = new JRadioButton("Change Semester Filename");
		JRadioButton button2 = new JRadioButton("Copy Semester");
		JRadioButton button3 = new JRadioButton("Delete Semester");
		buttongroup.add(button1);
		buttongroup.add(button2);
		buttongroup.add(button3);
		button1.setSelected(true);
		westbox.add(westlabel);
		westbox.add(button1);
		westbox.add(button2);
		westbox.add(button3);
		pane.add(westbox, BorderLayout.WEST);
		
		//Eastbox
		Box eastbox = Box.createVerticalBox();
		JPanel eastpanel1 = new JPanel();
		JLabel semesterlabel = new JLabel("Selected: ");
		semestercombo = new JComboBox();
		semestercombo.setPreferredSize(new Dimension(150, semestercombo.getPreferredSize().height));
		getSemesters();
		
		JPanel eastpanel2 = new JPanel();
		JLabel filenamelabel = new JLabel("New Filename: ");
		filenamefield = new JTextField("");
		filenamefield.setPreferredSize(new Dimension(150, filenamefield.getPreferredSize().height));
		
		eastpanel1.add(semesterlabel);
		eastpanel1.add(semestercombo);
		eastpanel2.add(filenamelabel);
		eastpanel2.add(filenamefield);
		eastbox.add(eastpanel1);
		eastbox.add(eastpanel2);
		pane.add(eastbox, BorderLayout.EAST);
		
		//Action Listeners
		button1.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				filenamelabel.setEnabled(true);
				filenamefield.setEnabled(true);
			}  
		});
		button2.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				filenamelabel.setEnabled(true);
				filenamefield.setEnabled(true);
			}  
		});
		button3.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				filenamelabel.setEnabled(false);
				filenamefield.setEnabled(false);
			}  
		});
		southbutton1.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				
				//Perform the selected operation
				
				//Change folder name
				if(button1.isSelected()){
					
					//Check if folder name can replace an existing name
					if(filenamefield.getText().equals("")){
						utilities.misc.errorMessage("Can't rename semester! Folder name textfield is blank.");
						filenamefield.setText("");
						return;
					}//End of if
					for(int i = 0; i < semestercombo.getItemCount(); i++){
						if(filenamefield.getText().equals(semestercombo.getItemAt(i))){
							utilities.misc.errorMessage("Can't rename semester! Folder name already exists.");
							filenamefield.setText("");
							return;
						}//End of if
					}//End of for
					
					//Renames file
					File initial = new File(System.getProperty("user.dir") + "/savedsemesters" + "/" + semestercombo.getSelectedItem().toString());
					if(initial.renameTo(new File(System.getProperty("user.dir") + "/savedsemesters" + "/" + filenamefield.getText()))) {
						
						//Opens quicklaunch file and updates it if semester being quicklaunched has a new name
						String quicksemester = "";
						String quicktitle = "";
						boolean quickboolean;
						try {
							Scanner quickscan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
							quicksemester = quickscan.nextLine();
							quicktitle = quickscan.nextLine();
							quickboolean = Boolean.parseBoolean(quickscan.nextLine());
							quickscan.close();
							if(quicksemester.equals(semestercombo.getSelectedItem().toString())){
								FileWriter quickwrite = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
								quickwrite.write(filenamefield.getText() + "\n");
								quickwrite.write(quicktitle + "\n");
								quickwrite.write("" + quickboolean);
								quickwrite.close();
							}//End of if
						} catch (Exception ex) {
							
						}//End of try catch
						
						//Changes combobox to accomodate name change and set index to new name
						removeCombos();
						getSemesters();
						semestercombo.setSelectedItem(filenamefield.getText());
						filenamefield.setText("");
						
					}//End of if
					else {
						
						utilities.misc.errorMessage("An error has occured when renaming folder; Please try again later.");
						filenamefield.setText("");
						return;
						
					}//End of else
					
					
				}//End of if
				
				//Copy folder
				else if(button2.isSelected()){
					
					//Check if folder name can replace an existing name
					if(filenamefield.getText().equals("")){
						utilities.misc.errorMessage("Can't copy semester! Folder name textfield is blank.");
						filenamefield.setText("");
						return;
					}//End of if
					for(int i = 0; i < semestercombo.getItemCount(); i++){
						if(filenamefield.getText().equals(semestercombo.getItemAt(i))){
							utilities.misc.errorMessage("Can't copy semester! Folder name already exists.");
							filenamefield.setText("");
							return;
						}//End of if
					}//End of for
					
					//Copies folder
					try {
						Path src = Paths.get(System.getProperty("user.dir") + "/savedsemesters" + "/" + semestercombo.getSelectedItem().toString());
						Path dest = Paths.get(System.getProperty("user.dir") + "/savedsemesters" + "/" + filenamefield.getText());
						Files.walkFileTree(src, new copyFolder(src, dest));
						
						//Changes combobox to accomodate new folder and set index to new folder
						removeCombos();
						getSemesters();
						semestercombo.setSelectedItem(filenamefield.getText());
						filenamefield.setText("");
						
					}catch(Exception ex) {
						utilities.misc.errorMessage("An error has occured when copying folder; Please try again later.");
						filenamefield.setText("");
						return;
					}
					
				}//End of else if
				
				//Delete folder
				else if(button3.isSelected()){
					
					//Delete files
					try {
						removeDirectory(new File(System.getProperty("user.dir") + "/savedsemesters" + "/" + semestercombo.getSelectedItem().toString()));
						
					}catch(Exception ex) {
						utilities.misc.errorMessage("An error has occured when deleting folder; Please try again later.");
						filenamefield.setText("");
						return;
					}
					
					//Deletes quicklaunch file if the semester set for quicklaunch has been deleted
					String quicksemester = "";
					try {
						Scanner quickscan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
						quicksemester = quickscan.nextLine();
						quickscan.close();
						if(quicksemester.equals(semestercombo.getSelectedItem().toString())){
							File removed = new File(System.getProperty("user.dir") + "/quicklaunch.txt");
							removed.delete();
						}//End of if
					} catch (Exception ex) {
						
					}//End of try catch
					
					//Set combobox to previous index to accomodate deleted folder and close window if no ore fodlers are present
					semestercombo.removeItem(semestercombo.getSelectedItem().toString());
					int index = semestercombo.getItemCount()-1;
					if(index < 0){
						filenamefield.setText("");
						dispose();
						launch.main(null);
					}//End of if
					else{
						semestercombo.setSelectedIndex(index);
						
						//Changes combobox to accomodate name change and set index to new name
						removeCombos();
						getSemesters();
						filenamefield.setText("");
					}//End of else
					
				}//End of else if
				
			}  
		});
		southbutton2.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				
				//Open current semester folder
				Desktop desktop = Desktop.getDesktop();
		        File open = null;
		        try {
		            open = new File(System.getProperty("user.dir") + "/savedsemesters/" + semestercombo.getSelectedItem().toString());
		            desktop.open(open);
		        } catch (Exception ex) {
		        	utilities.misc.errorMessage("An error has occured, Please try again later.");
		        }
		        
			}  
		});
		southbutton3.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				File relaunchfile = new File("relaunch.txt");
		    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
				removeCombos();
				filenamefield.setText("");
				dispose();
				launch.main(null);
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			File relaunchfile = new File("relaunch.txt");
	    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
			removeCombos();
			filenamefield.setText("");
			dispose();
			launch.main(null);
		}
		});
		
	}//End of filemanager
	
	
	
	
	
	public void removeDirectory(File dir) {
		
		//Clear directory recursively recursively (Tutorial at: https://www.codejava.net/java-se/file-io/clean-and-remove-a-non-empty-directory)
		 if(dir.isDirectory()) {
			 File[] fileset = dir.listFiles();
				 if (fileset != null && fileset.length > 0) {
					 for (int i = 0; i < fileset.length; i++) {
						 removeDirectory(fileset[i]);
					 }//End of for
				 }//End of if
			 dir.delete();
		 }//End of if
		 else {
			 dir.delete();
		 }//End of else
		
	}//End of removeDirectory
	
	public void getSemesters() {
		
		//Set semesters directories to only include semesters (folders)
		File directory = new File(System.getProperty("user.dir") + "/savedsemesters");
		FileFilter filter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
		File[] folderset = directory.listFiles(filter);
		
		//Add folders to combobox
		for(int i = 0; i < folderset.length; i++) {
			semestercombo.addItem(folderset[i].getName());
		}//End of for
		
	}//End of getSemesters
	
	public void removeCombos(){
		
		//Remove options from combobox
		int combocount = semestercombo.getItemCount();
		for(int i = 0; i < combocount-1; i++){
			semestercombo.removeItemAt(0);
		}//End of for
		semestercombo.removeItemAt(0);
		
	}//End of removeCombos
	
	public static void main(String[] args) {
		
		//Launch new instance of frame
		filemanager frame = new filemanager();
		frame.setVisible(true);

	}//End of main

}//End of filemanager
