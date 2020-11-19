package application;

import java.io.*;
import java.util.*;
import javax.swing.table.*;

public class GuiActions {

public static void openFiles(String fileName, String firstClass, String semesterTitle){
		
		//Opens files for loading class information
		AppFrame.comboListen = false;
		try {
			
			//Open first class file
			Scanner firstClassScan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + fileName + "/" + firstClass));
			AppFrame.courseName.setText(firstClassScan.nextLine() + " - " + firstClassScan.nextLine());
			AppFrame.scaleField.setText(firstClassScan.nextLine());
			
			//Add categories to array list and get index of most recent category
			int recentIndex = 0;
			boolean endCheck = false;
			while(!endCheck){
				
				//Keeps creating category objects until the last line of file is detected
				String temp = firstClassScan.nextLine();
				if(utilities.Misc.isANumber(temp)){
					recentIndex = Integer.parseInt(temp);
					endCheck = true;
				}//End of if
				else {
					String v1 = temp;
					String v2 = firstClassScan.nextLine();
					String v3 = firstClassScan.nextLine();
					AppFrame.categoryList.add(new CategoryObject(v1, v2, v3));
				}//End of else
				
			}//End of while
			firstClassScan.close();
			
			//Fill GUI information for category opened and category combobox
			AppFrame.weightName.setText(AppFrame.categoryList.get(recentIndex).getName() + " - Weight: " + AppFrame.categoryList.get(recentIndex).getWeight() + "%");
			for(int i = 0; i < AppFrame.categoryList.size(); i++){
				AppFrame.categoryCombo.addItem(AppFrame.categoryList.get(i).getName());
			}//End of for
			AppFrame.categoryCombo.setSelectedIndex(recentIndex);
			
