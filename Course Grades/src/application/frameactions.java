package application;

import java.io.*;
import java.util.*;
import javax.swing.table.*;

public class frameactions extends appframe{

	public frameactions(String filename, boolean quickcheckvalue) {
		super(filename, quickcheckvalue);
	}//End of frmeactions

	
	
	
	
	public static void addGrade(String s1, String s2, String s3){
		
		//Adds row to table
		DefaultTableModel model = (DefaultTableModel) gradestable.getModel();
		model.addRow(new Object[]{gradestable.getRowCount()+1, s1, s2, s3});
		
	}//End of addGrade
	
	public static void addEmptyGrade(){
		
		//Adds row to table
		DefaultTableModel model = (DefaultTableModel) gradestable.getModel();
		model.addRow(new Object[]{gradestable.getRowCount()+1, "", "", ""});
		
	}//End of addEmptyGrade
	
	public static void removeAllRows(){
		
		//Removes all rows from table
		DefaultTableModel model = (DefaultTableModel) gradestable.getModel();
		for(int i = model.getRowCount()-1; i >= 0; i--){
			model.removeRow(i);
		}//End of for
		
	}//End of removeAllRows
	
	public static void removeGrades(){
		
		//Removes all empty rows from table
		DefaultTableModel model = (DefaultTableModel) gradestable.getModel();
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
		DefaultTableModel gradesmodel = (DefaultTableModel) gradestable.getModel();
		DefaultTableModel valuemodel = (DefaultTableModel) valuetable.getModel();
		
		//Set N/A to category fields if no rows exist
		if(gradesmodel.getRowCount() == 0){
			valuemodel.setValueAt("N/A", 3, 2);
			valuemodel.setValueAt("N/A", 4, 2);
			valuemodel.setValueAt("N/A", 5, 2);
			valuemodel.setValueAt("N/A", 6, 2);
		}//End of if
		
		//Calculate values based on table values
		else{
			
			//Loop through table to get values
			double earned = 0.0;
			double possible = 0.0;
			for(int i = 0; i < gradesmodel.getRowCount(); i++){
				earned += Double.parseDouble(gradesmodel.getValueAt(i, 2).toString());
				possible += Double.parseDouble(gradesmodel.getValueAt(i, 3).toString());
			}//End of for
			double percent = earned/possible;
			double weighted = percent*weight;
			
			//Set category fields to respective values
			valuemodel.setValueAt(earned, 3, 2);
			valuemodel.setValueAt(possible, 4, 2);
			valuemodel.setValueAt(percent*100, 5, 2);
			valuemodel.setValueAt(weighted, 6, 2);
			
		}//End of else
		
	}//End of loadCategoryValues
	
	public static void addCurrentClass(String filepath){
		
		//Load grades for current class
		try {
			Scanner filltablescan = new Scanner(new File(filepath));
			while(filltablescan.hasNext()){
				String[] graderow = filltablescan.nextLine().split(",");
				frameactions.addGrade(graderow[0], graderow[1], graderow[2]);
			}//End of while
			filltablescan.close();
		} catch (Exception e) {
			
		}//End of try catch
		
	}//End of addCurrentClass
	
