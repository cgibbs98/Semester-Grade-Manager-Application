package startup;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import javax.swing.*;

import application.AppFrame;

public class FileManager extends JFrame{
	
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
	
	
	
	
	
	public JComboBox semesterCombo;
	public JTextField fileNameField;
	
	public FileManager(){
		
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
		JPanel buttonPanel = new JPanel();
		JButton southButton1 = new JButton("Perform Action");
		JButton southButton2 = new JButton("Open Folder");
		JButton southButton3 = new JButton("Close Window");
		buttonPanel.add(southButton1);
		buttonPanel.add(southButton2);
		buttonPanel.add(southButton3);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		
		//WestBox
		Box westBox = Box.createVerticalBox();
		JLabel westLabel = new JLabel("  " + "File Actions:");
		ButtonGroup grouping = new ButtonGroup();
		JRadioButton button1 = new JRadioButton("Change Semester Filename");
		JRadioButton button2 = new JRadioButton("Copy Semester");
		JRadioButton button3 = new JRadioButton("Delete Semester");
		grouping.add(button1);
		grouping.add(button2);
		grouping.add(button3);
		button1.setSelected(true);
		westBox.add(westLabel);
		westBox.add(button1);
		westBox.add(button2);
		westBox.add(button3);
		pane.add(westBox, BorderLayout.WEST);
		
		//EastBox
		Box eastBox = Box.createVerticalBox();
		JPanel eastPanel1 = new JPanel();
		JLabel semesterLabel = new JLabel("Selected: ");
		semesterCombo = new JComboBox();
		semesterCombo.setPreferredSize(new Dimension(150, semesterCombo.getPreferredSize().height));
		getSemesters();
		
		JPanel eastPanel2 = new JPanel();
		JLabel fileNameLabel = new JLabel("New Filename: ");
		fileNameField = new JTextField("");
		fileNameField.setPreferredSize(new Dimension(150, fileNameField.getPreferredSize().height));
		
		eastPanel1.add(semesterLabel);
		eastPanel1.add(semesterCombo);
		eastPanel2.add(fileNameLabel);
		eastPanel2.add(fileNameField);
		eastBox.add(eastPanel1);
		eastBox.add(eastPanel2);
		pane.add(eastBox, BorderLayout.EAST);
		
		//Action Listeners
		button1.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				fileNameLabel.setEnabled(true);
				fileNameField.setEnabled(true);
			}  
		});
		button2.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				fileNameLabel.setEnabled(true);
				fileNameField.setEnabled(true);
			}  
		});
		button3.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				fileNameLabel.setEnabled(false);
				fileNameField.setEnabled(false);
			}  
		});
		southButton1.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				
				//Perform the selected operation
				
				//Change folder name
				if(button1.isSelected()){
					
					//Check if folder name can replace an existing name
					if(fileNameField.getText().equals("")){
						utilities.Misc.errorMessage("Can't rename semester! Folder name textfield is blank.");
						fileNameField.setText("");
						return;
					}//End of if
					for(int i = 0; i < semesterCombo.getItemCount(); i++){
						if(fileNameField.getText().equals(semesterCombo.getItemAt(i))){
							utilities.Misc.errorMessage("Can't rename semester! Folder name already exists.");
							fileNameField.setText("");
							return;
						}//End of if
					}//End of for
					
					//Renames file
					File initial = new File(System.getProperty("user.dir") + "/savedsemesters" + "/" + semesterCombo.getSelectedItem().toString());
					if(initial.renameTo(new File(System.getProperty("user.dir") + "/savedsemesters" + "/" + fileNameField.getText()))) {
						
						//Opens quicklaunch file and updates it if semester being quicklaunched has a new name
						String quickSemester = "";
						String quickTitle = "";
						boolean quickBoolean;
						try {
							Scanner quickScan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
							quickSemester = quickScan.nextLine();
							quickTitle = quickScan.nextLine();
							quickBoolean = Boolean.parseBoolean(quickScan.nextLine());
							quickScan.close();
							if(quickSemester.equals(semesterCombo.getSelectedItem().toString())){
								FileWriter quickWrite = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
								quickWrite.write(fileNameField.getText() + "\n");
								quickWrite.write(quickTitle + "\n");
								quickWrite.write("" + quickBoolean);
								quickWrite.close();
							}//End of if
						} catch (Exception ex) {
							
						}//End of try catch
						
						//Changes combobox to accomodate name change and set index to new name
						removeCombos();
						getSemesters();
						semesterCombo.setSelectedItem(fileNameField.getText());
						fileNameField.setText("");
						
					}//End of if
					else {
						
						utilities.Misc.errorMessage("An error has occured when renaming folder; Please try again later.");
						fileNameField.setText("");
						return;
						
					}//End of else
					
					
				}//End of if
				
				//Copy folder
				else if(button2.isSelected()){
					
					//Check if folder name can replace an existing name
					if(fileNameField.getText().equals("")){
						utilities.Misc.errorMessage("Can't copy semester! Folder name textfield is blank.");
						fileNameField.setText("");
						return;
					}//End of if
					for(int i = 0; i < semesterCombo.getItemCount(); i++){
						if(fileNameField.getText().equals(semesterCombo.getItemAt(i))){
							utilities.Misc.errorMessage("Can't copy semester! Folder name already exists.");
							fileNameField.setText("");
							return;
						}//End of if
					}//End of for
					
					//Copies folder
					try {
						Path src = Paths.get(System.getProperty("user.dir") + "/savedsemesters" + "/" + semesterCombo.getSelectedItem().toString());
						Path dest = Paths.get(System.getProperty("user.dir") + "/savedsemesters" + "/" + fileNameField.getText());
						Files.walkFileTree(src, new copyFolder(src, dest));
						
						//Changes combobox to accomodate new folder and set index to new folder
						removeCombos();
						getSemesters();
						semesterCombo.setSelectedItem(fileNameField.getText());
						fileNameField.setText("");
						
					}catch(Exception ex) {
						utilities.Misc.errorMessage("An error has occured when copying folder; Please try again later.");
						fileNameField.setText("");
						return;
					}
					
				}//End of else if
				
				//Delete folder
				else if(button3.isSelected()){
					
					//Delete files
					try {
						removeDirectory(new File(System.getProperty("user.dir") + "/savedsemesters" + "/" + semesterCombo.getSelectedItem().toString()));
						
					}catch(Exception ex) {
						utilities.Misc.errorMessage("An error has occured when deleting folder; Please try again later.");
						fileNameField.setText("");
						return;
					}
					
					//Deletes quicklaunch file if the semester set for quicklaunch has been deleted
					String quickSemester = "";
					try {
						Scanner quickScan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
						quickSemester = quickScan.nextLine();
						quickScan.close();
						if(quickSemester.equals(semesterCombo.getSelectedItem().toString())){
							File removed = new File(System.getProperty("user.dir") + "/quicklaunch.txt");
							removed.delete();
						}//End of if
					} catch (Exception ex) {
						
					}//End of try catch
					
					//Set combobox to previous index to accomodate deleted folder and close window if no ore fodlers are present
					semesterCombo.removeItem(semesterCombo.getSelectedItem().toString());
					int index = semesterCombo.getItemCount()-1;
					if(index < 0){
						fileNameField.setText("");
						dispose();
						Launch.main(null);
					}//End of if
					else{
						semesterCombo.setSelectedIndex(index);
						
						//Changes combobox to accomodate name change and set index to new name
						removeCombos();
						getSemesters();
						fileNameField.setText("");
					}//End of else
					
				}//End of else if
				
			}  
		});
		southButton2.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				
				//Open current semester folder
				Desktop desktop = Desktop.getDesktop();
		        File open = null;
		        try {
		            open = new File(System.getProperty("user.dir") + "/savedsemesters/" + semesterCombo.getSelectedItem().toString());
		            desktop.open(open);
		        } catch (Exception ex) {
		        	utilities.Misc.errorMessage("An error has occured, Please try again later.");
		        }
		        
			}  
		});
		southButton3.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				File relaunchFile = new File("relaunch.txt");
		    	try {relaunchFile.createNewFile();} catch (Exception ex) {}
				removeCombos();
				fileNameField.setText("");
				dispose();
				Launch.main(null);
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			File relaunchFile = new File("relaunch.txt");
	    	try {relaunchFile.createNewFile();} catch (Exception ex) {}
			removeCombos();
			fileNameField.setText("");
			dispose();
			Launch.main(null);
		}
		});
		
	}//End of FileManager
	
	
	
	
	
	public void removeDirectory(File dir) {
		
		//Clear directory recursively recursively (Tutorial at: https://www.codejava.net/java-se/file-io/clean-and-remove-a-non-empty-directory)
		 if(dir.isDirectory()) {
			 File[] fileSet = dir.listFiles();
				 if (fileSet != null && fileSet.length > 0) {
					 for (int i = 0; i < fileSet.length; i++) {
						 removeDirectory(fileSet[i]);
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
		File[] folderSet = directory.listFiles(filter);
		
		//Add folders to combobox
		for(int i = 0; i < folderSet.length; i++) {
			semesterCombo.addItem(folderSet[i].getName());
		}//End of for
		
	}//End of getSemesters
	
	public void removeCombos(){
		
		//Remove options from combobox
		int comboCount = semesterCombo.getItemCount();
		for(int i = 0; i < comboCount-1; i++){
			semesterCombo.removeItemAt(0);
		}//End of for
		semesterCombo.removeItemAt(0);
		
	}//End of removeCombos
	
	public static void main(String[] args) {
		
		//Launch new instance of frame
		FileManager frame = new FileManager();
		frame.setVisible(true);

	}//End of main

}//End of FileManager
