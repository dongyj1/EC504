import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import swing_gui.detailpanel;

public class MainFrame extends JFrame{
	private detailpanel dp;
	
	public MainFrame(String title){
		super(title);
		//set layout manager
		setLayout(new BorderLayout());
		
		//create swing components
		//final JTextArea textArea = new JTextArea();
		JButton button = new JButton("Click me to input an image");
		JButton button2 = new JButton("Image Segmentation result");

		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		dp = new detailpanel();
		
		JLabel imgLabel = new JLabel(new ImageIcon("/Users/xinyuli/Documents/workspace/swing_gui/src/1.jpg"));

		
		//add swing components to its content pane
		Container c = getContentPane();
		//c.add(textArea,BorderLayout.CENTER);
		c.add(button,BorderLayout.SOUTH);
		c.add(panel,BorderLayout.WEST);
		c.add(imgLabel,BorderLayout.EAST);

		
		//add behavior
		button.addActionListener(new ActionListener(){

			@Override
			
			 public void actionPerformed(ActionEvent e) {

	            System.out.println("In the action");
		            try {
		                panel.add(new JPanel(){
		                    //java.net.URL imgURL = this.getClass().getResource("https://github.com/zwc662/EC504/blob/master/images/baboon.png");
		                    BufferedImage image = ImageIO.read(new File("/Users/xinyuli/Documents/workspace/swing_gui/src/1.jpg"));
		                });
		            
		            } catch (IOException ex) {
		            }
		         
		        }
			
	
			
		});
		
	};
		
	};
