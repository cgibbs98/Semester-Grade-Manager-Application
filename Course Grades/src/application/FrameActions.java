package application;

import java.io.*;
import java.util.*;
import javax.swing.table.*;

public class FrameActions extends AppFrame{

	public FrameActions(String fileName, boolean quickCheckValue) {
		super(fileName, quickCheckValue);
	}//End of FrameActions

	
	
	
	
	public static void addGrade(String s1, String s2, String s3){
		
		//Adds row to table
		DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
		model.addRow(new Object[]{gradesTable.getRowCount()+1, s1, s2, s3});
		
	}//End of addGrade
	
	public static void addEmptyGrade(){
		
		//Adds row to table
		DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
		model.addRow(new Object[]{gradesTable.getRowCount()+1, "", "", ""});
		
	}//End of addEmptyGrade
	
	public static void removeAllRows(){
		
		//Removes all rows from table
		DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
		for(int i = model.getRowCount()-1; i >= 0; i--){
			model.removeRow(i);
		}//End of for
		
	}//End of removeAllRows
	
	public static void removeGrades(){
		
		//Removes all empty rows from table
		DefaultTableModel model = (DefaultTableModel) gradesTable.getModel();
		for(int i = model.getRowCount()-1; i >= 0; i--){
			if(model.getValueAt(i, 1).equals("") && model.getValueAt(i, 2).equals("") && model.getValueAt(i, 3).equals("")){
				model.removeRow(i);
			}//End of if
		}//End of for
		
		//Assigns new ids to remaining rows to match count
		for(int i = model.getRowCount()-1; i >= 0; i--){
			model.setValueAt(i+1, i, 0);
		}//End of for
		
	}//End of addGrade
	
	public static void loadCategoryValues(double weight){
		
		//Calculates current stats for table
		DefaultTableModel gradesModel = (DefaultTableModel) gradesTable.getModel();
		DefaultTableModel valueModel = (DefaultTableModel) valueTable.getModel();
		
		//Set N/A to category fields if no rows exist
		if(gradesModel.getRowCount() == 0){
			valueModel.setValueAt("N/A", 3, 2);
			valueModel.setValueAt("N/A", 4, 2);
			valueModel.setValueAt("N/A", 5, 2);
			valueModel.setValueAt("N/A", 6, 2);
		}//End of if
		
		//Calculate values based on table values
		else{
			
			//Loop through table to get values
			double earned = 0.0;
			double possible = 0.0;
			for(int i = 0; i < gradesModel.getRowCount(); i++){
				earned += Double.parseDouble(gradesModel.getValueAt(i, 2).toString());
				possible += Double.parseDouble(gradesModel.getValueAt(i, 3).toString());
			}//End of for
			double percent = earned/possible;
			double weighted = percent*weight;
			
			//Set category fields to respective values
			valueModel.setValueAt(earned, 3, 2);
			valueModel.setValueAt(possible, 4, 2);
			valueModel.setValueAt(percent*100, 5, 2);
			valueModel.setValueAt(weighted, 6, 2);
			
		}//End of else
		
	}//End of loadCategoryValues
	
	public static void addCurrentClass(String filePath){
		
		//Load grades for current class
		try {
			Scanner fillTableScan = new Scanner(new File(filePath));
			while(fillTableScan.hasNext()){
				String[] gradeRow = fillTableScan.nextLine().split(",");
				FrameActions.addGrade(gradeRow[0], gradeRow[1], gradeRow[2]);
			}//End of while
			fillTableScan.close();
		} catch (Exception e) {
			
		}//End of try catch
		
	}//End of addCurrentClass
	
