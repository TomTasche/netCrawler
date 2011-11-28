package at.netcrawler.gui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SpecificPanel extends JComponent {
	
	private static final long serialVersionUID = 3139958380904300704L;
	
	JPanel panel = new JPanel();
	
	JLabel text = new JLabel("Hallo :D");
	
	public SpecificPanel() {
		
		panel.add(text);
		setLayout(new BorderLayout());
		add(panel);
	}
	
}
