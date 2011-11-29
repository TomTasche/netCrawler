package at.netcrawler.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;


public class GUI extends JFrame {
	
	private static final long serialVersionUID = 4564331951103104451L;
	
	String[] list = {"Router 1", "Router 2", "Router 3"};
	
	JList deviceList = new JList(list);
	
	JPanel all = new JPanel(new BorderLayout());
	JPanel content = new JPanel(new BorderLayout());
	JPanel overview = new JPanel(new BorderLayout());
	JPanel overviewNorth = new JPanel(new GridLayout());
	JPanel overviewCenter = new JPanel();
	JPanel buttonOverviewCenter = new JPanel();
	
	JButton refresh = new JButton(new ImageIcon("refresh.png"));
	JButton stop = new JButton(new ImageIcon("stop.png"));
	JButton plus = new JButton(new ImageIcon("plus.png"));
	
	JToolBar toolbar = new JToolBar("Toolbar");
	
	JMenuBar menu = new JMenuBar();
	JMenu data = new JMenu("Datei");
	JMenu help = new JMenu("Hilfe");
	JMenuItem search = new JMenuItem("Neuen Suchlauf starten");
	
	JScrollPane contentscroll = new JScrollPane(content);
	JScrollPane overviewscroll = new JScrollPane(overview);
	
	JTextField info = new JTextField("Suche l�uft...");
	
	JFileChooser chooser = new JFileChooser();
	
	WindowAdapter adapter = new WindowAdapter() {
		
		public void windowClosing(WindowEvent e) {
			Object[] options = {"OK", "Abbruch"};
			if (JOptionPane.showOptionDialog(null,
					"Wollen Sie das Programm wirklich beenden?", "",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]) == JOptionPane.OK_OPTION) {
				System.exit(0);
			}
		}
	};
	
	public GUI() {
		setTitle("netCrawler");
		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(adapter);
		
		toolbar.add(refresh);
		toolbar.add(stop);
		toolbar.add(plus);
		
		setJMenuBar(menu);
		menu.add(data);
		menu.add(help);
		data.add(search);
		
		overviewNorth.add(deviceList);
		
		overview.add(overviewNorth, BorderLayout.NORTH);
		overview.add(overviewCenter, BorderLayout.CENTER);
		overview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		overviewscroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		content.add(toolbar, BorderLayout.WEST);
		content.add(buttonOverviewCenter, BorderLayout.CENTER);
		content.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		contentscroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		all.add(contentscroll, BorderLayout.NORTH);
		all.add(overviewscroll, BorderLayout.EAST);
		all.add(info, BorderLayout.SOUTH);
		add(all);
		
		info.setEditable(false);
		
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
		new GUI();
	}
}
