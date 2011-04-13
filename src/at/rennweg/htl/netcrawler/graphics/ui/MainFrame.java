package at.rennweg.htl.netcrawler.graphics.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import at.andiwand.library.graphics.graph.DrawableVertex;
import at.andiwand.library.graphics.graph.RingGraphLayout;
import at.andiwand.library.graphics.graph.VertexMouseAdapter;
import at.andiwand.library.math.graph.Graph;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoRemoteExecutorFactory;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimpleCiscoSSHExecutorFactory;
import at.rennweg.htl.netcrawler.cli.executor.factory.SimplePacketTracerTelnetExecutorFactory;
import at.rennweg.htl.netcrawler.graphics.graph.JNetworkGraph;
import at.rennweg.htl.netcrawler.network.crawler.NetworkCrawler;
import at.rennweg.htl.netcrawler.network.crawler.SimpleCiscoNetworkCrawler;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;
import at.rennweg.htl.netcrawler.network.graph.NetworkGraph;


public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -2929738858517756241L;
	
	public static final String DEFAULT_TITLE = "NetCrawler";
	
	public static final String FILE_EXTENSION = ".crawl";
	
	
	
	private CrawlerOptionPane crawlerOptionPane = new CrawlerOptionPane(true);
	
	private NetworkGraph networkGraph;
	private JNetworkGraph jNetworkGraph = new JNetworkGraph();
	private JScrollPane scrollPane = new JScrollPane(jNetworkGraph);
	
	private final JMenuItem save = new JMenuItem(new SaveAction());
	private final JMenuItem saveAs = new JMenuItem(new SaveAsAction());
	private final JMenuItem saveDocumentation = new JMenuItem(new SaveDocumentationAction());
	private final JCheckBoxMenuItem magnetic = new JCheckBoxMenuItem(new MagneticAction());
	
	private File file;
	private JFileChooser fileChooser = new JFileChooser();
	
	
	public MainFrame() {
		this(DEFAULT_TITLE);
	}
	public MainFrame(String title) {
		super(title);
		setLayout(new BorderLayout());
		
		
		JMenuBar menuBar = new JMenuBar();
		
		save.setEnabled(false);
		saveAs.setEnabled(false);
		saveDocumentation.setEnabled(false);
		
		JMenu file = new JMenu("File");
		file.add(new JMenuItem(new NewAction()));
		file.add(new JMenuItem(new OpenAction()));
		file.addSeparator();
		file.add(save);
		file.add(saveAs);
		file.add(saveDocumentation);
		file.addSeparator();
		file.add(new JMenuItem(new ExitAction()));
		menuBar.add(file);
		
		magnetic.setSelected(true);
		
		JMenu view = new JMenu("View");
		view.add(magnetic);
		menuBar.add(view);
		
		JMenu help = new JMenu("Help");
		menuBar.add(help);
		
		setJMenuBar(menuBar);
		
		
		jNetworkGraph.setGraphLayout(new RingGraphLayout(jNetworkGraph));
		jNetworkGraph.setAntialiasing(true);
		jNetworkGraph.setMagneticLines(true);
		jNetworkGraph.addVertexMouseListener(new DeviceListener());
		
		add(scrollPane);
		
		
		fileChooser.setFileFilter(new FileFilter() {
			public String getDescription() {
				return "NetCrawler file (*.crawl)";
			}
			public boolean accept(File f) {
				return f.getName().endsWith(FILE_EXTENSION);
			}
		});
		
		
		//TODO: kill me!
		try {
			SimpleCiscoUser masterUser = new SimpleCiscoUser("cisco", "cisco");
			Inet4Address deviceAddress = (Inet4Address) Inet4Address.getByName("192.168.0.153");
			Inet4Address deviceGateway = (Inet4Address) Inet4Address.getByName("192.168.0.254");
			crawlerOptionPane.setMasterUser(masterUser);
			crawlerOptionPane.setPTDeviceAddress(deviceAddress);
			crawlerOptionPane.setPTDeviceGateway(deviceGateway);
			crawlerOptionPane.setPTNetworkName("Simple Bridge");
		} catch (UnknownHostException e) {}
	}
	
	
	private class NewAction extends AbstractAction {
		private static final long serialVersionUID = 7678674214495381468L;
		private static final String name = "New...";
		
		public NewAction() {
			putValue(NAME, name);
			putValue(SMALL_ICON, null);
		}
		
		public void actionPerformed(ActionEvent event) {
			int result = crawlerOptionPane.showOptionPane(MainFrame.this);
			
			if (result == CrawlerOptionPane.CANCEL_OPTION) return;
			
			try {
				SimpleCiscoRemoteExecutorFactory executorFactory;
				
				if (crawlerOptionPane.isPTUsed()) {
					executorFactory = new SimplePacketTracerTelnetExecutorFactory(
							crawlerOptionPane.getPTAddress(), crawlerOptionPane.getPTPort(),
							crawlerOptionPane.getPTNetworkName(),
							(Inet4Address) crawlerOptionPane.getPTDeviceAddress(),
							(Inet4Address) crawlerOptionPane.getPTDeviceGateway());
				} else {
					executorFactory = new SimpleCiscoSSHExecutorFactory();
				}
				
				final NetworkCrawler networkCrawler = new SimpleCiscoNetworkCrawler(executorFactory, crawlerOptionPane.getMasterUser(), crawlerOptionPane.getRootAddress());
				networkGraph = new NetworkGraph();
				jNetworkGraph.setGraph(networkGraph);
				
				//TODO: fix me!
				new Thread() {
					public void run() {
						try {
							networkCrawler.crawl(networkGraph);
							
							save.setEnabled(true);
							saveAs.setEnabled(true);
							saveDocumentation.setEnabled(true);
						} catch (Exception e) {}
					}
				}.start();
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(MainFrame.this, t, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private class OpenAction extends AbstractAction {
		private static final long serialVersionUID = 307715643338394524L;
		private static final String name = "Open...";
		
		public OpenAction() {
			putValue(NAME, name);
			putValue(SMALL_ICON, null);
		}
		
		public void actionPerformed(ActionEvent event) {
			int result = fileChooser.showOpenDialog(MainFrame.this);
			
			if (result != JFileChooser.APPROVE_OPTION) return;
			
			file = fileChooser.getSelectedFile();
			
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				remove(jNetworkGraph);
				jNetworkGraph = (JNetworkGraph) objectInputStream.readObject();
				jNetworkGraph.addVertexMouseListener(new DeviceListener());
				networkGraph = (NetworkGraph) (Graph<?, ?>) jNetworkGraph.getGraphModel();
				scrollPane.setViewportView(jNetworkGraph);
				
				save.setEnabled(true);
				saveAs.setEnabled(true);
				saveDocumentation.setEnabled(true);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(MainFrame.this, e, "Error", JOptionPane.ERROR_MESSAGE);
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(MainFrame.this, e, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					inputStream.close();
				} catch (Throwable t) {}
			}
		}
	}
	private class SaveAction extends AbstractAction {
		private static final long serialVersionUID = -2714731919325318935L;
		private static final String name = "Save";
		
		public SaveAction() {
			putValue(NAME, name);
			putValue(SMALL_ICON, null);
		}
		
		public void actionPerformed(ActionEvent event) {
			if (file == null) {
				saveAs.doClick();
				return;
			}
			
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
				objectOutputStream.writeObject(jNetworkGraph);
				objectOutputStream.flush();
				outputStream.flush();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(MainFrame.this, e, "Error", JOptionPane.ERROR_MESSAGE);
			} finally {
				try {
					outputStream.close();
				} catch (Throwable t) {}
			}
		}
	}
	private class SaveAsAction extends AbstractAction {
		private static final long serialVersionUID = -8186442045718807878L;
		private static final String name = "Save as...";
		
		public SaveAsAction() {
			putValue(NAME, name);
			putValue(SMALL_ICON, null);
		}
		
		public void actionPerformed(ActionEvent event) {
			int result = fileChooser.showSaveDialog(MainFrame.this);
			
			if (result != JFileChooser.APPROVE_OPTION) return;
			
			file = fileChooser.getSelectedFile();
			
			save.doClick();
		}
	}
	private class SaveDocumentationAction extends AbstractAction {
		private static final long serialVersionUID = -3391944060824212173L;
		private static final String name = "Save documentation...";
		
		public SaveDocumentationAction() {
			putValue(NAME, name);
			putValue(SMALL_ICON, null);
		}
		
		public void actionPerformed(ActionEvent e) {
			
		}
	}
	private class ExitAction extends AbstractAction {
		private static final long serialVersionUID = 7712125312575525479L;
		private static final String name = "Exit";
		
		public ExitAction() {
			putValue(NAME, name);
			putValue(SMALL_ICON, null);
		}
		
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	private class MagneticAction extends AbstractAction {
		private static final long serialVersionUID = -632518238515384240L;
		private static final String name = "Magnetic lines";
		
		public MagneticAction() {
			putValue(NAME, name);
			putValue(SMALL_ICON, null);
		}
		
		public void actionPerformed(ActionEvent e) {
			jNetworkGraph.setMagneticLines(magnetic.isSelected());
		}
	}
	
	private class DeviceListener extends VertexMouseAdapter {
		private static final long serialVersionUID = -3498444227661231579L;
		
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON1) return;
			
			DrawableVertex source = (DrawableVertex) e.getSource();
			Object vertex = source.getCoveredVertex();
			
			if (!(vertex instanceof CiscoDevice)) return;
			CiscoDevice device = (CiscoDevice) vertex;
			
			CiscoDeviceViewer deviceViewer = new CiscoDeviceViewer(device);
			deviceViewer.showDialog(MainFrame.this);
		}
	}
	
}