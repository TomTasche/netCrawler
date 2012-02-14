package at.netcrawler.gui.main;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SwitchingPanel extends JComponent {
	
	private static final long serialVersionUID = 5078077497449808582L;
	
	JPanel panel = new JPanel();
	
	JLabel text = new JLabel("Hallo :D");
	
	public SwitchingPanel() {
		
		panel.add(text);
		setLayout(new BorderLayout());
		add(panel);
	}
	
}
