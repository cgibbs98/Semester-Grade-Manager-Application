package help;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class AboutFrame extends JDialog{

	public AboutFrame(){
		
		//Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int) (screenSize.getWidth()/3.75);
		int h = (int) (screenSize.getHeight()/4.75);
		setSize(w, h);
		
		//Properties
		setTitle("About");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		//Components
		Container pane = getContentPane();
		Box labelsBox = Box.createVerticalBox();
		JLabel accountLabel = new JLabel("This project was created by cgibbs98:");
		JLabel accountLink = new JLabel("Github Profile");
		accountLink.setToolTipText("https://github.com/cgibbs98");
		JLabel projectLabel = new JLabel("View project repository/source code and licensing below:");
		JLabel projectLink = new JLabel("Github Link");
		projectLink.setToolTipText("https://github.com/cgibbs98/Semester-Grade-Manager-Application");
		
		//Create hyperlinks (Tutorial at https://www.codejava.net/java-se/swing/how-to-create-hyperlink-with-jlabel-in-java-swing)
		accountLink.setForeground(Color.BLUE.darker());
		accountLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		accountLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               try {
            	   Desktop.getDesktop().browse(new URI("https://github.com/cgibbs98"));
               }catch (Exception e){
            	   utilities.Misc.errorMessage("An error has occured. Please try again later.");
               }
            }
            @Override
            public void mouseExited(MouseEvent me) {
            	accountLink.setText(accountLink.getText());
            }
            @Override
            public void mouseEntered(MouseEvent me) {
            	accountLink.setText("<html><a href=''>" + accountLink.getText() + "</a></html>");
            }
        });
		projectLink.setForeground(Color.BLUE.darker());
		projectLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		projectLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               try {
            	   Desktop.getDesktop().browse(new URI("https://github.com/cgibbs98/Semester-Grade-Manager-Application"));
               }catch (Exception e){
            	   utilities.Misc.errorMessage("An error has occured. Please try again later.");
               }
            }
            @Override
            public void mouseExited(MouseEvent me) {
            	projectLink.setText(projectLink.getText());
            }
            @Override
            public void mouseEntered(MouseEvent me) {
            	projectLink.setText("<html><a href=''>" + projectLink.getText() + "</a></html>");
            }
        });
		
		//Create panels to center JLabels
		JPanel panel1 = new JPanel();
		panel1.add(accountLabel);
		JPanel panel2 = new JPanel();
		panel2.add(accountLink);
		JPanel panel3 = new JPanel();
		panel3.add(projectLabel);
		JPanel panel4 = new JPanel();
		panel4.add(projectLink);
		labelsBox.add(panel1);
		labelsBox.add(panel2);
		labelsBox.add(panel3);
		labelsBox.add(panel4);
		pane.add(labelsBox, BorderLayout.NORTH);
		
		//Close button
		JPanel buttonPanel = new JPanel();
		JButton closeButton = new JButton("Close Window");
		buttonPanel.add(closeButton);
		pane.add(buttonPanel, BorderLayout.SOUTH);
		
		//Action Listeners
		closeButton.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				dispose();
			}  
		});
		
		//Window Listener for Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
	}//End of AboutFrame
	
	public static void main(String[] args) {
		
		//Launch new instance of frame
		AboutFrame frame = new AboutFrame();
		frame.setVisible(true);

	}//End of main

}//End of AboutFrame
