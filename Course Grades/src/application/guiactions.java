package application;

import java.io.*;
import java.util.*;
import javax.swing.table.*;

public class guiactions {

public static void openFiles(String filename, String firstclass, String semestertitle){
		
		//Opens files for loading class information
		appframe.combolisten = false;
		try {
			
			//Open first class file
			Scanner firstclassscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/" + firstclass));
			appframe.coursename.setText(firstclassscan.nextLine() + " - " + firstclassscan.nextLine());
			appframe.scalefield.setText(firstclassscan.nextLine());
			
			//Add categories to array list and get index of most recent category
			int recentindex = 0;
			boolean endcheck = false;
			while(!endcheck){
				
				//Keeps creating category objects until the last line of file is detected
				String temp = firstclassscan.nextLine();
				if(utilities.misc.isANumber(temp)){
					recentindex = Integer.parseInt(temp);
					endcheck = true;
				}//End of if
				else {
					String v1 = temp;
					String v2 = firstclassscan.nextLine();
					String v3 = firstclassscan.nextLine();
					appframe.categorylist.add(new categoryobject(v1, v2, v3));
				}//End of else
				
			}//End of while
			firstclassscan.close();
			
			//Fill GUI information for category opened and category combobox
			appframe.weightname.setText(appframe.categorylist.get(recentindex).getName() + " - Weight: " + appframe.categorylist.get(recentindex).getWeight() + "%");
			for(int i = 0; i < appframe.categorylist.size(); i++){
				appframe.categorycombo.addItem(appframe.categorylist.get(i).getName());
			}//End of for
			appframe.categorycombo.setSelectedIndex(recentindex);
			
			///Fill GUI information for class opened and class combobox
			int classindex = 0;
			int selectedclass = 0;
			boolean classcheck = true;
			while(classcheck){
				
				//Check all class files and record current class for class combo
				try {
					Scanner classcomboscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/class" + (classindex+1) + ".txt"));
					String s1 = classcomboscan.nextLine();
					String s2 = classcomboscan.nextLine();
					appframe.classcombo.addItem(s1);
					if((s1 + " - " + s2).equals(appframe.coursename.getText())){
						selectedclass = appframe.classcombo.getItemCount()-1;
					}//End of if
					classindex++;
					classcomboscan.close();
				} catch (Exception e){
					classcheck = false;
				}//End of try catch
				
			}//End of while
			appframe.classcombo.setSelectedIndex(selectedclass);
			
			//Load current class and calculate values for current category
			frameactions.addCurrentClass(appframe.categorylist.get(recentindex).getFilepath());
			frameactions.loadCategoryValues(Double.parseDouble(appframe.categorylist.get(recentindex).getWeight()));
			
			//Calculate overall average grade
			String overallgrade = frameactions.loadOverallGrade(recentindex);
			DefaultTableModel valuemodel = (DefaultTableModel) appframe.valuetable.getModel();
			valuemodel.setValueAt(overallgrade, 0, 2);
			
			//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
			if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
				valuemodel.setValueAt(frameactions.loadBestGrade(recentindex), 1, 2);
				valuemodel.setValueAt(frameactions.loadWorstGrade(recentindex), 2, 2);
				appframe.needbutton.setEnabled(true);
			}//End of if
			else{
				valuemodel.setValueAt("N/A", 1, 2);
				valuemodel.setValueAt("N/A", 2, 2);
				appframe.needbutton.setEnabled(false);
			}//End of else
			
			//Rewrite file for opening recent semester quickly
			FileWriter fw = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			fw.write(filename + "\n");
			fw.write(semestertitle + "\n");
			fw.write("" + appframe.quickcheck.isSelected());
			fw.close();
			
			appframe.prevcategory = appframe.categorycombo.getSelectedIndex();
			appframe.prevclass = appframe.classcombo.getSelectedIndex();
			switchClass(appframe.classcombo.getSelectedIndex(), filename);
			
			//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
			DefaultTableModel gradesmodel = (DefaultTableModel) appframe.gradestable.getModel();
			appframe.lasttable = new String[gradesmodel.getRowCount()][3];
			for(int i = 0; i < appframe.lasttable.length; i++) {
				appframe.lasttable[i][0] = gradesmodel.getValueAt(i, 1).toString();
				appframe.lasttable[i][1] = gradesmodel.getValueAt(i, 2).toString();
				appframe.lasttable[i][2] = gradesmodel.getValueAt(i, 3).toString();
			}//End of for
			appframe.lastscale = appframe.scalefield.getText();
			appframe.lastquick = appframe.quickcheck.isSelected();
			
		} catch (Exception e) {
			utilities.misc.errorMessage("Can't open semester files! Closing application.");
			System.exit(0);
		}//End of try catch
		appframe.combolisten = true;
		
	}//End of openFiles
	
	public static void clearGUI() {
		
		//Clear both table rows and both comboboxes
		appframe.combolisten = false;
		frameactions.removeAllRows();
		int categorycount = appframe.categorycombo.getItemCount();
		for(int i = 0; i < categorycount; i++){
			appframe.categorycombo.removeItemAt(0);
		}//End of for
		int classcount = appframe.classcombo.getItemCount();
		for(int i = 0; i < classcount; i++){
			appframe.classcombo.removeItemAt(0);
		}//End of for
		appframe.combolisten = true;
		
	}//End of clearGUI
	
	
	
	
	
	public static void switchCategory(int index){
		
		//Switch category title and grades table to current selected category
		appframe.weightname.setText(appframe.categorylist.get(index).getName() + " - Weight: " + appframe.categorylist.get(index).getWeight() + "%");
		
		//Load current class and calculate values for current category
		frameactions.removeAllRows();
		frameactions.addCurrentClass(appframe.categorylist.get(index).getFilepath());
		frameactions.loadCategoryValues(Double.parseDouble(appframe.categorylist.get(index).getWeight()));
		appframe.prevcategory = appframe.categorycombo.getSelectedIndex();
		
		//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
		DefaultTableModel gradesmodel = (DefaultTableModel) appframe.gradestable.getModel();
		appframe.lasttable = new String[gradesmodel.getRowCount()][3];
		for(int i = 0; i < appframe.lasttable.length; i++) {
			appframe.lasttable[i][0] = gradesmodel.getValueAt(i, 1).toString();
			appframe.lasttable[i][1] = gradesmodel.getValueAt(i, 2).toString();
			appframe.lasttable[i][2] = gradesmodel.getValueAt(i, 3).toString();
		}//End of for
		appframe.lastscale = appframe.scalefield.getText();
		appframe.lastquick = appframe.quickcheck.isSelected();
		
	}//End of switchCategory
	
	public static void switchClass(int index, String filename){
		
		//Switch category title and grades table to current selected category
		appframe.combolisten = false;
		try {
			
			//Scan for class title
			Scanner classscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/class" + (index+1) + ".txt"));
			appframe.coursename.setText(classscan.nextLine() + " - " + classscan.nextLine());
			appframe.scalefield.setText(classscan.nextLine());
			
			//Remove categories from last class
			int combocount = appframe.categorycombo.getItemCount();
			for(int i = 0; i < combocount-1; i++){
				appframe.categorycombo.removeItemAt(0);
			}
			for(int i = appframe.categorylist.size()-1; i >= 0; i--){
				appframe.categorylist.remove(i);
			}//End of for
			
			//Add categories to array list and get index of most recent category
			int recentindex = 0;
			boolean endcheck = false;
			while(!endcheck){
				
				//Keeps creating category objects until the last line of file is detected
				String temp = classscan.nextLine();
				if(utilities.misc.isANumber(temp)){
					recentindex = Integer.parseInt(temp);
					endcheck = true;
				}//End of if
				else {
					String v1 = temp;
					String v2 = classscan.nextLine();
					String v3 = classscan.nextLine();
					appframe.categorylist.add(new categoryobject(v1, v2, v3));
				}//End of else
				
			}//End of while
			classscan.close();
			
			//Fill GUI information for category opened and category combobox
			appframe.weightname.setText(appframe.categorylist.get(recentindex).getName() + " - Weight: " + appframe.categorylist.get(recentindex).getWeight() + "%");
			for(int i = 0; i < appframe.categorylist.size(); i++){
				appframe.categorycombo.addItem(appframe.categorylist.get(i).getName());
			}//End of for
			appframe.categorycombo.removeItemAt(0);
			appframe.categorycombo.setSelectedIndex(recentindex);
			
			//Load current class and calculate values for current category
			frameactions.removeAllRows();
			frameactions.addCurrentClass(appframe.categorylist.get(recentindex).getFilepath());
			frameactions.loadCategoryValues(Double.parseDouble(appframe.categorylist.get(recentindex).getWeight()));
			
			//Calculate overall average grade
			String overallgrade = frameactions.loadOverallGrade(recentindex);
			DefaultTableModel valuemodel = (DefaultTableModel) appframe.valuetable.getModel();
			valuemodel.setValueAt(overallgrade, 0, 2);
			
			//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
			if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
				valuemodel.setValueAt(frameactions.loadBestGrade(recentindex), 1, 2);
				valuemodel.setValueAt(frameactions.loadWorstGrade(recentindex), 2, 2);
				appframe.needbutton.setEnabled(true);
			}//End of if
			else{
				valuemodel.setValueAt("N/A", 1, 2);
				valuemodel.setValueAt("N/A", 2, 2);
				appframe.needbutton.setEnabled(false);
			}//End of else
			appframe.prevcategory = appframe.categorycombo.getSelectedIndex();
			appframe.prevclass = appframe.classcombo.getSelectedIndex();
			
			//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
			DefaultTableModel gradesmodel = (DefaultTableModel) appframe.gradestable.getModel();
			appframe.lasttable = new String[gradesmodel.getRowCount()][3];
			for(int i = 0; i < appframe.lasttable.length; i++) {
				appframe.lasttable[i][0] = gradesmodel.getValueAt(i, 1).toString();
				appframe.lasttable[i][1] = gradesmodel.getValueAt(i, 2).toString();
				appframe.lasttable[i][2] = gradesmodel.getValueAt(i, 3).toString();
			}//End of for
			appframe.lastscale = appframe.scalefield.getText();
			appframe.lastquick = appframe.quickcheck.isSelected();
			
		} catch (FileNotFoundException e) {
			
		}//End of try catch
		appframe.combolisten = true;
		
	}//End of switchClass
	
}//End of guiactions
