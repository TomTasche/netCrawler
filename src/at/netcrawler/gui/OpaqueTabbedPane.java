package at.netcrawler.gui;
import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;


public class OpaqueTabbedPane extends JTabbedPane {
	
	private static final long serialVersionUID = 3155045858089276751L;
	
	private static final Color OPAQUE = new Color(0, 0, 0, 1);
	
	
	public OpaqueTabbedPane() {}
	public OpaqueTabbedPane(int tabPlacement) {
		super(tabPlacement);
	}
	public OpaqueTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}
	
	
	@Override
	public void insertTab(String title, Icon icon, Component component,
			String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		
		component.setBackground(OPAQUE);
	}
	
}