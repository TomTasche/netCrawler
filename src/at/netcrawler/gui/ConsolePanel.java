package at.netcrawler.gui;
import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ConsolePanel extends JComponent{

	private static final long serialVersionUID = -5978914429072592625L;

	JPanel panel = new JPanel();
	
	JLabel text = new JLabel("Hallo :D");
	
	public ConsolePanel() {
		
		panel.add(text);
		setLayout(new BorderLayout());
		add(panel);
	}

}
