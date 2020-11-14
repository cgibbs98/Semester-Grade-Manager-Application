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
	public static JLabel coursename = new JLabel("");
	public static JTextField scalefield = new JTextField("");
	public static JLabel weightname = new JLabel("Category" + " - Weight: " + "%");
	public static JComboBox categorycombo = new JComboBox();
	public static JComboBox classcombo = new JComboBox();
	public static JCheckBox quickcheck = new JCheckBox();
	public static JButton needbutton = new JButton("Check Points Needed");
	public JButton addbutton = new JButton("Add New Grade");
	public JButton removebutton = new JButton("Remove Empty Grades");
	public JButton updatebutton = new JButton("Update and Save Grades");
	public static ArrayList<categoryobject> categorylist = new ArrayList();
	public static boolean combolisten;
	public static int prevcategory;
	public static int prevclass;
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
		JMenuItem startupsaveitem = new JMenuItem("Save & Open Startup");
		JMenuItem startupitem = new JMenuItem("Open Startup");
		JMenuItem exitsaveitem = new JMenuItem("Save & Exit");
		JMenuItem exititem = new JMenuItem("Exit");
		JMenuItem helpitem = new JMenuItem("Help Topics");
		JMenuItem propitem = new JMenuItem("View Properties");
		JMenuItem aboutitem = new JMenuItem("About");
		filemenu.add(saveitem);
		filemenu.addSeparator();
		filemenu.add(startupsaveitem);
		filemenu.add(startupitem);
		filemenu.addSeparator();
		filemenu.add(exitsaveitem);
		filemenu.add(exititem);
		helpmenu.add(helpitem);
		helpmenu.add(propitem);
		helpmenu.addSeparator();
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
		gradestable.setToolTipText("Table that displays set of assignments/exams and their individual grades");
		gradestablepane.setToolTipText("Table that displays set of assignments/exams and their individual grades");
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
        valuetable.setToolTipText("Table that displays current course grade and some values for calculating it");
        valuetablepane.setToolTipText("Table that displays current course grade and some values for calculating it");
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
		categorylabel.setToolTipText("Select another category to view (Autosaves current grades before accessing)");
		categorycombo.setToolTipText("Select another category to view (Autosaves current grades before accessing)");
		categorypanel.add(categorylabel);
		categorypanel.add(categorycombo);
		
        //Class
        JPanel classpanel = new JPanel();
        JLabel classlabel = new JLabel("Select Another Class: ");
        classcombo.setPreferredSize(new Dimension(150, classcombo.getPreferredSize().height));
        classlabel.setToolTipText("Select another class to view (Autosaves current grades before accessing)");
        classcombo.setToolTipText("Select another class to view (Autosaves current grades before accessing)");
        classpanel.add(classlabel);
        classpanel.add(classcombo);
        
        //Adds Above 2 Components
        westbox.add(classpanel);
        pane.add(westbox, BorderLayout.WEST);
        eastbox.add(categorypanel);
        pane.add(eastbox, BorderLayout.EAST);
        
		//Name + Weight Type
		Box northbox = Box.createVerticalBox();
		JPanel coursepanel = new JPanel();
		coursename.setToolTipText("Name of the current course and its code");
		coursepanel.add(coursename);
		northbox.add(coursepanel);
		JPanel weightpanel = new JPanel();
		weightname.setToolTipText("Name of the current category and its weight");
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
				pointsneeded.main(null);
			}  
		});
		updatebutton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				frameactions.updateAndSave();
			}  
		});
		//Open initial master file with semester title and most recent class file
		Scanner masterscan;
		String firstclass = null;
		String semestertitle = null;
		try {
			masterscan = new Scanner(new File(System.getProperty("user.dir") + "/savedsemesters/" + filename + "/master.txt"));
			semestertitle = masterscan.nextLine();
			firstclass = masterscan.nextLine();
			masterscan.close();
			setTitle(getTitle() + semestertitle);
		} catch (FileNotFoundException e1) {
			utilities.misc.errorMessage("Can't open semester files! Closing application.");
			System.exit(0);
		}
		guiactions.openFiles(filename, firstclass, semestertitle);
		
		//Combo listeners
		categorycombo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(combolisten == true){
		    		frameactions.save(prevcategory, prevclass);
		    		guiactions.switchCategory(categorycombo.getSelectedIndex());
		    	}//End of if
		    }
		});
		classcombo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(combolisten == true){
		    		frameactions.save(prevcategory, prevclass);
		    		guiactions.switchClass(classcombo.getSelectedIndex(), filename);
		    	}//End of if
		    }
		});
		quicklabel.addMouseListener(new MouseAdapter() {  
		    public void mouseClicked(MouseEvent e) {  
		    	quickcheck.setSelected(!(quickcheck.isSelected()));
		    }  
		});
		
		//Menu listeners
		saveitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	frameactions.updateAndSave();
		    }
		});
		startupsaveitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	frameactions.updateAndSave();
		    	File relaunchfile = new File("relaunch.txt");
		    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
		    	guiactions.clearGUI();
		    	dispose();
		    	launch.main(null);
		    }
		});
		startupitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	//If grades are unsaved, prompt the user for further action 
				if(checkLastUpdated() == false){
					int action = utilities.misc.savePrompt();
					if(action == 0){
						frameactions.updateAndSave();
						File relaunchfile = new File("relaunch.txt");
				    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
				    	guiactions.clearGUI();
				    	dispose();
				    	launch.main(null);
					}//End of if
					else if(action == 1){
						File relaunchfile = new File("relaunch.txt");
				    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
				    	guiactions.clearGUI();
				    	dispose();
				    	launch.main(null);
					}//End of else
				}//End of if
				else{
					File relaunchfile = new File("relaunch.txt");
			    	try {relaunchfile.createNewFile();} catch (Exception ex) {}
			    	guiactions.clearGUI();
			    	dispose();
			    	launch.main(null);
				}//End of else
		    }
		});
		exitsaveitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	frameactions.updateAndSave();
				System.exit(0);
		    }
		});
		exititem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	//If grades are unsaved, prompt the user for further action 
				if(checkLastUpdated() == false){
					int action = utilities.misc.savePrompt();
					if(action == 0){
						frameactions.updateAndSave();
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
		helpitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	help.helpframe.main(null);
		    }
		});
		propitem.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	help.propertiesframe.main(filename);
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
				
				//If grades are unsaved, prompt the user for further action 
				if(checkLastUpdated() == false){
					int action = utilities.misc.savePrompt();
					if(action == 0){
						frameactions.updateAndSave();
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
	
	public static void main(String filename, boolean quickcheck) {
		
		//Launch new instance of frame
		appframe frame = new appframe(filename, quickcheck);
		frame.setVisible(true);

	}//End of main

}//End of appframe
