package at.netcrawler.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Device extends JFrame implements ListSelectionListener {

	public enum MenuEntry {
		GENERAL("Allgemein", new GeneralPanel()),
		INTERFACES("Interfaces", new InterfacePanel()),
		ROUTING("Routing", new RoutingPanel()),
		SWITCHING("Switching", new SwitchingPanel()),
		SPECIFIC("Spezifisch", new SpecificPanel()),
		CONSOLE("Konsole", new ConsolePanel());
		
		public static JComponent getComponent(int index) {
			return values()[index].component;
		}

		String name;
		JComponent component;

		private MenuEntry(String listName, JComponent component) {
			this.name = listName;
			this.component = component;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	private static final long serialVersionUID = 4055271025615555461L;

	JList menuList = new JList(MenuEntry.values());

	JPanel panel = new JPanel(new BorderLayout());
	JPanel content = new JPanel(new BorderLayout());
	JPanel listPanel = new JPanel(new BorderLayout());
	JPanel buttonPanel = new JPanel(new GridLayout());
	JPanel centerPanel = new JPanel(new BorderLayout());
	JPanel generalPanel = new JPanel(new BorderLayout());

	JScrollPane scroll = new JScrollPane(listPanel);

	public Device(){
		setTitle("Device Info");
		setMinimumSize(new Dimension(700,500));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listPanel.add(buttonPanel, BorderLayout.NORTH);
		listPanel.add(centerPanel, BorderLayout.CENTER);

		scroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		buttonPanel.add(menuList);

		menuList.addListSelectionListener(this);

		panel.add(content, BorderLayout.CENTER);
		panel.add(scroll, BorderLayout.WEST);
		add(panel);


		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - getWidth()) / 2;
		int y = (screen.height - getHeight()) / 2;
		setLocation(x, y);

		setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		new Device();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;

		MenuEntry entry = (MenuEntry) menuList.getSelectedValue();

		content.removeAll();
		content.add(entry.component);

		content.validate();
		content.repaint();

	}
}
