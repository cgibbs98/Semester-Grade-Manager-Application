package help;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class aboutframe extends JDialog{

	public aboutframe(){
		
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
		Box labelsbox = Box.createVerticalBox();
		JLabel accountlabel = new JLabel("This project was created by cgibbs98:");
		JLabel accountlink = new JLabel("Github Profile");
		accountlink.setToolTipText("https://github.com/cgibbs98");
		JLabel projectlabel = new JLabel("View project repository/source code and licensing below:");
		JLabel projectlink = new JLabel("Github Link");
		projectlink.setToolTipText("https://github.com/cgibbs98/Semester-Grade-Manager-Application");
		
		//Create hyperlinks (Tutorial at https://www.codejava.net/java-se/swing/how-to-create-hyperlink-with-jlabel-in-java-swing)
		accountlink.setForeground(Color.BLUE.darker());
		accountlink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		accountlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               try {
            	   Desktop.getDesktop().browse(new URI("https://github.com/cgibbs98"));
               }catch (Exception e){
            	   utilities.misc.errorMessage("An error has occured. Please try again later.");
               }
            }
            @Override
            public void mouseExited(MouseEvent me) {
            	accountlink.setText(accountlink.getText());
            }
            @Override
            public void mouseEntered(MouseEvent me) {
            	accountlink.setText("<html><a href=''>" + accountlink.getText() + "</a></html>");
            }
        });
		projectlink.setForeground(Color.BLUE.darker());
		projectlink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		projectlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
               try {
            	   Desktop.getDesktop().browse(new URI("https://github.com/cgibbs98/Semester-Grade-Manager-Application"));
               }catch (Exception e){
            	   utilities.misc.errorMessage("An error has occured. Please try again later.");
               }
            }
            @Override
            public void mouseExited(MouseEvent me) {
            	projectlink.setText(projectlink.getText());
            }
            @Override
            public void mouseEntered(MouseEvent me) {
            	projectlink.setText("<html><a href=''>" + projectlink.getText() + "</a></html>");
            }
        });
		
		//Create panels to center JLabels
		JPanel panel1 = new JPanel();
		panel1.add(accountlabel);
		JPanel panel2 = new JPanel();
		panel2.add(accountlink);
		JPanel panel3 = new JPanel();
		panel3.add(projectlabel);
		JPanel panel4 = new JPanel();
		panel4.add(projectlink);
		labelsbox.add(panel1);
		labelsbox.add(panel2);
		labelsbox.add(panel3);
		labelsbox.add(panel4);
		pane.add(labelsbox, BorderLayout.NORTH);
		
		//Close button
		JPanel buttonpanel = new JPanel();
		JButton closebutton = new JButton("Close Window");
		buttonpanel.add(closebutton);
		pane.add(buttonpanel, BorderLayout.SOUTH);
		
		//Action Listeners
		closebutton.addActionListener(new ActionListener(){  
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
		
	}//End of aboutframe
	
	public static void main(String[] args) {
		
		//Launch new instance of frame
		aboutframe frame = new aboutframe();
		frame.setVisible(true);

	}//End of main

}//End of aboutframe
