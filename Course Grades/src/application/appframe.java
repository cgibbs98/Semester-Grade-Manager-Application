package application;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import startup.launch;

public class appframe extends JFrame{

	public static JTable gradestable;
	public static JTable valuetable;
	public JLabel coursename = new JLabel("");
	public static JTextField scalefield = new JTextField("");
	public JLabel weightname = new JLabel("Category" + " - Weight: " + "%");
	public static JComboBox categorycombo = new JComboBox();
	public static JComboBox classcombo = new JComboBox();
	public static JCheckBox quickcheck = new JCheckBox();
	public static JButton needbutton = new JButton("Check Points Needed");
	public static JButton addbutton = new JButton("Add New Grade");
	public static JButton removebutton = new JButton("Remove Empty Grades");
	public static JButton updatebutton = new JButton("Update and Save Grades");
	public static ArrayList<categoryobject> categorylist = new ArrayList();
	public static boolean savecheck = true;
	public static String[][] lasttable;
	public static String lastscale;
	public static boolean lastquick;
	
	public appframe(String filename, boolean quickcheckvalue){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/1.375);
		int h = (int) (screenSize.getHeight()/2.375);
		setSize(w, h);
		
		//Properties
		setTitle("Semester Grade Manager Application - ");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Container pane = getContentPane();
		
		//Menubar
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		JMenu helpmenu = new JMenu("Help");
		JMenuItem saveitem = new JMenuItem("Save");
		JMenuItem saveexititem = new JMenuItem("Save & Exit");
		JMenuItem exititem = new JMenuItem("Exit");
		JMenuItem aboutitem = new JMenuItem("About");
		filemenu.add(saveitem);
		filemenu.addSeparator();
		filemenu.add(saveexititem);
		filemenu.add(exititem);
		helpmenu.add(aboutitem);
		menubar.add(filemenu);
		menubar.add(helpmenu);
		setJMenuBar(menubar);
		