	public static void save(int categoryId, int classId){
		
		//Checks to make sure no empty fields including extra credit/scale field will be written existing file and that total points possible isn't 0
		DefaultTableModel gradesModel = (DefaultTableModel) gradesTable.getModel();
		double total = 0.0;
		for(int i = 0; i < gradesModel.getRowCount(); i++){
			if( gradesModel.getValueAt(i, 1).equals("") || gradesModel.getValueAt(i, 2).equals("") || gradesModel.getValueAt(i, 3).equals("") || 
			!(utilities.Misc.isANumber(gradesModel.getValueAt(i, 2).toString())) || !(utilities.Misc.isANumber(gradesModel.getValueAt(i, 3).toString())) ){
				utilities.Misc.errorMessage("Can't save grades! Blank field in grades table detected or points field is not a number.");
				return;
			}//End of if
			total += Double.parseDouble(gradesModel.getValueAt(i, 3).toString());
		}//End of for
		if( !(utilities.Misc.isANumber(scaleField.getText())) ){
			utilities.Misc.errorMessage("Can't save grades! Extra Credit/Scale field is blank or not a number.");
			return;
		}//End of if
		if(total <= 0){
			utilities.Misc.errorMessage("Can't save grades! Points possible must not be 0 or a negative value.");
			return;
		}//End of if
		
		//Writes updated table to existing category file
		try {
			FileWriter categoryScanner = new FileWriter(new File(categoryList.get(categoryId).getFilePath()));
			for(int i = 0; i < gradesModel.getRowCount(); i++){
				categoryScanner.write(gradesModel.getValueAt(i, 1) + "," + gradesModel.getValueAt(i, 2) + "," + gradesModel.getValueAt(i, 3));
				if(i != gradesModel.getRowCount()-1){
					categoryScanner.write("\n");
				}//End of if
			}//End of for
			categoryScanner.close();
		} catch (IOException e) {
			
		}//End of try catch
		
		//Updates quicklaunch file to determine whether the launch window can be used or not
		String quickSemester = "";
		String quickTitle = "";
		try {
			Scanner quickScan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quickSemester = quickScan.nextLine();
			quickTitle = quickScan.nextLine();
			quickScan.close();
			FileWriter quickWrite = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quickWrite.write(quickSemester + "\n");
			quickWrite.write(quickTitle + "\n");
			quickWrite.write("" + AppFrame.quickCheck.isSelected());
			quickWrite.close();
		} catch (Exception e) {
			
		}//End of try catch
		
		//Update master and class files to determine which class and category gets shown during launch
		try {
			
			//Master file
			String classFile = "class" + (classId+1) + ".txt";
			FileWriter masterWrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quickSemester + "/master.txt"));
			masterWrite.write(quickTitle + "\n");
			masterWrite.write(classFile);
			masterWrite.close();
			
