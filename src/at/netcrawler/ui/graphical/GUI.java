package at.netcrawler.ui.graphical;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import at.andiwand.library.component.JFrameUtil;
import at.andiwand.library.network.ip.IPv4Address;
import at.netcrawler.component.CrapGraphLayout;
import at.netcrawler.component.TopologyViewer;
import at.netcrawler.io.json.JsonHelper;
import at.netcrawler.network.connection.ConnectionGateway;
import at.netcrawler.network.connection.ssh.LocalSSHGateway;
import at.netcrawler.network.connection.ssh.SSHSettings;
import at.netcrawler.network.connection.ssh.SSHVersion;
import at.netcrawler.network.crawler.SimpleNetworkCrawler;
import at.netcrawler.network.manager.DeviceManagerFactory;
import at.netcrawler.network.manager.cli.CommandLineDeviceManagerFactory;
import at.netcrawler.network.topology.HashTopology;
import at.netcrawler.network.topology.Topology;
import at.netcrawler.network.topology.TopologyDevice;
import at.netcrawler.ui.graphical.device.DeviceView;
import at.netcrawler.util.Settings;


@SuppressWarnings("serial")
public class GUI extends JFrame {
	
	private JScrollPane scrollPane;
	private TopologyViewer viewer;
	private DeviceTable table;
	private JLabel statusLabel;
	private JMenuItem saveItem;
	private JFileChooser fileChooser;
	private Topology topology;
	private boolean dontClose;
	private boolean tableVisible;
	
