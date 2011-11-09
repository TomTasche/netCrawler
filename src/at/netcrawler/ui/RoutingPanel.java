package at.netcrawler.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class RoutingPanel extends JComponent{

	JPanel panel = new JPanel();
	
	JLabel text = new JLabel("Hallo :D");
	
	public RoutingPanel() {
		
		panel.add(text);
		setLayout(new BorderLayout());
		add(panel);
	}

}
