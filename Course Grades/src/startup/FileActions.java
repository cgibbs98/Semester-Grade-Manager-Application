package startup;

import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class FileActions extends CreateNew{

		//Filename for createnew and launch classes
		public static String openFileName = "";
		
		public static void setFileName(String name){
			openFileName = name;
		}//End of setFileName
		
		public static String getFileName(){
			return openFileName;
		}//End of getFileName
	
		
		
		
		
		public static void openSemester(){
			
			//Initiates FileChooser to open semester
			JFrame fcFrame = new JFrame();
			JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + "/savedsemesters");
			chooser.setDialogTitle("Open Semester Folder");
			chooser.setAcceptAllFileFilterUsed(false);
			UIManager.put("FileChooser.readOnly", Boolean.TRUE);
			chooser.setFileView(new FileView() {
			    @Override
			    public Boolean isTraversable(File f) {
			         return (new File(System.getProperty("user.dir") + "/savedsemesters")).equals(f);
			    }
			});
			
			int userSelection = chooser.showOpenDialog(fcFrame);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File semesterFile = chooser.getSelectedFile();
				setFileName(semesterFile.getName());
			}//End of if
			else {
				setFileName(null);
			}//End of else
			
		}//End of open semester
		
		public static void saveSemester(){
		
			//Check if new semester can be created be added to the layout by checking if categories only add up to 100
			double total = 0;
			for(int i = indexHold; i < centerBox.getComponentCount(); i++){
				
				//Add totals and prevent adding category if a field is blank or not a number
				if(utilities.Misc.isANumber(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText())){
					total += Double.parseDouble(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText());
				}//End of if
				else{
					utilities.Misc.errorMessage("Can't create semester! Recently added weight isn't filled in or isn't a numerical value.");
					return;
				}//End of else
				
				//Also prevents a category with no name from being added
				if(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(1)).getText().equals("")){
					utilities.Misc.errorMessage("Can't create semester! Recently added category has no name.");
					return;
				}//End of if
				
			}//End of for
		
			//Only crate new semester if total weights < 100, both course code and title fields aren't empty, and semester has a name
			if(total != 100){
				utilities.Misc.errorMessage("Can't create semester! Weights for any class must add up to exactly 100.");
			}//End of if
			else if(((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).getText().equals("") || ((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).getText().equals("")){
				utilities.Misc.errorMessage("Can't create semester! Both class code and title fields must be filled in.");
			}//End of else if
			else if(createField.getText().equals("")){
				utilities.Misc.errorMessage("Can't create semester! New semester must have a name.");
			}//End of else if
			else{
			
				//Disables remaining fields
				((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).setEditable(false);
				((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).setEditable(false);
				((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(false);
				((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(false);
				createField.setEditable(false);
				
				//Initiates FileChooser to save semester
				JFrame fcFrame = new JFrame();
				JFileChooser chooser = new JFileChooser(System.getProperty("user.dir") + "/savedsemesters");
				chooser.setDialogTitle("Save Semester Folder");
				chooser.setAcceptAllFileFilterUsed(false);
				UIManager.put("FileChooser.readOnly", Boolean.TRUE);
				chooser.setFileView(new FileView() {
				    @Override
				    public Boolean isTraversable(File f) {
				         return (new File(System.getProperty("user.dir") + "/savedsemesters")).equals(f);
				    }
				});
				
				//Once folder is named, create folder and all of its subfiles
				int userSelection = chooser.showSaveDialog(fcFrame);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    
					//Write and save file
					File semesterFile = chooser.getSelectedFile();
					try {
						
						//Create folder for subfiles
					    Path folderPath = Paths.get("savedsemesters/" + semesterFile.getName());
					  
					    //Throw error if file does exist to avoid confusion
						if(Files.exists(folderPath)){
							((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(true);
							((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(true);
							((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).setEditable(true);
							((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).setEditable(true);
							createField.setEditable(true);
							setFileName(null);
							utilities.Misc.errorMessage("Cannot create files! Folder name already exists.");
							return;
						}//End of if
					    Files.createDirectories(folderPath);
					    
					    //Create array for writing master files
					    String[] masterWrite = new String[(centerBox.getComponentCount()*3)+(classCount+1)];
					    masterWrite[0] = createField.getText();
					    int mw = 1;
					    
					    //Creates subfiles and subfolders based off classes user inputed onto GUI
					    int classes = 0;
					    int categories = 0;
					    for(int i = 0; i < centerBox.getComponentCount(); i++){
					    	
					    	//Adds folder for class
					    	if( !(utilities.Misc.isANumber(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText())) ){
					    		classes++;
					    		categories = 0;
					    		String subFolderName = "savedsemesters/" + semesterFile.getName() + "/" + "class" + classes;
					    		masterWrite[mw] = "";
					    		masterWrite[mw+1] = ((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(1)).getText();
					    		masterWrite[mw+2] = ((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText();
					    		masterWrite[mw+3] = "0";
					    		mw += 4;
					    		Path subFolderPath = Paths.get(subFolderName);
							    Files.createDirectories(subFolderPath);
					    	}//End of if
					    	
					    	//Adds file for category
					    	else{
					    		categories++;
					    		String subFileName = "savedsemesters/" + semesterFile.getName() + "/" + "class" + classes + "/" + "category" + categories + ".txt";
					    		masterWrite[mw] = ((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(1)).getText();
					    		masterWrite[mw+1] = ((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText();
					    		masterWrite[mw+2] = subFileName;
					    		mw += 3;
					    		FileWriter fw = new FileWriter(new File(subFileName));
					    		fw.close();
					    	}//End of else
					    	
					    }//End of for
					    
					    //Create master files
					    int i = 1;
					    int classCount = 0;
					    FileWriter masterFw = new FileWriter(new File("savedsemesters/" + semesterFile.getName() + "/" + "master.txt"));
					    masterFw.write(masterWrite[i-1] + "\n");
					    masterFw.write("class1.txt");
					    masterFw.close();
					    
				    	//Loop through array to create files
					    while(i  < masterWrite.length){
					    	
					    	//Create new file for new class
					    	if(masterWrite[i] == ""){
					    		classCount++;
					    		FileWriter fw = new FileWriter(new File("savedsemesters/" + semesterFile.getName() + "/" + "class" + classCount + ".txt"));
					    		fw.close();
					    	}//End of if
					    	
					    	//Reopen and append to current files
					    	else{
					    		
					    		//Reopen file
					    		FileWriter fw = new FileWriter(new File("savedsemesters/" + semesterFile.getName() + "/" + "class" + classCount + ".txt"), true);
					    		fw.write(masterWrite[i]);
					    		
					    		//Write master contents and \n to respective files
					    		if((i != masterWrite.length-1)){
					    			if(masterWrite[i+1] != ""){
					    				fw.write("\n");
					    			}//End of if
					    			else{
					    				fw.write("\n");
					    				fw.write("0");
						    		}//End of else
					    		}//End of if
					    		else{
					    			fw.write("\n");
					    			fw.write("0");
					    		}//End of else
					    		
					    		//Close filewriter
					    		fw.close();
					    		
					    	}//End of else
					    	i++;
					    	
					    }//End of while
					    
					    //Set filename for other classes to open
					    setFileName(semesterFile.getName());
					    
					} catch (Exception e) {
						((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(true);
						((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(true);
						((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).setEditable(true);
						((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).setEditable(true);
						createField.setEditable(true);
						setFileName(null);
						utilities.Misc.errorMessage("An error has occured when creating semester files! Please try again later.");
					}//End of try catch
					 
				
				}//End of if
			
				//Re-enables most recent fields since nothing will be saved yet
				else{
					((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(true);
					((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(true);
					((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).setEditable(true);
					((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).setEditable(true);
					createField.setEditable(true);
					setFileName(null);
				}//End of else
			
		}//End of else
		
	}//End of saveSemester
	
}//End of FileActions
