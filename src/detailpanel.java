package swing_gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class detailpanel extends JPanel {
	public detailpanel(){
		Dimension size = getPreferredSize();
		size.width = 250;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Original Image"));
		JLabel namelabel = new JLabel("Name");
		JLabel ocupation = new JLabel("Job");
		JButton button = new JButton("add");
		setLayout(new GridLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		
		
	}

}
