package at.netcrawler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class GeneralPanel extends JComponent {
	
	private static final long serialVersionUID = -4911145940779192332L;
	
	JTabbedPane generalTabs = new JTabbedPane();
	JPanel shRun = new JPanel();
	JPanel shVer = new JPanel();
	JPanel shCdp = new JPanel();
	
	JLabel host = new JLabel("Hostname: ");
	JLabel sys = new JLabel("System: ");
	JLabel clock = new JLabel("Clock: ");
	
	JTextField hostName = new JTextField();
	JTextField system = new JTextField();
	JTextField clockTime = new JTextField();
	
	JButton editHost = new JButton(new ImageIcon("edit.png"));
	JButton editClock = new JButton(new ImageIcon("edit.png"));
	
	public GeneralPanel() {
		
		GroupLayout groupLayout = new GroupLayout(shRun);
		shRun.setLayout(groupLayout);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup()
						
						.addComponent(
								host).addComponent(
								sys).addComponent(
								clock)).addGroup(
						groupLayout.createParallelGroup()
						
						.addComponent(
								hostName).addComponent(
								system).addComponent(
								clockTime)).addGroup(
						groupLayout.createParallelGroup()
						
						.addComponent(
								editHost).addComponent(
								editClock)));
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(
						groupLayout.createParallelGroup(
								Alignment.BASELINE).addComponent(
								host).addComponent(
								hostName).addComponent(
								editHost)).addGroup(
						groupLayout.createParallelGroup(
								Alignment.BASELINE).addComponent(
								sys).addComponent(
								system)).addGroup(
						groupLayout.createParallelGroup(
								Alignment.BASELINE).addComponent(
								clock).addComponent(
								clockTime).addComponent(
								editClock)));
		
		generalTabs.addTab(
				"Show Running Config", shRun);
		generalTabs.setMnemonicAt(
				0, KeyEvent.VK_1);
		generalTabs.addTab(
				"Show Version", shVer);
		generalTabs.setMnemonicAt(
				1, KeyEvent.VK_2);
		generalTabs.addTab(
				"CDP", shCdp);
		generalTabs.setMnemonicAt(
				2, KeyEvent.VK_3);
		
		for (int i = 0; i < generalTabs.getTabCount(); i++) {
			generalTabs.getComponentAt(
					i).setBackground(
					new Color(0, 0, 0, 1));
		}
		
		setLayout(new BorderLayout());
		add(generalTabs);
	}
}