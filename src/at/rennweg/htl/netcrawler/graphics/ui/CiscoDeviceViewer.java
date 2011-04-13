package at.rennweg.htl.netcrawler.graphics.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import at.andiwand.library.util.JFrameUtil;
import at.rennweg.htl.netcrawler.network.graph.CiscoDevice;


public class CiscoDeviceViewer extends JDialog {
	
	private static final long serialVersionUID = 2073201906243870338L;
	
	private static final Font COMMAND_FONT = new Font("monospaced", Font.PLAIN, 12);
	
	
	
	private final CiscoDevice device;
	
	
	public CiscoDeviceViewer(CiscoDevice device) {
		this(device.getHostname(), device);
	}
	public CiscoDeviceViewer(String title, CiscoDevice device) {
		setTitle(title);
		setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		JTextArea version = new JTextArea(device.getShowVersion());
		version.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		version.setFont(COMMAND_FONT);
		version.setEditable(false);
		tabbedPane.addTab("show version", new JScrollPane(version));
		
		JTextArea flash = new JTextArea(device.getDirFlash());
		flash.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		flash.setFont(COMMAND_FONT);
		flash.setEditable(false);
		tabbedPane.addTab("dir flash", new JScrollPane(flash));
		
		JTextArea runningConfiguration = new JTextArea(device.getShowRunningConfiguration());
		runningConfiguration.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		runningConfiguration.setFont(COMMAND_FONT);
		runningConfiguration.setEditable(false);
		tabbedPane.addTab("show running-config", new JScrollPane(runningConfiguration));
		
		JTextArea interfaceBrief = new JTextArea(device.getShowIpInterfaceBrief());
		interfaceBrief.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		interfaceBrief.setFont(COMMAND_FONT);
		interfaceBrief.setEditable(false);
		tabbedPane.addTab("show ip interface brief", new JScrollPane(interfaceBrief));
		
		add(tabbedPane);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				CiscoDeviceViewer.this.dispose();
			}
		});
		
		this.device = device;
		
		setSize(500, 400);
	}
	
	
	public void showDialog(JFrame parent) {
		JFrameUtil.centerFrame(this, parent);
		setVisible(true);
	}
	
	public CiscoDevice getDevice() {
		return device;
	}
	
}