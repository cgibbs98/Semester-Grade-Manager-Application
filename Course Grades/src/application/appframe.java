package application;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import startup.Launch;

public class AppFrame extends JFrame{

	public static JTable gradesTable;
	public static JTable valueTable;
	public static JLabel courseName = new JLabel("");
	public static JTextField scaleField = new JTextField("");
	public static JLabel weightName = new JLabel("Category" + " - Weight: " + "%");
	public static JComboBox categoryCombo = new JComboBox();
	public static JComboBox classCombo = new JComboBox();
	public static JCheckBox quickCheck = new JCheckBox();
	public static JButton needButton = new JButton("Check Points Needed");
	public JButton addButton = new JButton("Add New Grade");
	public JButton removeButton = new JButton("Remove Empty Grades");
	public JButton updateButton = new JButton("Update and Save Grades");
	public static ArrayList<CategoryObject> categoryList = new ArrayList();
	public static boolean comboListen;
	public static int prevCategory;
	public static int prevClass;
	public static String[][] lastTable;
	public static String lastScale;
	public static boolean lastQuick;
	
	public AppFrame(String fileName, boolean quickCheckValue){
		
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
		
		//menuBar
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem startUpSaveItem = new JMenuItem("Save & Open Startup");
		JMenuItem startUpItem = new JMenuItem("Open Startup");
		JMenuItem exitSaveItem = new JMenuItem("Save & Exit");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem helpItem = new JMenuItem("Help Topics");
		JMenuItem propItem = new JMenuItem("View Properties");
		JMenuItem aboutItem = new JMenuItem("About");
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(startUpSaveItem);
		fileMenu.add(startUpItem);
		fileMenu.addSeparator();
		fileMenu.add(exitSaveItem);
		fileMenu.add(exitItem);
		helpMenu.add(helpItem);
		helpMenu.add(propItem);
		helpMenu.addSeparator();
		helpMenu.add(aboutItem);
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		
		//Grades Table
		String[] columns = new String[] {
	            "#", "Description", "Points Earned", "Points Possible"
	    };
		Object[][] data = new Object[][] {
        };
        //Create table
        gradesTable = new JTable(new DefaultTableModel(data, columns)){
        	//Make ID non editable
        	@Override
        	public boolean isCellEditable(int row, int col) {
        	     return col != 0;
        	}
        };
        gradesTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        gradesTable.getColumnModel().getColumn(1).setPreferredWidth(125);
        gradesTable.getColumnModel().getColumn(2).setPreferredWidth(125);
        gradesTable.getColumnModel().getColumn(3).setPreferredWidth(125);
        //Prevent Reordering and Resizing
        gradesTable.getTableHeader().setReorderingAllowed(false);
        gradesTable.getTableHeader().setResizingAllowed(false);
        gradesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane gradesTablePane = new JScrollPane(gradesTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gradesTablePane.setPreferredSize(new Dimension(gradesTable.getPreferredSize().width+17, 160));
        //Align Points Columns to Right
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        gradesTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        gradesTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		//Table Sorting
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(gradesTable.getModel());
		gradesTable.setRowSorter(sorter);
		gradesTable.setToolTipText("Table that displays set of assignments/exams and their individual grades");
		gradesTablePane.setToolTipText("Table that displays set of assignments/exams and their individual grades");
		Box eastBox = Box.createVerticalBox();
		eastBox.add(gradesTablePane);
		
		
		
		
		
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
        valueTable = new JTable(new DefaultTableModel(data2, columns2)){
        	//Make ID non editable
        	@Override
        	public boolean isCellEditable(int row, int col) {
        	     return (col < 0);
        	}
        };
        valueTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        valueTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        valueTable.getColumnModel().getColumn(2).setPreferredWidth(75);
        //Prevent Reordering and Resizing
        valueTable.getTableHeader().setReorderingAllowed(false);
        valueTable.getTableHeader().setResizingAllowed(false);
        valueTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane valueTablePane = new JScrollPane(valueTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        valueTablePane.setPreferredSize(new Dimension(valueTable.getPreferredSize().width+17, valueTable.getPreferredSize().height));
        //Align Points Columns to Right
        DefaultTableCellRenderer rightRenderer2 = new DefaultTableCellRenderer();
        rightRenderer2.setHorizontalAlignment(JLabel.RIGHT);
        valueTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer2);
        valueTable.setToolTipText("Table that displays current course grade and some values for calculating it");
        valueTablePane.setToolTipText("Table that displays current course grade and some values for calculating it");
		Box westBox = Box.createVerticalBox();
		westBox.add(valueTablePane);
		
		//Extra Credit/Scale
		JPanel scalePanel = new JPanel();
		JLabel scaleLabel = new JLabel("Final Grade Extra Credit or Scale: ");
		scaleField.setPreferredSize(new Dimension(20, scaleField.getPreferredSize().height));
		scaleField.setHorizontalAlignment(SwingConstants.RIGHT);
		scaleLabel.setToolTipText("Raises overall grade in course by set points and works for overall grade penalties with negative values as well. (Must save for changes to take effect)");
		scaleField.setToolTipText("Raises overall grade in course by set points and works for overall grade penalties with negative values as well. (Must save for changes to take effect)");
		scalePanel.add(scaleLabel);
		scalePanel.add(scaleField);
		westBox.add(scalePanel);
		
		//Category
		JPanel categoryPanel = new JPanel();
		JLabel categoryLabel = new JLabel("Select Another Category: ");
		categoryCombo.setPreferredSize(new Dimension(200, categoryCombo.getPreferredSize().height));
		categoryLabel.setToolTipText("Select another category to view (Autosaves current grades before accessing)");
		categoryCombo.setToolTipText("Select another category to view (Autosaves current grades before accessing)");
		categoryPanel.add(categoryLabel);
		categoryPanel.add(categoryCombo);
		
        //Class
        JPanel classPanel = new JPanel();
        JLabel classLabel = new JLabel("Select Another Class: ");
        classCombo.setPreferredSize(new Dimension(150, classCombo.getPreferredSize().height));
        classLabel.setToolTipText("Select another class to view (Autosaves current grades before accessing)");
        classCombo.setToolTipText("Select another class to view (Autosaves current grades before accessing)");
        classPanel.add(classLabel);
        classPanel.add(classCombo);
        
        //Adds Above 2 Components
        westBox.add(classPanel);
        pane.add(westBox, BorderLayout.WEST);
        eastBox.add(categoryPanel);
        pane.add(eastBox, BorderLayout.EAST);
        
		//Name + Weight Type
		Box northBox = Box.createVerticalBox();
		JPanel coursePanel = new JPanel();
		courseName.setToolTipText("Name of the current course and its code");
		coursePanel.add(courseName);
		northBox.add(coursePanel);
		JPanel weightPanel = new JPanel();
		weightName.setToolTipText("Name of the current category and its weight");
		weightPanel.add(weightName);
		northBox.add(weightPanel);
		pane.add(northBox, BorderLayout.NORTH);
		
		//Table Actions
		JPanel actionsPanel = new JPanel();
		JLabel quickLabel = new JLabel("Open This Semester at Launch:");
		quickCheck.setSelected(quickCheckValue);
		quickLabel.setToolTipText("Allows semester to be opened up immediately instead of showing a startup screen upon launch (Must save for changes to take effect)");
		quickCheck.setToolTipText("Allows semester to be opened up immediately instead of showing a startup screen upon launch (Must save for changes to take effect)");
		needButton.setEnabled(false);
		actionsPanel.add(quickLabel);
		actionsPanel.add(quickCheck);
		actionsPanel.add(new JLabel("          "));
		actionsPanel.add(addButton);
		actionsPanel.add(removeButton);
		actionsPanel.add(needButton);
		actionsPanel.add(updateButton);
		addButton.setToolTipText("Adds new row to add a new grade for a class");
		removeButton.setToolTipText("Removes empty rows from grades table");
		needButton.setToolTipText("Allows you to find what percentage of points you need in catgories without grades to get a specific overall grade");
		updateButton.setToolTipText("Updates grade in course and saves class information");
		pane.add(actionsPanel, BorderLayout.SOUTH);
		
		//Action Listeners
		addButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				FrameActions.addEmptyGrade();
			}  
		});
		removeButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				FrameActions.removeGrades();
			}  
		});
		needButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				PointsNeeded.main(null);
			}  
		});
		updateButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				FrameActions.updateAndSave();
			}  
		});
		//Open initial master file with semester title and most recent class file
		Scanner masterscan;
		String firstclass = null;
		String semestertitle = null;
		try {
			masterscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + fileName + "/master.txt"));
			semestertitle = masterscan.nextLine();
			firstclass = masterscan.nextLine();
			masterscan.close();
			setTitle(getTitle() + semestertitle);
		} catch (FileNotFoundException e1) {
			utilities.Misc.errorMessage("Can't open semester files! Closing application.");
			System.exit(0);
		}
		GuiActions.openFiles(fileName, firstclass, semestertitle);
		
		//Combo listeners
		categoryCombo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(comboListen == true){
		    		FrameActions.save(prevCategory, prevClass);
		    		GuiActions.switchCategory(categoryCombo.getSelectedIndex());
		    	}//End of if
		    }
		});
		classCombo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(comboListen == true){
		    		FrameActions.save(prevCategory, prevClass);
		    		GuiActions.switchClass(classCombo.getSelectedIndex(), fileName);
		    	}//End of if
		    }
		});
		quickLabel.addMouseListener(new MouseAdapter() {  
		    public void mouseClicked(MouseEvent e) {  
		    	quickCheck.setSelected(!(quickCheck.isSelected()));
		    }  
		});
		
		//Menu listeners
		saveItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	FrameActions.updateAndSave();
		    }
		});
		startUpSaveItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	FrameActions.updateAndSave();
		    	File relaunchfile = new File("relaunch.txt");
		    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
		    	GuiActions.clearGUI();
		    	dispose();
		    	Launch.main(null);
		    }
		});
		startUpItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	//If grades are unsaved, prompt the user for further action 
				if(checkLastUpdated() == false){
					int action = utilities.Misc.savePrompt();
					if(action == 0){
						FrameActions.updateAndSave();
						File relaunchfile = new File("relaunch.txt");
				    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
				    	GuiActions.clearGUI();
				    	dispose();
				    	Launch.main(null);
					}//End of if
					else if(action == 1){
						File relaunchfile = new File("relaunch.txt");
				    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
				    	GuiActions.clearGUI();
				    	dispose();
				    	Launch.main(null);
					}//End of else
				}//End of if
				else{
					File relaunchfile = new File("relaunch.txt");
			    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
			    	GuiActions.clearGUI();
			    	dispose();
			    	Launch.main(null);
				}//End of else
		    }
		});
		exitSaveItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	FrameActions.updateAndSave();
				System.exit(0);
		    }
		});
		exitItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	//If grades are unsaved, prompt the user for further action 
				if(checkLastUpdated() == false){
					int action = utilities.Misc.savePrompt();
					if(action == 0){
						FrameActions.updateAndSave();
						System.exit(0);
					}//End of if
					else if(action == 1){
						System.exit(0);
					}//End of else
				}//End of if
				else{
					System.exit(0);
				}//End of else
		    }
		});
		helpItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	help.HelpFrame.main(null);
		    }
		});
		propItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	help.PropertiesFrame.main(fileName);
		    }
		});
		aboutItem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	help.AboutFrame.main(null);
		    }
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				
				//If grades are unsaved, prompt the user for further action 
				if(checkLastUpdated() == false){
					int action = utilities.Misc.savePrompt();
					if(action == 0){
						FrameActions.updateAndSave();
						System.exit(0);
					}//End of if
					else if(action == 1){
						System.exit(0);
					}//End of else
				}//End of if
				else{
					System.exit(0);
				}//End of else
				
			}
		});
		
	}//End of AppFrame
	
	
	
	
	
	public boolean checkLastUpdated(){
		
		//Check every editable value in the application and return false if something is different from last update, otherwise return true
		boolean b = true;
		if(!(lastQuick == quickCheck.isSelected())){
			return b = false;
		}//End of if
		if(!(lastScale.equals(scaleField.getText()))){
			return b = false;
		}//End of else if
		DefaultTableModel gradesmodel = (DefaultTableModel) gradesTable.getModel();
		if(!(lastTable.length == gradesmodel.getRowCount())){
			return b = false;
		}
		for(int i = 0; i < lastTable.length; i++) {
			if( !(lastTable[i][0].equals(gradesmodel.getValueAt(i, 1).toString())) || !(lastTable[i][1].equals(gradesmodel.getValueAt(i, 2).toString())) || !(lastTable[i][2].equals(gradesmodel.getValueAt(i, 3).toString())) ){
				return b = false;
			}//End of if
		}//End of for
		return b;
		
	}//End of checkLastUpdated
	
	public static void main(String fileName, boolean quickCheck) {
		
		//Launch new instance of frame
		AppFrame frame = new AppFrame(fileName, quickCheck);
		frame.setVisible(true);

	}//End of main

}//End of AppFrame
