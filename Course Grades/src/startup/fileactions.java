package startup;

import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class fileactions extends createnew{

		//Filename for createnew and launch classes
		public static String openfilename = "";
		
		public static void setFileName(String name){
			openfilename = name;
		}//End of setFileName
		
		public static String getFileName(){
			return openfilename;
		}//End of getFileName
	
		
		
		
		
		public static void openSemester(){
			
			//Initiates FileChooser to open semester
			JFrame fcframe = new JFrame();
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
			
			int userSelection = chooser.showOpenDialog(fcframe);
			if (userSelection == JFileChooser.APPROVE_OPTION) {
				File semesterfile = chooser.getSelectedFile();
				setFileName(semesterfile.getName());
			}//End of if
			else {
				setFileName(null);
			}//End of else
			
		}//End of open semester
		
		public static void saveSemester(){
		
			//Check if new semester can be created be added to the layout by checking if categories only add up to 100
			double total = 0;
			for(int i = indexhold; i < centerbox.getComponentCount(); i++){
				
				//Add totals and prevent adding category if a field is blank or not a number
				if(utilities.misc.isANumber(((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(3)).getText())){
					total += Double.parseDouble(((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(3)).getText());
				}//End of if
				else{
					utilities.misc.errorMessage("Can't create semester! Recently added weight isn't filled in or isn't a numerical value.");
					return;
				}//End of else
				
				//Also prevents a category with no name from being added
				if(((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(1)).getText().equals("")){
					utilities.misc.errorMessage("Can't create semester! Recently added category has no name.");
					return;
				}//End of if
				
			}//End of for
		
			//Only crate new semester if total weights < 100, both course code and title fields aren't empty, and semester has a name
			if(total != 100){
				utilities.misc.errorMessage("Can't create semester! Weights for any class must add up to exactly 100.");
			}//End of if
			else if(((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(3)).getText().equals("") || ((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(1)).getText().equals("")){
				utilities.misc.errorMessage("Can't create semester! Both class code and title fields must be filled in.");
			}//End of else if
			else if(createfield.getText().equals("")){
				utilities.misc.errorMessage("Can't create semester! New semester must have a name.");
			}//End of else if
			else{
			
				//Disables reaming fields
				((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(1)).setEditable(false);
				((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(3)).setEditable(false);
				((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(1)).setEditable(false);
				((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(3)).setEditable(false);
				createfield.setEditable(false);
				
				//Initiates FileChooser to save semester
				JFrame fcframe = new JFrame();
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
				int userSelection = chooser.showSaveDialog(fcframe);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    
					//Write and save file
					File semesterfile = chooser.getSelectedFile();
					try {
						
						//Create folder for subfiles
					    Path folderpath = Paths.get("savedsemesters/" + semesterfile.getName());
					  
					    //Throw error if file does exist to avoid confusion
						if(Files.exists(folderpath)){
							((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(1)).setEditable(true);
							((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(3)).setEditable(true);
							((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(1)).setEditable(true);
							((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(3)).setEditable(true);
							createfield.setEditable(true);
							setFileName(null);
							utilities.misc.errorMessage("Cannot create files! Folder name already exists.");
							return;
						}//End of if
					    Files.createDirectories(folderpath);
					    
					    //Create array for writing master files
					    String[] masterwrite = new String[(centerbox.getComponentCount()*3)+(classcount+1)];
					    masterwrite[0] = createfield.getText();
					    int mw = 1;
					    
					    //Creates subfiles and subfolders based off classes user inputed onto GUI
					    int classes = 0;
					    int categories = 0;
					    for(int i = 0; i < centerbox.getComponentCount(); i++){
					    	
					    	//Adds folder for class
					    	if( !(utilities.misc.isANumber(((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(3)).getText())) ){
					    		classes++;
					    		categories = 0;
					    		String subfoldername = "savedsemesters/" + semesterfile.getName() + "/" + "class" + classes;
					    		masterwrite[mw] = "";
					    		masterwrite[mw+1] = ((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(1)).getText();
					    		masterwrite[mw+2] = ((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(3)).getText();
					    		masterwrite[mw+3] = "0";
					    		mw += 4;
					    		Path subfolderpath = Paths.get(subfoldername);
							    Files.createDirectories(subfolderpath);
					    	}//End of if
					    	
					    	//Adds file for category
					    	else{
					    		categories++;
					    		String subfilename = "savedsemesters/" + semesterfile.getName() + "/" + "class" + classes + "/" + "category" + categories + ".txt";
					    		masterwrite[mw] = ((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(1)).getText();
					    		masterwrite[mw+1] = ((JTextField) ((JPanel) centerbox.getComponent(i)).getComponent(3)).getText();
					    		masterwrite[mw+2] = subfilename;
					    		mw += 3;
					    		FileWriter fw = new FileWriter(new File(subfilename));
					    		fw.close();
					    	}//End of else
					    	
					    }//End of for
					    
					    //Create master files
					    int i = 1;
					    int classcount = 0;
					    FileWriter masterfw = new FileWriter(new File("savedsemesters/" + semesterfile.getName() + "/" + "master.txt"));
					    masterfw.write(masterwrite[i-1] + "\n");
					    masterfw.write("class1.txt");
					    masterfw.close();
					    
				    	//Loop through array to create files
					    while(i  < masterwrite.length){
					    	
					    	//Create new file for new class
					    	if(masterwrite[i] == ""){
					    		classcount++;
					    		FileWriter fw = new FileWriter(new File("savedsemesters/" + semesterfile.getName() + "/" + "class" + classcount + ".txt"));
					    		fw.close();
					    	}//End of if
					    	
					    	//Reopen and append to current files
					    	else{
					    		
					    		//Reopen file
					    		FileWriter fw = new FileWriter(new File("savedsemesters/" + semesterfile.getName() + "/" + "class" + classcount + ".txt"), true);
					    		fw.write(masterwrite[i]);
					    		
					    		//Write master contents and \n to respective files
					    		if((i != masterwrite.length-1)){
					    			if(masterwrite[i+1] != ""){
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
					    setFileName(semesterfile.getName());
					    
					} catch (Exception e) {
						((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(1)).setEditable(true);
						((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(3)).setEditable(true);
						((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(1)).setEditable(true);
						((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(3)).setEditable(true);
						createfield.setEditable(true);
						setFileName(null);
						utilities.misc.errorMessage("An error has occured when creating semester files! Please try again later.");
					}//End of try catch
					 
				
				}//End of if
			
				//Reenables most recent fields since nothing will be saved yet
				else{
					((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(1)).setEditable(true);
					((JTextField) ((JPanel) centerbox.getComponent(centerbox.getComponentCount()-1)).getComponent(3)).setEditable(true);
					((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(1)).setEditable(true);
					((JTextField) ((JPanel) centerbox.getComponent(indexhold-1)).getComponent(3)).setEditable(true);
					createfield.setEditable(true);
					setFileName(null);
				}//End of else
			
		}//End of else
		
	}//End of saveSemester
	
}//End of fileactions
