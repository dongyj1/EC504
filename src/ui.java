package src;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.media.jai.*;
import javax.swing.*;

import javax.swing.*;

public class ui {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                JFrame frame = new ImageViewerFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

class ImageViewerFrame extends JFrame{
    public ImageViewerFrame(){
        setTitle("ImageViewer");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        label = new JLabel();
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        add(label, BorderLayout.PAGE_START);
       
        add(label1, BorderLayout.EAST);
        add(label2, BorderLayout.WEST);
        
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu menu = new JMenu("File");
        menubar.add(menu);
        JMenuItem openItem = new JMenuItem("Open");
        menu.add(openItem);
        
        JMenuItem segmentItem = new JMenuItem("2 Segment");
        menu.add(segmentItem);
        JMenuItem ksegmentItem = new JMenuItem("k Segment");
        menu.add(ksegmentItem);
        String [] names = new String [10];
        for(int i = 0; i<names.length; i++) names[i]=null;        
        
        //label.setIcon(new ImageIcon("/Users/weichaozhou/Documents/Boston University/EC504/EC504_Project/images/baboon 2.png"));
        openItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                int result = chooser.showOpenDialog(null);
                if(result == JFileChooser.APPROVE_OPTION){
                    String name = chooser.getSelectedFile().getPath();
                    System.out.println(name);
                    String [] names_temp = name.split("/");
                    
                    for(int i = 0; i<names_temp.length; i++) names[i] = names_temp[i];
                    PlanarImage pgmImage = JAI.create("fileload", name);

                    // create an ImageIcon from the image
                    ImageIcon icon = new ImageIcon(pgmImage.getAsBufferedImage());
                    label.setIcon(icon);
                    //label.setIcon(new ImageIcon(name));
                }
            }
            }
        );
        ksegmentItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                //int result = chooser.showOpenDialog(null);
                //if(result == JFileChooser.APPROVE_OPTION){
                //    String name = chooser.getSelectedFile().getPath();
            	int i = 0;
            	for( ; i<names.length; i++) {
            		if(names[i]==null) break;
            	}
            	
            	String [] args = new String [2];
            	args[0] = names[i-1];
                /**
                 for(int j = 1; j<i; j++) {
                	if(names[j].length() != 1) {
                		args[0]= args[0] + "/" + names[j];
                	}
                }
            	
                
            	names[i-1] = "output_" + names[i-1];
            	args[1] = names[0];
            	
            	for(int j = 1; j<i; j++) {
            		if(names[j].length() != 1) {
            			args[1]= args[1] + "/" + names[j];
            		}
            	}**/
            	
            	
            	args[1] = "output_"+ names[i-1];
            	System.out.println(args[0]);
            	System.out.println(args[1]);
            	
            	String fileName = markov_random_field_opt.segk(args);
            	System.out.println("yeah!!!");
            	//label.setIcon(new ImageIcon(name));
            	 // load the image using JAI
                PlanarImage pgmImage = JAI.create("fileload", fileName);
                
                

                // create an ImageIcon from the image
                ImageIcon icon = new ImageIcon(pgmImage.getAsBufferedImage());
                label1.setIcon(icon);
                
            
            	
                
                //label.setIcon(new ImageIcon("/Users/weichaozhou/Documents/Boston University/EC504/EC504_Project/images/gator.png"));
                }
            }
        );
        
        segmentItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                //int result = chooser.showOpenDialog(null);
                //if(result == JFileChooser.APPROVE_OPTION){
                //    String name = chooser.getSelectedFile().getPath();
            	int i = 0;
            	for( ; i<names.length; i++) {
            		if(names[i]==null) break;
            	}
            	
            	String [] args = new String [2];
            	args[0] = names[i-1];
                /**
                 for(int j = 1; j<i; j++) {
                	if(names[j].length() != 1) {
                		args[0]= args[0] + "/" + names[j];
                	}
                }
            	
                
            	names[i-1] = "output_" + names[i-1];
            	args[1] = names[0];
            	
            	for(int j = 1; j<i; j++) {
            		if(names[j].length() != 1) {
            			args[1]= args[1] + "/" + names[j];
            		}
            	}**/
            	
            	
            	args[1] = "output_"+ names[i-1];
            	System.out.println(args[0]);
            	System.out.println(args[1]);
            	
            	String fileName = markov_random_field_opt.seg2(args);
            	System.out.println("yeah!!!");
            	//label.setIcon(new ImageIcon(name));
            	 // load the image using JAI
                PlanarImage pgmImage = JAI.create("fileload", fileName);
                
                

                // create an ImageIcon from the image
                ImageIcon icon = new ImageIcon(pgmImage.getAsBufferedImage());
                label2.setIcon(icon);
                
            
            	
                
                //label.setIcon(new ImageIcon("/Users/weichaozhou/Documents/Boston University/EC504/EC504_Project/images/gator.png"));
                }
            }
        );
       
    }
    private JLabel label;
    private JFileChooser chooser;
    private static final int DEFAULT_WIDTH = 1920;
    private static final int DEFAULT_HEIGHT = 1080;
}