	public static void save(int categoryid, int classid){
		
		//Checks to make sure no empty fields including extra credit/scale field will be written existing file and that total points possible isn't 0
		DefaultTableModel gradesmodel = (DefaultTableModel) gradestable.getModel();
		double total = 0.0;
		for(int i = 0; i < gradesmodel.getRowCount(); i++){
			if( gradesmodel.getValueAt(i, 1).equals("") || gradesmodel.getValueAt(i, 2).equals("") || gradesmodel.getValueAt(i, 3).equals("") || 
			!(utilities.misc.isANumber(gradesmodel.getValueAt(i, 2).toString())) || !(utilities.misc.isANumber(gradesmodel.getValueAt(i, 3).toString())) ){
				utilities.misc.errorMessage("Can't save grades! Blank field in grades table detected or points field is not a number.");
				return;
			}//End of if
			total += Double.parseDouble(gradesmodel.getValueAt(i, 3).toString());
		}//End of for
		if( !(utilities.misc.isANumber(scalefield.getText())) ){
			utilities.misc.errorMessage("Can't save grades! Extra Credit/Scale field is blank or not a number.");
			return;
		}//End of if
		if(total <= 0){
			utilities.misc.errorMessage("Can't save grades! Points possible must not be 0 or a negative value.");
			return;
		}//End of if
		
		//Writes updated table to existing category file
		try {
			FileWriter categoryscanner = new FileWriter(new File(categorylist.get(categoryid).getFilepath()));
			for(int i = 0; i < gradesmodel.getRowCount(); i++){
				categoryscanner.write(gradesmodel.getValueAt(i, 1) + "," + gradesmodel.getValueAt(i, 2) + "," + gradesmodel.getValueAt(i, 3));
				if(i != gradesmodel.getRowCount()-1){
					categoryscanner.write("\n");
				}//End of if
			}//End of for
			categoryscanner.close();
		} catch (IOException e) {
			
		}//End of try catch
		
		//Updates quicklaunch file to determine whether the launch window can be used or not
		String quicksemester = "";
		String quicktitle = "";
		try {
			Scanner quickscan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quicksemester = quickscan.nextLine();
			quicktitle = quickscan.nextLine();
			quickscan.close();
			FileWriter quickwrite = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quickwrite.write(quicksemester + "\n");
			quickwrite.write(quicktitle + "\n");
			quickwrite.write("" + appframe.quickcheck.isSelected());
			quickwrite.close();
		} catch (Exception e) {
			
		}//End of try catch
		
		//Update master and class files to determine which class and category gets shown during launch
		try {
			
			//Master file
			String classfile = "class" + (classid+1) + ".txt";
			FileWriter masterwrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quicksemester + "/master.txt"));
			masterwrite.write(quicktitle + "\n");
			masterwrite.write(classfile);
			masterwrite.close();
			
