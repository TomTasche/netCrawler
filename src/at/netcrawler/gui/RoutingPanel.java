package at.netcrawler.gui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class RoutingPanel extends JComponent {
	
	private static final long serialVersionUID = -3130476083964667094L;
	
	JPanel panel = new JPanel();
	
	JLabel text = new JLabel("Hallo :D");
	
	public RoutingPanel() {
		
		panel.add(text);
		setLayout(new BorderLayout());
		add(panel);
	}
	
}