	public GUI(Topology topology) {
		this.topology = topology;
		
		setTitle("netCrawler");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				
				Settings.setLastWindowSize(getSize());
			}
		});
		
		fileChooser = new JFileChooser(Settings.getLastCrawl());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return "*.crawl - Saved netCrawler Crawls";
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".crawl");
			}
		});
		
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
		JMenu helpMenu = new JMenu("Help");
		JMenuItem crawlItem = new JMenuItem("Crawl...");
		JMenuItem loadItem = new JMenuItem("Load");
		saveItem = new JMenuItem("Save");
		JMenuItem closeItem = new JMenuItem("Exit");
		JMenuItem toggleViewItem = new JMenuItem("Toggle view");
		
		saveItem.setEnabled(false);
		
		loadItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (fileChooser.showOpenDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
					fileChooser.getSelectedFile();
				}
			}
		});
		saveItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(JsonHelper.getGson().toJson(
						GUI.this.topology));
			}
		});
		closeItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		crawlItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				crawl();
			}
		});
		toggleViewItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleView();
			}
		});
		
		fileMenu.add(crawlItem);
		fileMenu.add(loadItem);
		fileMenu.add(saveItem);
		fileMenu.add(closeItem);
		viewMenu.add(toggleViewItem);
		
		menu.add(fileMenu);
		menu.add(viewMenu);
		menu.add(helpMenu);
		
		setJMenuBar(menu);
		
		table = new DeviceTable(this);
		table.setTopology(topology);
		
		viewer = new TopologyViewer();
		// TODO: use another GraphLayout
		viewer.setGraphLayout(new CrapGraphLayout(viewer));
		viewer.setModel(topology);
		viewer.addRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		viewer.addRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		viewer.addVertexMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouse(e, (TopologyDevice) e.getSource());
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(400, 400));
		if (Settings.getLastView() == 1) {
			scrollPane.setViewportView(table);
			
			tableVisible = true;
		} else {
			scrollPane.setViewportView(viewer);
		}
		
		statusLabel = new JLabel();
		statusLabel
				.setText("Start a new crawl or load an old one using the menu above...");
		
		add(scrollPane, BorderLayout.CENTER);
		add(statusLabel, BorderLayout.SOUTH);
		
		Dimension lastSize = Settings.getLastWindowSize();
		if (lastSize == null) {
			pack();
			setMinimumSize(getSize());
		} else {
			setSize(lastSize);
		}
		
		JFrameUtil.centerFrame(this);
		
		setVisible(true);
	}
	
	protected void handleMouse(MouseEvent event, TopologyDevice device) {
		JFrame frame = null;
		if (event.getButton() == MouseEvent.BUTTON1) {
			frame = new DeviceView(device);
		} else if (event.getButton() == MouseEvent.BUTTON3) {
			frame = new BatchManager(device);
		}
		
		if (frame != null) {
			JFrameUtil.centerFrame(frame);
			frame.setVisible(true);
		}
	}
	
	private void close() {
		if (dontClose
				&& JOptionPane.showOptionDialog(GUI.this,
						"Do you really want to close netCrawler?", "",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
			dispose();
		} else if (!dontClose) {
			dispose();
		}
		
		Settings.write();
	}
	
	private void crawl() {
		dontClose = true;
		
		statusLabel.setText("Crawling your net...");
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		// TODO: hotfix
		new Thread() {
			public void run() {
				ConnectionGateway gateway = new LocalSSHGateway();
				SSHSettings settings = new SSHSettings();
				settings.setVersion(SSHVersion.VERSION2);
				settings.setUsername("cisco");
				settings.setPassword("cisco");
				DeviceManagerFactory managerFactory = new CommandLineDeviceManagerFactory();
				IPv4Address start = new IPv4Address("192.168.0.254");
				SimpleNetworkCrawler crawler = new SimpleNetworkCrawler(gateway,
						settings, managerFactory, start);
				
				try {
					topology = new HashTopology();
					viewer.setModel(topology);
					table.setTopology(topology);
					crawler.crawl(topology);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				saveItem.setEnabled(true);
				
				setCursor(Cursor.getDefaultCursor());
				statusLabel.setText("Crawl completed.");
				
				dontClose = false;
			}
		}.start();
	}
	
	private void toggleView() {
		int newView = 0;
		if (tableVisible) {
			scrollPane.setViewportView(viewer);
		} else {
			scrollPane.setViewportView(table);
			
			newView = 1;
		}
		
		Settings.setLastView(newView);
		
		tableVisible = !tableVisible;
	}
	
//	public static void main(String[] args) throws ClassNotFoundException,
//			InstantiationException, IllegalAccessException,
//			UnsupportedLookAndFeelException {
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		
//		Topology topology = new HashTopology();
//		
//		NetworkDevice deviceA = new NetworkDevice();
//		NetworkInterface interfaceA = new NetworkInterface();
//		interfaceA.setValue(NetworkInterface.NAME, "eth0");
//		deviceA.setValue(NetworkDevice.HOSTNAME, "RouterA");
//		deviceA.setValue(NetworkDevice.INTERFACES,
//				new HashSet<NetworkInterface>(Arrays.asList(interfaceA)));
//		
//		NetworkDevice deviceB = new NetworkDevice();
//		NetworkInterface interfaceB = new NetworkInterface();
//		interfaceB.setValue(NetworkInterface.NAME, "eth0");
//		deviceB.setValue(NetworkDevice.HOSTNAME, "RouterB");
//		deviceB.setValue(NetworkDevice.INTERFACES,
//				new HashSet<NetworkInterface>(Arrays.asList(interfaceB)));
//		
//		NetworkCable cable = new NetworkCable();
//		
//		TopologyDevice topologyDeviceA = new TopologyDevice(
//				new UniqueDeviceIdentifier(), deviceA);
//		TopologyInterface topologyInterfaceA = new TopologyInterface(interfaceA);
//		TopologyDevice topologyDeviceB = new TopologyDevice(
//				new UniqueDeviceIdentifier(), deviceB);
//		TopologyInterface topologyInterfaceB = new TopologyInterface(interfaceB);
//		TopologyCable topologyCable = new TopologyCable(cable,
//				new HashSet<TopologyInterface>(Arrays.asList(
//						topologyInterfaceA, topologyInterfaceB)));
//		
//		topology.addVertex(topologyDeviceA);
//		topology.addVertex(topologyDeviceB);
//		topology.addEdge(topologyCable);
//		
//		deviceA.setValue(NetworkDevice.MAJOR_CAPABILITY, Capability.ROUTER);
//		deviceB.setValue(NetworkDevice.MAJOR_CAPABILITY, Capability.SWITCH);
//		
//		new GUI(topology);
//	}
}
