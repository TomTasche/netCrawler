package at.netcrawler.ui;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class SwitchingPanel extends JComponent{

	JPanel panel = new JPanel();
	
	JLabel text = new JLabel("Hallo :D");
	
	public SwitchingPanel() {
		
		panel.add(text);
		setLayout(new BorderLayout());
		add(panel);
	}

}