			//Class file
			ArrayList<String> classlines = new ArrayList();
			Scanner classscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + quicksemester + "/" + classfile));
			while(classscan.hasNext()){
				classlines.add(classscan.nextLine());
			}//End of while
			classscan.close();
			classlines.set(2, scalefield.getText());
			classlines.set((classlines.size()-1), "" + categoryid);
			FileWriter classwrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quicksemester + "/" + classfile));
			for(int i = 0; i < classlines.size(); i++){
				classwrite.write(classlines.get(i));
				if(i != classlines.size()-1){
					classwrite.write("\n");
				}//End of if
			}//End of for
			classwrite.close();
			
		} catch (Exception e) {
			
		}//End of try catch
		
		
		
	}//End of save
	
	public static void updateAndSave(){
		
		//Checks to make sure no empty fields including extra credit/scale field will be written existing file and that total points possible isn't 0
		DefaultTableModel gradesmodel = (DefaultTableModel) gradestable.getModel();
		double total = 0.0;
		for(int i = 0; i < gradesmodel.getRowCount(); i++){
			if( gradesmodel.getValueAt(i, 1).equals("") || gradesmodel.getValueAt(i, 2).equals("") || gradesmodel.getValueAt(i, 3).equals("") || 
			!(utilities.misc.isANumber(gradesmodel.getValueAt(i, 2).toString())) || !(utilities.misc.isANumber(gradesmodel.getValueAt(i, 3).toString())) ){
				utilities.misc.errorMessage("Can't update grades! Blank field in grades table detected or points field is not a number.");
				return;
			}//End of if
			total += Double.parseDouble(gradesmodel.getValueAt(i, 3).toString());
		}//End of for
		if( !(utilities.misc.isANumber(scalefield.getText())) ){
			utilities.misc.errorMessage("Can't update grades! Extra Credit/Scale field is blank or not a number.");
			return;
		}//End of if
		if(total <= 0){
			utilities.misc.errorMessage("Can't update grades! Points possible must not be 0 or a negative value.");
			return;
		}//End of if
		
		//Updates grade
		loadCategoryValues(Double.parseDouble(categorylist.get(categorycombo.getSelectedIndex()).getWeight()));
		String overallgrade = loadOverallGrade(categorycombo.getSelectedIndex());
		DefaultTableModel valuemodel = (DefaultTableModel) valuetable.getModel();
		valuemodel.setValueAt(overallgrade, 0, 2);
		
		//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
		if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
			valuemodel.setValueAt(frameactions.loadBestGrade(categorycombo.getSelectedIndex()), 1, 2);
			valuemodel.setValueAt(frameactions.loadWorstGrade(categorycombo.getSelectedIndex()), 2, 2);
			needbutton.setEnabled(true);
		}//End of if
		else{
			valuemodel.setValueAt("N/A", 1, 2);
			valuemodel.setValueAt("N/A", 2, 2);
			needbutton.setEnabled(false);
		}//End of else
		
		//Writes updated table to existing category file
		try {
			FileWriter categoryscanner = new FileWriter(new File(categorylist.get(categorycombo.getSelectedIndex()).getFilepath()));
			for(int i = 0; i < gradesmodel.getRowCount(); i++){
				categoryscanner.write(gradesmodel.getValueAt(i, 1) + "," + gradesmodel.getValueAt(i, 2) + "," + gradesmodel.getValueAt(i, 3));
				if(i != gradesmodel.getRowCount()-1){
					categoryscanner.write("\n");
				}//End of if
			}//End of for
			categoryscanner.close();
		} catch (IOException e) {
			
		}//End of try catch
		
		//Updates quicklaunch file to determine whether the launch window can be used or not
		String quicksemester = "";
		String quicktitle = "";
		try {
			Scanner quickscan = new Scanner(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quicksemester = quickscan.nextLine();
			quicktitle = quickscan.nextLine();
			quickscan.close();
			FileWriter quickwrite = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			quickwrite.write(quicksemester + "\n");
			quickwrite.write(quicktitle + "\n");
			quickwrite.write("" + appframe.quickcheck.isSelected());
			quickwrite.close();
		} catch (Exception e) {
			
		}//End of try catch
		
		//Update master and class files to determine which class and category gets shown during launch
		try {
			
			//Master file
			String classfile = "class" + (classcombo.getSelectedIndex()+1) + ".txt";
			FileWriter masterwrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quicksemester + "/master.txt"));
			masterwrite.write(quicktitle + "\n");
			masterwrite.write(classfile);
			masterwrite.close();
			
			//Class file
			ArrayList<String> classlines = new ArrayList();
			Scanner classscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + quicksemester + "/" + classfile));
			while(classscan.hasNext()){
				classlines.add(classscan.nextLine());
			}//End of while
			classscan.close();
			classlines.set(2, scalefield.getText());
			classlines.set((classlines.size()-1), "" + categorycombo.getSelectedIndex());
			FileWriter classwrite = new FileWriter(new File(System.getProperty("user.dir") + "/savedsemesters/" + quicksemester + "/" + classfile));
			for(int i = 0; i < classlines.size(); i++){
				classwrite.write(classlines.get(i));
				if(i != classlines.size()-1){
					classwrite.write("\n");
				}//End of if
			}//End of for
			classwrite.close();
			
		} catch (Exception e) {
			
		}//End of try catch
		
		//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
		DefaultTableModel newmodel = (DefaultTableModel) gradestable.getModel();
		appframe.lasttable = new String[newmodel.getRowCount()][3];
		for(int i = 0; i < lasttable.length; i++) {
			appframe.lasttable[i][0] = newmodel.getValueAt(i, 1).toString();
			appframe.lasttable[i][1] = newmodel.getValueAt(i, 2).toString();
			appframe.lasttable[i][2] = newmodel.getValueAt(i, 3).toString();
		}//End of for
		appframe.lastscale = scalefield.getText();
		appframe.lastquick = quickcheck.isSelected();
		
	}//End of updateAndSave
	
	
	
	
	
	public static String loadOverallGrade(int in){
		
		//Iterate loop until grade is calculated or an empty file is found
		double grade = 0.0;
		DefaultTableModel valuemodel = (DefaultTableModel) valuetable.getModel();
		for(int i = 0; i < categorylist.size(); i++){
			
			//If current category is loaded already
			if(i == in){
				if(valuemodel.getValueAt(6, 2).equals("N/A")){
					return "N/A";
				}//End of if
				else{
					grade += Double.parseDouble(valuemodel.getValueAt(6, 2).toString());
				}//End of else
			}//End of if
			
			//If current category needs to be opened
			else{
				try {
					
					//If file is empty
					Scanner sc = new Scanner(new File(categorylist.get(i).getFilepath()));
					if(!(sc.hasNext())){
						sc.close();
						return "N/A";
					}//End of if
					
					//If file has atleast 1 grade
					else{
						double earned = 0.0;
						double possible = 0.0;
						while(sc.hasNext()){
							String[] graderow = sc.nextLine().split(",");
							earned += Double.parseDouble(graderow[1]);
							possible += Double.parseDouble(graderow[2]);
						}//End of while
						double percent = earned/possible;
						double weighted = percent*Double.parseDouble(categorylist.get(i).getWeight());
						grade += weighted;
					}//End of else
					sc.close();
					
				} catch (Exception e) {
					
				}//End of try catch
				
			}//End of else
			
		}//End of for
		
		grade += Double.parseDouble(scalefield.getText());
		return "" + grade;
		
	}//End of loadOverallGrade
	
	public static String loadBestGrade(int in){
		
		//Iterate loop until grade is calculated or an empty file is found
		double grade = 0.0;
		DefaultTableModel valuemodel = (DefaultTableModel) valuetable.getModel();
		for(int i = 0; i < categorylist.size(); i++){
			
			//If current category is loaded already
			if(i == in){
				if(valuemodel.getValueAt(6, 2).equals("N/A")){
					grade += Double.parseDouble(categorylist.get(i).getWeight());
				}//End of if
				else{
					grade += Double.parseDouble(valuemodel.getValueAt(6, 2).toString());
				}//End of else
			}//End of if
			
			//If current category needs to be opened
			else{
				try {
					
					//If file is empty
					Scanner sc = new Scanner(new File(categorylist.get(i).getFilepath()));
					if(!(sc.hasNext())){
						sc.close();
						grade += Double.parseDouble(categorylist.get(i).getWeight());
					}//End of if
					
					//If file has atleast 1 grade
					else{
						double earned = 0.0;
						double possible = 0.0;
						while(sc.hasNext()){
							String[] graderow = sc.nextLine().split(",");
							earned += Double.parseDouble(graderow[1]);
							possible += Double.parseDouble(graderow[2]);
						}//End of while
						double percent = earned/possible;
						double weighted = percent*Double.parseDouble(categorylist.get(i).getWeight());
						grade += weighted;
					}//End of else
					sc.close();
					
				} catch (Exception e) {
					
				}//End of try catch
				
			}//End of else
			
		}//End of for
		
		grade += Double.parseDouble(scalefield.getText());
		return "" + grade;
		
	}//End of loadBestGrade

	public static String loadWorstGrade(int in){
		
		//Iterate loop until grade is calculated or an empty file is found
		double grade = 0.0;
		DefaultTableModel valuemodel = (DefaultTableModel) valuetable.getModel();
		for(int i = 0; i < categorylist.size(); i++){
			
			//If current category is loaded already
			if(i == in){
				if(valuemodel.getValueAt(6, 2).equals("N/A")){
					grade += 0;
				}//End of if
				else{
					grade += Double.parseDouble(valuemodel.getValueAt(6, 2).toString());
				}//End of else
			}//End of if
			
			//If current category needs to be opened
			else{
				try {
					
					//If file is empty
					Scanner sc = new Scanner(new File(categorylist.get(i).getFilepath()));
					if(!(sc.hasNext())){
						sc.close();
						grade += 0;
					}//End of if
					
					//If file has atleast 1 grade
					else{
						double earned = 0.0;
						double possible = 0.0;
						while(sc.hasNext()){
							String[] graderow = sc.nextLine().split(",");
							earned += Double.parseDouble(graderow[1]);
							possible += Double.parseDouble(graderow[2]);
						}//End of while
						double percent = earned/possible;
						double weighted = percent*Double.parseDouble(categorylist.get(i).getWeight());
						grade += weighted;
					}//End of else
					sc.close();
					
				} catch (Exception e) {
					
				}//End of try catch
				
			}//End of else
			
		}//End of for
		
		grade += Double.parseDouble(scalefield.getText());
		return "" + grade;
		
	}//End of loadWorstGrade
	
}//End of frameactions