		//Grades Table
		String[] columns = new String[] {
	            "#", "Description", "Points Earned", "Points Possible"
	    };
		Object[][] data = new Object[][] {
        };
        //Create table
        gradestable = new JTable(new DefaultTableModel(data, columns)){
        	//Make ID non editable
        	@Override
        	public boolean isCellEditable(int row, int col) {
        	     return col != 0;
        	}
        };
        gradestable.getColumnModel().getColumn(0).setPreferredWidth(25);
        gradestable.getColumnModel().getColumn(1).setPreferredWidth(125);
        gradestable.getColumnModel().getColumn(2).setPreferredWidth(125);
        gradestable.getColumnModel().getColumn(3).setPreferredWidth(125);
        //Prevent Reordering and Resizing
        gradestable.getTableHeader().setReorderingAllowed(false);
        gradestable.getTableHeader().setResizingAllowed(false);
        gradestable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane gradestablepane = new JScrollPane(gradestable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gradestablepane.setPreferredSize(new Dimension(gradestable.getPreferredSize().width+17, 160));
        //Align Points Columns to Right
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        gradestable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        gradestable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		//Table Sorting
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(gradestable.getModel());
		gradestable.setRowSorter(sorter);
		Box eastbox = Box.createVerticalBox();
		eastbox.add(gradestablepane);
		
		
		
		
		
		//Raw Values Table
		String[] columns2 = new String[] {
		"#", "Raw Values", "Amount"
		};
		Object[][] data2 = new Object[][] {
            {"1", "Class - Overall Average Grade", ""},
            {"2", "Class - Best Possible Grade", ""},
            {"3", "Class - Worst Possible Grade", ""},
            {"4", "Category - Total Points Earned", ""},
            {"5", "Category - Total Points Possible", ""},
            {"6", "Category - Total Percentage (%)", ""},
            {"7", "Category - Weighted Points", ""},
        };
        //Create table
        valuetable = new JTable(new DefaultTableModel(data2, columns2)){
        	//Make ID non editable
        	@Override
        	public boolean isCellEditable(int row, int col) {
        	     return (col < 0);
        	}
        };
        valuetable.getColumnModel().getColumn(0).setPreferredWidth(25);
        valuetable.getColumnModel().getColumn(1).setPreferredWidth(250);
        valuetable.getColumnModel().getColumn(2).setPreferredWidth(75);
        //Prevent Reordering and Resizing
        valuetable.getTableHeader().setReorderingAllowed(false);
        valuetable.getTableHeader().setResizingAllowed(false);
        valuetable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane valuetablepane = new JScrollPane(valuetable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        valuetablepane.setPreferredSize(new Dimension(valuetable.getPreferredSize().width+17, valuetable.getPreferredSize().height));
        //Align Points Columns to Right
        DefaultTableCellRenderer rightRenderer2 = new DefaultTableCellRenderer();
        rightRenderer2.setHorizontalAlignment(JLabel.RIGHT);
        valuetable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer2);
		Box westbox = Box.createVerticalBox();
		westbox.add(valuetablepane);
		
		//Extra Credit/Scale
		JPanel scalepanel = new JPanel();
		JLabel scalelabel = new JLabel("Final Grade Extra Credit or Scale: ");
		scalefield.setPreferredSize(new Dimension(20, scalefield.getPreferredSize().height));
		scalefield.setHorizontalAlignment(SwingConstants.RIGHT);
		scalelabel.setToolTipText("Raises overall grade in course by set points and works for overall grade penalties with negative values as well. (Must save for changes to take effect)");
		scalefield.setToolTipText("Raises overall grade in course by set points and works for overall grade penalties with negative values as well. (Must save for changes to take effect)");
		scalepanel.add(scalelabel);
		scalepanel.add(scalefield);
		westbox.add(scalepanel);
		
		//Category
		JPanel categorypanel = new JPanel();
		JLabel categorylabel = new JLabel("Select Another Category: ");
		categorycombo.setPreferredSize(new Dimension(200, categorycombo.getPreferredSize().height));
		categorylabel.setToolTipText("Select another category to view (Must save current grades to access)");
		categorycombo.setToolTipText("Select another category to view (Must save current grades to access)");
		categorypanel.add(categorylabel);
		categorypanel.add(categorycombo);
		categorycombo.setEnabled(false);
		
        //Class
        JPanel classpanel = new JPanel();
        JLabel classlabel = new JLabel("Select Another Class: ");
        classcombo.setPreferredSize(new Dimension(150, classcombo.getPreferredSize().height));
        classlabel.setToolTipText("Select another class to view (Must save current grades to access)");
        classcombo.setToolTipText("Select another class to view (Must save current grades to access)");
        classpanel.add(classlabel);
        classpanel.add(classcombo);
        classcombo.setEnabled(false);
        
        //Adds Above 2 Components
        westbox.add(classpanel);
        pane.add(westbox, BorderLayout.WEST);
        eastbox.add(categorypanel);
        pane.add(eastbox, BorderLayout.EAST);
        
		//Name + Weight Type
		Box northbox = Box.createVerticalBox();
		JPanel coursepanel = new JPanel();
		coursepanel.add(coursename);
		northbox.add(coursepanel);
		JPanel weightpanel = new JPanel();
		weightpanel.add(weightname);
		northbox.add(weightpanel);
		pane.add(northbox, BorderLayout.NORTH);
		
		//Table Actions
		JPanel actionspanel = new JPanel();
		JLabel quicklabel = new JLabel("Open This Semester at Launch:");
		quickcheck.setSelected(quickcheckvalue);
		quicklabel.setToolTipText("Allows semester to be opened up immediately instead of showing a startup screen upon launch (Must save for changes to take effect)");
		quickcheck.setToolTipText("Allows semester to be opened up immediately instead of showing a startup screen upon launch (Must save for changes to take effect)");
		needbutton.setEnabled(false);
		actionspanel.add(quicklabel);
		actionspanel.add(quickcheck);
		actionspanel.add(new JLabel("          "));
		actionspanel.add(addbutton);
		actionspanel.add(removebutton);
		actionspanel.add(needbutton);
		actionspanel.add(updatebutton);
		addbutton.setToolTipText("Adds new row to add a new grade for a class");
		removebutton.setToolTipText("Removes empty rows from grades table");
		needbutton.setToolTipText("Allows you to find what percentage of points you need in catgories without grades to get a specific overall grade");
		updatebutton.setToolTipText("Updates grade in course and saves class information");
		pane.add(actionspanel, BorderLayout.SOUTH);
		
		//Action Listeners
		addbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				frameactions.addEmptyGrade();
			}  
		});
		removebutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				frameactions.removeGrades();
			}  
		});
		needbutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				gradestable.setEnabled(false);
				categorycombo.setEnabled(false);
				classcombo.setEnabled(false);
				addbutton.setEnabled(false);
				removebutton.setEnabled(false);
				updatebutton.setEnabled(false);
				needbutton.setEnabled(false);
				scalefield.setEnabled(false);
				pointsneeded.main(null);
			}  
		});
		updatebutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				frameactions.updateAndSave();
				categorycombo.setEnabled(true);
				classcombo.setEnabled(true);
			}  
		});
		openFiles(filename);
		categorycombo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switchCategory(categorycombo.getSelectedIndex());
		    	categorycombo.setEnabled(false);
				classcombo.setEnabled(false);
		    }
		});
		classcombo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	switchClass(classcombo.getSelectedIndex(), filename);
		    	categorycombo.setEnabled(false);
				classcombo.setEnabled(false);
		    }
		});
		quicklabel.addMouseListener(new MouseAdapter() {  
		    public void mouseClicked(MouseEvent e) {  
		    	quickcheck.setSelected(!(quickcheck.isSelected()));
		    }  
		});
		saveitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	frameactions.updateAndSave();
		    }
		});
		saveexititem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	frameactions.updateAndSave();
		    	System.exit(0);
		    }
		});
		exititem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(savecheck == true){
		    		savecheck = false;
		    		if(checkLastUpdated() == false){
		    			saveframe.main(0);
		    		}//End of if
		    		else{
		    			System.exit(0);
		    		}//End of else
		    	}//End of if
		    }
		});
		aboutitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	help.aboutframe.main(null);
		    }
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(savecheck == true){
		    		savecheck = false;
		    		if(checkLastUpdated() == false){
		    			saveframe.main(0);
		    		}//End of if
		    		else{
		    			System.exit(0);
		    		}//End of else
		    	}//End of if
			}
		});
		
		//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
		DefaultTableModel gradesmodel = (DefaultTableModel) gradestable.getModel();
		lasttable = new String[gradesmodel.getRowCount()][3];
		for(int i = 0; i < lasttable.length; i++) {
			lasttable[i][0] = gradesmodel.getValueAt(i, 1).toString();
			lasttable[i][1] = gradesmodel.getValueAt(i, 2).toString();
			lasttable[i][2] = gradesmodel.getValueAt(i, 3).toString();
		}//End of for
		lastscale = scalefield.getText();
		lastquick = quickcheck.isSelected();
		
	}//End of appframe

	public boolean checkLastUpdated(){
		
		//Check every editable value in the application and return false if something is different from last update, otherwise return true
		boolean b = true;
		if(!(lastquick == quickcheck.isSelected())){
			return b = false;
		}//End of if
		if(!(lastscale.equals(scalefield.getText()))){
			return b = false;
		}//End of else if
		DefaultTableModel gradesmodel = (DefaultTableModel) gradestable.getModel();
		if(!(lasttable.length == gradesmodel.getRowCount())){
			return b = false;
		}
		for(int i = 0; i < lasttable.length; i++) {
			if( !(lasttable[i][0].equals(gradesmodel.getValueAt(i, 1).toString())) || !(lasttable[i][1].equals(gradesmodel.getValueAt(i, 2).toString())) || !(lasttable[i][2].equals(gradesmodel.getValueAt(i, 3).toString())) ){
				return b = false;
			}//End of if
		}//End of for
		return b;
		
	}//End of checkLastUpdated
	
	
	
	
	
	public void openFiles(String filename){
		
		//Opens files for loading class information
		try {
			
			//Open initial master file with semester title and most recent class file
			Scanner masterscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/master.txt"));
			String semestertitle = masterscan.nextLine();
			setTitle(getTitle() + semestertitle);
			String firstclass = masterscan.nextLine();
			masterscan.close();
			
			//Open first class file
			Scanner firstclassscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/" + firstclass));
			coursename.setText(firstclassscan.nextLine() + " - " + firstclassscan.nextLine());
			scalefield.setText(firstclassscan.nextLine());
			
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
					categorylist.add(new categoryobject(v1, v2, v3));
				}//End of else
				
			}//End of while
			firstclassscan.close();
			
			//Fill GUI information for category opened and category combobox
			weightname.setText(categorylist.get(recentindex).getName() + " - Weight: " + categorylist.get(recentindex).getWeight() + "%");
			for(int i = 0; i < categorylist.size(); i++){
				categorycombo.addItem(categorylist.get(i).getName());
			}//End of for
			categorycombo.setSelectedIndex(recentindex);
			
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
					classcombo.addItem(s1);
					if((s1 + " - " + s2).equals(coursename.getText())){
						selectedclass = classcombo.getItemCount()-1;
					}//End of if
					classindex++;
					classcomboscan.close();
				} catch (Exception e){
					classcheck = false;
				}//End of try catch
				
			}//End of while
			classcombo.setSelectedIndex(selectedclass);
			
			//Load current class and calculate values for current category
			frameactions.addCurrentClass(categorylist.get(recentindex).getFilepath());
			frameactions.loadCategoryValues(Double.parseDouble(categorylist.get(recentindex).getWeight()));
			
			//Calculate overall average grade
			String overallgrade = frameactions.loadOverallGrade(recentindex);
			DefaultTableModel valuemodel = (DefaultTableModel) valuetable.getModel();
			valuemodel.setValueAt(overallgrade, 0, 2);
			
			//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
			if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
				valuemodel.setValueAt(frameactions.loadBestGrade(recentindex), 1, 2);
				valuemodel.setValueAt(frameactions.loadWorstGrade(recentindex), 2, 2);
				needbutton.setEnabled(true);
			}//End of if
			else{
				valuemodel.setValueAt("N/A", 1, 2);
				valuemodel.setValueAt("N/A", 2, 2);
				needbutton.setEnabled(false);
			}//End of else
			
			//Rewrite file for opening recent semester quickly
			FileWriter fw = new FileWriter(new File(System.getProperty("user.dir") + "/quicklaunch.txt"));
			fw.write(filename + "\n");
			fw.write(semestertitle + "\n");
			fw.write("" + quickcheck.isSelected());
			fw.close();
			
			
		} catch (Exception e) {
			utilities.misc.errorMessage("Can't open semester files! Closing application.");
			System.exit(0);
		}//End of try catch
		
	}//End of openFiles
	
	
	
	
	
	public void switchCategory(int index){
		
		//Switch category title and grades table to current selected category
		weightname.setText(categorylist.get(index).getName() + " - Weight: " + categorylist.get(index).getWeight() + "%");
		
		//Load current class and calculate values for current category
		frameactions.removeAllRows();
		frameactions.addCurrentClass(categorylist.get(index).getFilepath());
		frameactions.loadCategoryValues(Double.parseDouble(categorylist.get(index).getWeight()));
		
		//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
		DefaultTableModel gradesmodel = (DefaultTableModel) gradestable.getModel();
		lasttable = new String[gradesmodel.getRowCount()][3];
		for(int i = 0; i < lasttable.length; i++) {
			lasttable[i][0] = gradesmodel.getValueAt(i, 1).toString();
			lasttable[i][1] = gradesmodel.getValueAt(i, 2).toString();
			lasttable[i][2] = gradesmodel.getValueAt(i, 3).toString();
		}//End of for
		lastscale = scalefield.getText();
		lastquick = quickcheck.isSelected();
		
	}//End of switchCategory
	
	public void switchClass(int index, String filename){
		
		//Switch category title and grades table to current selected category
		try {
			
			//Scan for class title
			Scanner classscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/class" + (index+1) + ".txt"));
			coursename.setText(classscan.nextLine() + " - " + classscan.nextLine());
			scalefield.setText(classscan.nextLine());
			
			//Remove categories from last class
			int combocount = categorycombo.getItemCount();
			for(int i = 0; i < combocount-1; i++){
				categorycombo.removeItemAt(0);
			}
			for(int i = categorylist.size()-1; i >= 0; i--){
				categorylist.remove(i);
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
					categorylist.add(new categoryobject(v1, v2, v3));
				}//End of else
				
			}//End of while
			classscan.close();
			
			//Fill GUI information for category opened and category combobox
			weightname.setText(categorylist.get(recentindex).getName() + " - Weight: " + categorylist.get(recentindex).getWeight() + "%");
			for(int i = 0; i < categorylist.size(); i++){
				categorycombo.addItem(categorylist.get(i).getName());
			}//End of for
			categorycombo.removeItemAt(0);
			categorycombo.setSelectedIndex(recentindex);
			
			//Load current class and calculate values for current category
			frameactions.removeAllRows();
			frameactions.addCurrentClass(categorylist.get(recentindex).getFilepath());
			frameactions.loadCategoryValues(Double.parseDouble(categorylist.get(recentindex).getWeight()));
			
			//Calculate overall average grade
			String overallgrade = frameactions.loadOverallGrade(recentindex);
			DefaultTableModel valuemodel = (DefaultTableModel) valuetable.getModel();
			valuemodel.setValueAt(overallgrade, 0, 2);
			
			//If overall average is determined, set best and worst grade fields as N/A, otherwise calculate them
			if(valuemodel.getValueAt(0, 2).toString().equals("N/A")){
				valuemodel.setValueAt(frameactions.loadBestGrade(recentindex), 1, 2);
				valuemodel.setValueAt(frameactions.loadWorstGrade(recentindex), 2, 2);
				needbutton.setEnabled(true);
			}//End of if
			else{
				valuemodel.setValueAt("N/A", 1, 2);
				valuemodel.setValueAt("N/A", 2, 2);
				needbutton.setEnabled(false);
			}//End of else
			
			//Get last saved table, grade scale, and quicklaunch check to determine if something is updated
			DefaultTableModel gradesmodel = (DefaultTableModel) gradestable.getModel();
			lasttable = new String[gradesmodel.getRowCount()][3];
			for(int i = 0; i < lasttable.length; i++) {
				lasttable[i][0] = gradesmodel.getValueAt(i, 1).toString();
				lasttable[i][1] = gradesmodel.getValueAt(i, 2).toString();
				lasttable[i][2] = gradesmodel.getValueAt(i, 3).toString();
			}//End of for
			lastscale = scalefield.getText();
			lastquick = quickcheck.isSelected();
			
		} catch (FileNotFoundException e) {
			
		}//End of try catch
		
	}//End of switchClass
	
	public static void main(String filename, boolean quickcheck) {
		
		//Launch new instance of frame
		appframe frame = new appframe(filename, quickcheck);
		frame.setVisible(true);

	}//End of main

}//End of appframe
