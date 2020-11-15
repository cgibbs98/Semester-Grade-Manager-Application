package startup;

import java.awt.*;
import javax.swing.*;

public class NewActions extends CreateNew{

	public static void newClassPanel(boolean b){
		
		//Launch check
		if(b){
			addClass();
			addCategory();
			indexHold++;
		}//End of if
		else{
			
			//Check if new class can be added to the layout by checking if categories only add up to 100
			double total = 0;
			for(int i = indexHold; i < centerBox.getComponentCount(); i++){
				
				//Add totals and prevent adding category if a field is blank or not a number
				if(utilities.Misc.isANumber(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText())){
					total += Double.parseDouble(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText());
				}//End of if
				else{
					utilities.Misc.errorMessage("Can't add class! Recently added weight isn't filled in or isn't a numerical value.");
					return;
				}//End of else
				
				//Also prevents a category with no name from being added
				if(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(1)).getText().equals("")){
					utilities.Misc.errorMessage("Can't add class! Recently added category has no name.");
					return;
				}//End of if
				
			}//End of for
			
			//Only add new category if total weights < 100 and both course code and title fields aren't empty
			if(total != 100){
				utilities.Misc.errorMessage("Can't add class! Weights for any class must add up to exactly 100.");
			}//End of if
			else if(((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).getText().equals("") || ((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).getText().equals("")){
				utilities.Misc.errorMessage("Can't add class! Both class code and title fields must be filled in.");
			}//End of else if
			else{
				((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).setEditable(false);
				((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).setEditable(false);
				((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(false);
				((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(false);
				addClass();
				addCategory();
				//Save 2nd to last index of Box components
				indexHold += ((centerBox.getComponentCount()-indexHold)-1);
			}//End of else
			
		}//End of else
		
	}//End of newClassPanel
	
	public static void newCategoryPanel(boolean b){
		
		//Launch check
		if(b){
			addCategory();
		}//End of if
		else{
			
			//Check if new category can be added to the layout by checking if categories only add up to 100
			double total = 0;
			for(int i = indexHold; i < centerBox.getComponentCount(); i++){
				
				//Add totals and prevent adding category if a field is blank or not a number
				if(utilities.Misc.isANumber(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText())){
					total += Double.parseDouble(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText());
				}//End of if
				else{
					utilities.Misc.errorMessage("Can't add category! Recently added weight isn't filled in or isn't a numerical value.");
					return;
				}//End of else
				
				//Also prevents a category with no name from being added
				if(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(1)).getText().equals("")){
					utilities.Misc.errorMessage("Can't add category! Recently added category has no name.");
					return;
				}//End of if
				
			}//End of for
			
			//Only add new category if total weights < 100
			if(total < 100){
				((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(false);
				((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(false);
				addCategory();
			}//End of if
			else {
				utilities.Misc.errorMessage("Can't add category! Weights for any class can't be over 100.");
			}//End of else
			
		}//End of else
		
	}//End of newCategoryPanel
	
	
	
	
	
	public static void deleteLastClass(){
		
		//Remove components until a panel has a field with no weight category
		int i = centerBox.getComponentCount()-1;
		while(utilities.Misc.isANumber(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText())){
			deleteLastCategory();
			i--;
		}//End of while
		
		//If only one class is remaining, clear first class and category fields, otherwise, delete them
		if(centerBox.getComponentCount() == 2){
			((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(1)).setText("");
			((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).setText("");
			indexHold = 1;
		}//End of if
		else{
			centerBox.remove(centerBox.getComponentCount()-1);
			centerBox.remove(centerBox.getComponentCount()-1);
			centerBox.revalidate();
			centerBox.repaint();
			((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(true);
			((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(true);
			i = centerBox.getComponentCount()-1;
			while(utilities.Misc.isANumber(((JTextField) ((JPanel) centerBox.getComponent(i)).getComponent(3)).getText())){
				i--;
			}//End of while
			indexHold = i+1;
			classCount--;
			((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(1)).setEditable(true);
			((JTextField) ((JPanel) centerBox.getComponent(indexHold-1)).getComponent(3)).setEditable(true);
		}//End of else
		
	}//End of deleteLastClass
	
	public static void deleteLastCategory(){
		
		//Only remove component if there will be at least 1 category remaining afterwards, otherwise clear both 1st category fields
		if(centerBox.getComponentCount()-1 >= indexHold+1){
			centerBox.remove(centerBox.getComponentCount()-1);
			centerBox.revalidate();
			centerBox.repaint();
			((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(1)).setEditable(true);
			((JTextField) ((JPanel) centerBox.getComponent(centerBox.getComponentCount()-1)).getComponent(3)).setEditable(true);
		}//End of if
		else{
			((JTextField) ((JPanel) centerBox.getComponent(indexHold)).getComponent(1)).setText("");
			((JTextField) ((JPanel) centerBox.getComponent(indexHold)).getComponent(3)).setText("");
		}//End of else
		
	}//End of deleteLastCategory
	
	public static void removeAllComponents(){
		
		//Removes all components one by one
		for(int i = centerBox.getComponentCount()-1; i >= 0; i--){
			centerBox.remove(i);
			centerBox.revalidate();
			centerBox.repaint();
		}//End of for
		indexHold = 0;
		classCount = 0;
		
	}//End of removeAllComponents
	
	
	
	
	
	public static void addClass(){
		
		//Components
		JPanel temp = new JPanel();
		JLabel label1 = new JLabel("Course Code: ");
		JTextField field1 = new JTextField("");
		field1.setPreferredSize(new Dimension(150, field1.getPreferredSize().height));
		JLabel label2 = new JLabel("Course Title: ");
		JTextField field2 = new JTextField("");
		field2.setPreferredSize(new Dimension(300, field2.getPreferredSize().height));
		
		//Add to Frame
		temp.add(label1);
		temp.add(field1);
		temp.add(label2);
		temp.add(field2);
		centerBox.add(temp);
		centerBox.revalidate();
		centerBox.repaint();
		classCount++;
		
	}//End of addClass
	
	public static void addCategory(){
		
		//Components
		JPanel temp = new JPanel();
		JLabel label1 = new JLabel("Category: ");
		JTextField field1 = new JTextField("");
		field1.setPreferredSize(new Dimension(200, field1.getPreferredSize().height));
		
		JLabel label2 = new JLabel("Weight (%): ");
		JTextField field2 = new JTextField("");
		field2.setPreferredSize(new Dimension(20, field2.getPreferredSize().height));
		field2.setHorizontalAlignment(SwingConstants.RIGHT);
		
		//Add to Frame
		temp.add(label1);
		temp.add(field1);
		temp.add(label2);
		temp.add(field2);
		centerBox.add(temp);
		centerBox.revalidate();
		centerBox.repaint();
		
	}//End of addCategory
	
}//End of NewActions
