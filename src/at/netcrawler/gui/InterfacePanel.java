package at.netcrawler.gui;
import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class InterfacePanel extends JComponent{

	private static final long serialVersionUID = 7612096074789240186L;

	JPanel panel = new JPanel();
	
	JLabel text = new JLabel("Hallo :D");
	
	public InterfacePanel() {
		
		panel.add(text);
		setLayout(new BorderLayout());
		add(panel);
	}

}