			///Fill GUI information for class opened and class combobox
			int classIndex = 0;
			int selectedClass = 0;
			boolean classCheck = true;
			while(classCheck){
				
				//Check all class files and record current class for class combo
				try {
					Scanner classComboScan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + fileName + "/class" + (classIndex+1) + ".txt"));
					String s1 = classComboScan.nextLine();
					String s2 = classComboScan.nextLine();
					AppFrame.classCombo.addItem(s1);
					if((s1 + " - " + s2).equals(AppFrame.courseName.getText())){
						selectedClass = AppFrame.classCombo.getItemCount()-1;
					}//End of if
					classIndex++;
					classComboScan.close();
				} catch (Exception e){
					classCheck = false;
				}//End of try catch
				
			}//End of while
			AppFrame.classCombo.setSelectedIndex(selectedClass);
			
			//Load current class and calculate values for current category
			FrameActions.addCurrentClass(AppFrame.categoryList.get(recentIndex).getFilePath());
			FrameActions.loadCategoryValues(Double.parseDouble(AppFrame.categoryList.get(recentIndex).getWeight()));
			
			//Calculate overall average grade
			String overallGrade = FrameActions.loadOverallGrade(recentIndex);
			DefaultTableModel valueModel = (DefaultTableModel) AppFrame.valueTable.getModel();
			valueModel.setValueAt(overallGrade, 0, 2);
			
			//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
			if(valueModel.getValueAt(0, 2).toString().equals("N/A")){
				valueModel.setValueAt(FrameActions.loadBestGrade(recentIndex), 1, 2);
				valueModel.setValueAt(FrameActions.loadWorstGrade(recentIndex), 2, 2);
				AppFrame.needButton.setEnabled(true);
			}//End of if
			else{
				valueModel.setValueAt("N/A", 1, 2);
				valueModel.setValueAt("N/A", 2, 2);
				AppFrame.needButton.setEnabled(false);
			}//End of else
			
			//Rewrite file for opening recent semester quickly
			FileWriter fw = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			fw.write(fileName + "\n");
			fw.write(semesterTitle + "\n");
			fw.write("" + AppFrame.quickCheck.isSelected());
			fw.close();
			
			AppFrame.prevCategory = AppFrame.categoryCombo.getSelectedIndex();
			AppFrame.prevClass = AppFrame.classCombo.getSelectedIndex();
			switchClass(AppFrame.classCombo.getSelectedIndex(), fileName);
			
			//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
			DefaultTableModel gradesModel = (DefaultTableModel) AppFrame.gradesTable.getModel();
			AppFrame.lastTable = new String[gradesModel.getRowCount()][3];
			for(int i = 0; i < AppFrame.lastTable.length; i++) {
				AppFrame.lastTable[i][0] = gradesModel.getValueAt(i, 1).toString();
				AppFrame.lastTable[i][1] = gradesModel.getValueAt(i, 2).toString();
				AppFrame.lastTable[i][2] = gradesModel.getValueAt(i, 3).toString();
			}//End of for
			AppFrame.lastScale = AppFrame.scaleField.getText();
			AppFrame.lastQuick = AppFrame.quickCheck.isSelected();
			
		} catch (Exception e) {
			utilities.Misc.errorMessage("Can't open semester files! Closing application.");
			System.exit(0);
		}//End of try catch
		AppFrame.comboListen = true;
		
	}//End of openFiles
	
	public static void clearGUI() {
		
		//Clear both table rows and both comboboxes
		AppFrame.comboListen = false;
		FrameActions.removeAllRows();
		int categoryCount = AppFrame.categoryCombo.getItemCount();
		for(int i = 0; i < categoryCount; i++){
			AppFrame.categoryCombo.removeItemAt(0);
		}//End of for
		int classCount = AppFrame.classCombo.getItemCount();
		for(int i = 0; i < classCount; i++){
			AppFrame.classCombo.removeItemAt(0);
		}//End of for
		AppFrame.comboListen = true;
		
	}//End of clearGUI
	
	
	
	
	
	public static void switchCategory(int index){
		
		//Switch category title and grades table to current selected category
		AppFrame.weightName.setText(AppFrame.categoryList.get(index).getName() + " - Weight: " + AppFrame.categoryList.get(index).getWeight() + "%");
		
		//Load current class and calculate values for current category
		FrameActions.removeAllRows();
		FrameActions.addCurrentClass(AppFrame.categoryList.get(index).getFilePath());
		FrameActions.loadCategoryValues(Double.parseDouble(AppFrame.categoryList.get(index).getWeight()));
		AppFrame.prevCategory = AppFrame.categoryCombo.getSelectedIndex();
		
		//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
		DefaultTableModel gradesModel = (DefaultTableModel) AppFrame.gradesTable.getModel();
		AppFrame.lastTable = new String[gradesModel.getRowCount()][3];
		for(int i = 0; i < AppFrame.lastTable.length; i++) {
			AppFrame.lastTable[i][0] = gradesModel.getValueAt(i, 1).toString();
			AppFrame.lastTable[i][1] = gradesModel.getValueAt(i, 2).toString();
			AppFrame.lastTable[i][2] = gradesModel.getValueAt(i, 3).toString();
		}//End of for
		AppFrame.lastScale = AppFrame.scaleField.getText();
		AppFrame.lastQuick = AppFrame.quickCheck.isSelected();
		
	}//End of switchCategory
	
	public static void switchClass(int index, String fileName){
		
		//Switch category title and grades table to current selected category
		AppFrame.comboListen = false;
		try {
			
			//Scan for class title
			Scanner classScan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + fileName + "/class" + (index+1) + ".txt"));
			AppFrame.courseName.setText(classScan.nextLine() + " - " + classScan.nextLine());
			AppFrame.scaleField.setText(classScan.nextLine());
			
			//Remove categories from last class
			int comboCount = AppFrame.categoryCombo.getItemCount();
			for(int i = 0; i < comboCount-1; i++){
				AppFrame.categoryCombo.removeItemAt(0);
			}
			for(int i = AppFrame.categoryList.size()-1; i >= 0; i--){
				AppFrame.categoryList.remove(i);
			}//End of for
			
			//Add categories to array list and get index of most recent category
			int recentIndex = 0;
			boolean endCheck = false;
			while(!endCheck){
				
				//Keeps creating category objects until the last line of file is detected
				String temp = classScan.nextLine();
				if(utilities.Misc.isANumber(temp)){
					recentIndex = Integer.parseInt(temp);
					endCheck = true;
				}//End of if
				else {
					String v1 = temp;
					String v2 = classScan.nextLine();
					String v3 = classScan.nextLine();
					AppFrame.categoryList.add(new CategoryObject(v1, v2, v3));
				}//End of else
				
			}//End of while
			classScan.close();
			
			//Fill GUI information for category opened and category combobox
			AppFrame.weightName.setText(AppFrame.categoryList.get(recentIndex).getName() + " - Weight: " + AppFrame.categoryList.get(recentIndex).getWeight() + "%");
			for(int i = 0; i < AppFrame.categoryList.size(); i++){
				AppFrame.categoryCombo.addItem(AppFrame.categoryList.get(i).getName());
			}//End of for
			AppFrame.categoryCombo.removeItemAt(0);
			AppFrame.categoryCombo.setSelectedIndex(recentIndex);
			
			//Load current class and calculate values for current category
			FrameActions.removeAllRows();
			FrameActions.addCurrentClass(AppFrame.categoryList.get(recentIndex).getFilePath());
			FrameActions.loadCategoryValues(Double.parseDouble(AppFrame.categoryList.get(recentIndex).getWeight()));
			
			//Calculate overall average grade
			String overallGrade = FrameActions.loadOverallGrade(recentIndex);
			DefaultTableModel valueModel = (DefaultTableModel) AppFrame.valueTable.getModel();
			valueModel.setValueAt(overallGrade, 0, 2);
			
			//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
			if(valueModel.getValueAt(0, 2).toString().equals("N/A")){
				valueModel.setValueAt(FrameActions.loadBestGrade(recentIndex), 1, 2);
				valueModel.setValueAt(FrameActions.loadWorstGrade(recentIndex), 2, 2);
				AppFrame.needButton.setEnabled(true);
			}//End of if
			else{
				valueModel.setValueAt("N/A", 1, 2);
				valueModel.setValueAt("N/A", 2, 2);
				AppFrame.needButton.setEnabled(false);
			}//End of else
			AppFrame.prevCategory = AppFrame.categoryCombo.getSelectedIndex();
			AppFrame.prevClass = AppFrame.classCombo.getSelectedIndex();
			
			//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
			DefaultTableModel gradesModel = (DefaultTableModel) AppFrame.gradesTable.getModel();
			AppFrame.lastTable = new String[gradesModel.getRowCount()][3];
			for(int i = 0; i < AppFrame.lastTable.length; i++) {
				AppFrame.lastTable[i][0] = gradesModel.getValueAt(i, 1).toString();
				AppFrame.lastTable[i][1] = gradesModel.getValueAt(i, 2).toString();
				AppFrame.lastTable[i][2] = gradesModel.getValueAt(i, 3).toString();
			}//End of for
			AppFrame.lastScale = AppFrame.scaleField.getText();
			AppFrame.lastQuick = AppFrame.quickCheck.isSelected();
			
		} catch (FileNotFoundException e) {
			
		}//End of try catch
		AppFrame.comboListen = true;
		
	}//End of switchClass
	
}//End of GuiActions