			//Class file
			ArrayList<String> classLines = new ArrayList();
			Scanner classScan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + quickSemester + "/" + classFile));
			while(classScan.hasNext()){
				classLines.add(classScan.nextLine());
			}//End of while
			classScan.close();
			classLines.set(2, scaleField.getText());
			classLines.set((classLines.size()-1), "" + categoryId);
			FileWriter classWrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quickSemester + "/" + classFile));
			for(int i = 0; i < classLines.size(); i++){
				classWrite.write(classLines.get(i));
				if(i != classLines.size()-1){
					classWrite.write("\n");
				}//End of if
			}//End of for
			classWrite.close();
			
		} catch (Exception e) {
			
		}//End of try catch
		
		
		
	}//End of save
	
	public static void updateAndSave(){
		
		//Checks to make sure no empty fields including extra credit/scale field will be written existing file and that total points possible isn't 0
		DefaultTableModel gradesModel = (DefaultTableModel) gradesTable.getModel();
		double total = 0.0;
		for(int i = 0; i < gradesModel.getRowCount(); i++){
			if( gradesModel.getValueAt(i, 1).equals("") || gradesModel.getValueAt(i, 2).equals("") || gradesModel.getValueAt(i, 3).equals("") || 
			!(utilities.Misc.isANumber(gradesModel.getValueAt(i, 2).toString())) || !(utilities.Misc.isANumber(gradesModel.getValueAt(i, 3).toString())) ){
				utilities.Misc.errorMessage("Can't update and save grades! Blank field in grades table detected or points field is not a number.");
				return;
			}//End of if
			total += Double.parseDouble(gradesModel.getValueAt(i, 3).toString());
		}//End of for
		if( !(utilities.Misc.isANumber(scaleField.getText())) ){
			utilities.Misc.errorMessage("Can't update and save grades! Extra Credit/Scale field is blank or not a number.");
			return;
		}//End of if
		if(total <= 0){
			utilities.Misc.errorMessage("Can't update and save grades! Points possible must not be 0 or a negative value.");
			return;
		}//End of if
		
		//Updates grade
		loadCategoryValues(Double.parseDouble(categoryList.get(categoryCombo.getSelectedIndex()).getWeight()));
		String overallGrade = loadOverallGrade(categoryCombo.getSelectedIndex());
		DefaultTableModel valueModel = (DefaultTableModel) valueTable.getModel();
		valueModel.setValueAt(overallGrade, 0, 2);
		
		//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
		if(valueModel.getValueAt(0, 2).toString().equals("N/A")){
			valueModel.setValueAt(FrameActions.loadBestGrade(categoryCombo.getSelectedIndex()), 1, 2);
			valueModel.setValueAt(FrameActions.loadWorstGrade(categoryCombo.getSelectedIndex()), 2, 2);
			needButton.setEnabled(true);
		}//End of if
		else{
			valueModel.setValueAt("N/A", 1, 2);
			valueModel.setValueAt("N/A", 2, 2);
			needButton.setEnabled(false);
		}//End of else
		
		//Writes updated table to existing category file
		try {
			FileWriter categoryScanner = new FileWriter(new File(categoryList.get(categoryCombo.getSelectedIndex()).getFilePath()));
			for(int i = 0; i < gradesModel.getRowCount(); i++){
				categoryScanner.write(gradesModel.getValueAt(i, 1) + "," + gradesModel.getValueAt(i, 2) + "," + gradesModel.getValueAt(i, 3));
				if(i != gradesModel.getRowCount()-1){
					categoryScanner.write("\n");
				}//End of if
			}//End of for
			categoryScanner.close();
		} catch (IOException e) {
			
		}//End of try catch
		
		//Updates quicklaunch file to determine whether the launch window can be used or not
		String quickSemester = "";
		String quickTitle = "";
		try {
			Scanner quickScan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quickSemester = quickScan.nextLine();
			quickTitle = quickScan.nextLine();
			quickScan.close();
			FileWriter quickWrite = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quickWrite.write(quickSemester + "\n");
			quickWrite.write(quickTitle + "\n");
			quickWrite.write("" + AppFrame.quickCheck.isSelected());
			quickWrite.close();
		} catch (Exception e) {
			
		}//End of try catch
		
		//Update master and class files to determine which class and category gets shown during launch
		try {
			
			//Master file
			String classFile = "class" + (classCombo.getSelectedIndex()+1) + ".txt";
			FileWriter masterWrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quickSemester + "/master.txt"));
			masterWrite.write(quickTitle + "\n");
			masterWrite.write(classFile);
			masterWrite.close();
			
			//Class file
			ArrayList<String> classLines = new ArrayList();
			Scanner classScan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + quickSemester + "/" + classFile));
			while(classScan.hasNext()){
				classLines.add(classScan.nextLine());
			}//End of while
			classScan.close();
			classLines.set(2, scaleField.getText());
			classLines.set((classLines.size()-1), "" + categoryCombo.getSelectedIndex());
			FileWriter classWrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quickSemester + "/" + classFile));
			for(int i = 0; i < classLines.size(); i++){
				classWrite.write(classLines.get(i));
				if(i != classLines.size()-1){
					classWrite.write("\n");
				}//End of if
			}//End of for
			classWrite.close();
			
			//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
			DefaultTableModel newModel = (DefaultTableModel) AppFrame.gradesTable.getModel();
			AppFrame.lastTable = new String[newModel.getRowCount()][3];
			for(int i = 0; i < AppFrame.lastTable.length; i++) {
				AppFrame.lastTable[i][0] = newModel.getValueAt(i, 1).toString();
				AppFrame.lastTable[i][1] = newModel.getValueAt(i, 2).toString();
				AppFrame.lastTable[i][2] = newModel.getValueAt(i, 3).toString();
			}//End of for
			AppFrame.lastScale = AppFrame.scaleField.getText();
			AppFrame.lastQuick = AppFrame.quickCheck.isSelected();
			
		} catch (Exception e) {
			
		}//End of try catch
		
	}//End of updateAndSave
	
	
	
	
	
	public static String loadOverallGrade(int in){
		
		//Iterate loop until grade is calculated or an empty file is found
		double grade = 0.0;
		DefaultTableModel valueModel = (DefaultTableModel) valueTable.getModel();
		for(int i = 0; i < categoryList.size(); i++){
			
			//If current category is loaded already
			if(i == in){
				if(valueModel.getValueAt(6, 2).equals("N/A")){
					return "N/A";
				}//End of if
				else{
					grade += Double.parseDouble(valueModel.getValueAt(6, 2).toString());
				}//End of else
			}//End of if
			
			//If current category needs to be opened
			else{
				try {
					
					//If file is empty
					Scanner sc = new Scanner(new File(categoryList.get(i).getFilePath()));
					if(!(sc.hasNext())){
						sc.close();
						return "N/A";
					}//End of if
					
					//If file has atleast 1 grade
					else{
						double earned = 0.0;
						double possible = 0.0;
						while(sc.hasNext()){
							String[] gradeRow = sc.nextLine().split(",");
							earned += Double.parseDouble(gradeRow[1]);
							possible += Double.parseDouble(gradeRow[2]);
						}//End of while
						double percent = earned/possible;
						double weighted = percent*Double.parseDouble(categoryList.get(i).getWeight());
						grade += weighted;
					}//End of else
					sc.close();
					
				} catch (Exception e) {
					
				}//End of try catch
				
			}//End of else
			
		}//End of for
		
		grade += Double.parseDouble(scaleField.getText());
		return "" + grade;
		
	}//End of loadOverallGrade
	
	public static String loadBestGrade(int in){
		
		//Iterate loop until grade is calculated or an empty file is found
		double grade = 0.0;
		DefaultTableModel valueModel = (DefaultTableModel) valueTable.getModel();
		for(int i = 0; i < categoryList.size(); i++){
			
			//If current category is loaded already
			if(i == in){
				if(valueModel.getValueAt(6, 2).equals("N/A")){
					grade += Double.parseDouble(categoryList.get(i).getWeight());
				}//End of if
				else{
					grade += Double.parseDouble(valueModel.getValueAt(6, 2).toString());
				}//End of else
			}//End of if
			
			//If current category needs to be opened
			else{
				try {
					
					//If file is empty
					Scanner sc = new Scanner(new File(categoryList.get(i).getFilePath()));
					if(!(sc.hasNext())){
						sc.close();
						grade += Double.parseDouble(categoryList.get(i).getWeight());
					}//End of if
					
					//If file has atleast 1 grade
					else{
						double earned = 0.0;
						double possible = 0.0;
						while(sc.hasNext()){
							String[] gradeRow = sc.nextLine().split(",");
							earned += Double.parseDouble(gradeRow[1]);
							possible += Double.parseDouble(gradeRow[2]);
						}//End of while
						double percent = earned/possible;
						double weighted = percent*Double.parseDouble(categoryList.get(i).getWeight());
						grade += weighted;
					}//End of else
					sc.close();
					
				} catch (Exception e) {
					
				}//End of try catch
				
			}//End of else
			
		}//End of for
		
		grade += Double.parseDouble(scaleField.getText());
		return "" + grade;
		
	}//End of loadBestGrade

	public static String loadWorstGrade(int in){
		
		//Iterate loop until grade is calculated or an empty file is found
		double grade = 0.0;
		DefaultTableModel valueModel = (DefaultTableModel) valueTable.getModel();
		for(int i = 0; i < categoryList.size(); i++){
			
			//If current category is loaded already
			if(i == in){
				if(valueModel.getValueAt(6, 2).equals("N/A")){
					grade += 0;
				}//End of if
				else{
					grade += Double.parseDouble(valueModel.getValueAt(6, 2).toString());
				}//End of else
			}//End of if
			
			//If current category needs to be opened
			else{
				try {
					
					//If file is empty
					Scanner sc = new Scanner(new File(categoryList.get(i).getFilePath()));
					if(!(sc.hasNext())){
						sc.close();
						grade += 0;
					}//End of if
					
					//If file has atleast 1 grade
					else{
						double earned = 0.0;
						double possible = 0.0;
						while(sc.hasNext()){
							String[] gradeRow = sc.nextLine().split(",");
							earned += Double.parseDouble(gradeRow[1]);
							possible += Double.parseDouble(gradeRow[2]);
						}//End of while
						double percent = earned/possible;
						double weighted = percent*Double.parseDouble(categoryList.get(i).getWeight());
						grade += weighted;
					}//End of else
					sc.close();
					
				} catch (Exception e) {
					
				}//End of try catch
				
			}//End of else
			
		}//End of for
		
		grade += Double.parseDouble(scaleField.getText());
		return "" + grade;
		
	}//End of loadWorstGrade
	
}//End of FrameActions
