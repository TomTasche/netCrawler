package at.rennweg.htl.netcrawler.graphics.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import at.andiwand.library.util.JFrameUtil;
import at.rennweg.htl.netcrawler.cli.SimpleCiscoUser;


public class CrawlerOptionPane extends JDialog {
	
	private static final long serialVersionUID = -2037600289769317019L;
	
	
	public static final String DEFAULT_TITLE = "Crawler Options";
	
	public static final int CANCEL_OPTION = 0;
	public static final int ACCEPT_OPTION = 1;
	
	
	
	
	private final JTextField rootAddress = new JTextField();
	private final JTextField masterUser = new JTextField();
	private final JPasswordField masterPassword = new JPasswordField();
	private final JCheckBox multithreading = new JCheckBox("Multithreading");
	private final JTextField threadCount = new JTextField("10");
	private final JCheckBox ptTroggle = new JCheckBox("Use Packet Tracer");
	private final JTextField ptAddress = new JTextField("localhost");
	private final JTextField ptPort = new JTextField("38000");
	private final JTextField ptNetworkName = new JTextField();
	private final JTextField ptDeviceAddress = new JTextField();
	private final JTextField ptDeviceGateway = new JTextField();
	
	private int lastReturnValue;
	
	
	
	public CrawlerOptionPane() {
		this(DEFAULT_TITLE);
	}
	public CrawlerOptionPane(boolean packetTracerOptions) {
		this(DEFAULT_TITLE, packetTracerOptions);
	}
	public CrawlerOptionPane(String title) {
		this(title, false);
	}
	public CrawlerOptionPane(String title, boolean packetTracerOptions) {
		super();
		setTitle(title);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		setLayout(groupLayout);
		
		rootAddress.setPreferredSize(new Dimension(150, rootAddress.getPreferredSize().height));
		JLabel rootAddressLabel = new JLabel("Root address:");
		JLabel masterUserLabel = new JLabel("Master user:");
		JLabel masterPasswordLabel = new JLabel("Master password:");
		JLabel threadCountLabel = new JLabel("Thread count:");
		
		ParallelGroup horizontalGroup = groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup()
								.addComponent(rootAddressLabel)
								.addComponent(masterUserLabel)
								.addComponent(masterPasswordLabel)
								.addComponent(threadCountLabel))
						.addGroup(groupLayout.createParallelGroup()
								.addComponent(rootAddress)
								.addComponent(masterUser)
								.addComponent(masterPassword)
								.addComponent(threadCount)))
				.addComponent(multithreading)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(threadCountLabel)
						.addComponent(threadCount));
		
		SequentialGroup verticalGroup = groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rootAddressLabel)
						.addComponent(rootAddress))
				.addGap(20)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(masterUserLabel)
						.addComponent(masterUser))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(masterPasswordLabel)
						.addComponent(masterPassword))
				.addGap(20)
				.addComponent(multithreading)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(threadCountLabel)
						.addComponent(threadCount));
		
		multithreading.setSelected(true);
		multithreading.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				threadCount.setEnabled(multithreading.isSelected());
			}
		});
		
		if (packetTracerOptions) {
			final JPanel ptPanel = new JPanel();
			ptPanel.setBorder(BorderFactory.createTitledBorder("Packet Tracer"));
			
			JLabel ptAddressLabel = new JLabel("Address:");
			JLabel ptPortLabel = new JLabel("Port:");
			
			JLabel ptNetworkNameLabel = new JLabel("Network name:");
			
			JLabel ptDeviceAddressLabel = new JLabel("Device Address:");
			JLabel ptDeviceGatewayLabel = new JLabel("Device Gateway:");
			
			GroupLayout ptGroupLayout = new GroupLayout(ptPanel);
			ptGroupLayout.setAutoCreateContainerGaps(true);
			ptGroupLayout.setAutoCreateGaps(true);
			ptPanel.setLayout(ptGroupLayout);
			
			ptAddress.setPreferredSize(new Dimension(135, ptAddress.getPreferredSize().height));
			ptPort.setPreferredSize(new Dimension(60, ptPort.getPreferredSize().height));
			
			ParallelGroup ptHorizontalGroup = ptGroupLayout.createParallelGroup()
					.addGroup(ptGroupLayout.createSequentialGroup()
							.addComponent(ptAddressLabel)
							.addComponent(ptAddress)
							.addComponent(ptPortLabel)
							.addComponent(ptPort))
					.addGroup(ptGroupLayout.createSequentialGroup()
							.addGroup(ptGroupLayout.createParallelGroup()
									.addComponent(ptNetworkNameLabel)
									.addComponent(ptDeviceAddressLabel)
									.addComponent(ptDeviceGatewayLabel))
							.addGroup(ptGroupLayout.createParallelGroup()
									.addComponent(ptNetworkName)
									.addComponent(ptDeviceAddress)
									.addComponent(ptDeviceGateway)));
			
			SequentialGroup ptverticalGroup = ptGroupLayout.createSequentialGroup()
					.addGroup(ptGroupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(ptAddressLabel)
							.addComponent(ptAddress)
							.addComponent(ptPortLabel)
							.addComponent(ptPort))
					.addGroup(ptGroupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(ptNetworkNameLabel)
							.addComponent(ptNetworkName))
					.addGroup(ptGroupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(ptDeviceAddressLabel)
							.addComponent(ptDeviceAddress))
					.addGroup(ptGroupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(ptDeviceGatewayLabel)
							.addComponent(ptDeviceGateway));
			
			ptGroupLayout.setHorizontalGroup(ptHorizontalGroup);
			ptGroupLayout.setVerticalGroup(ptverticalGroup);
			
			ptTroggle.setSelected(true);
			ptTroggle.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (ptTroggle.isSelected()) {
						ptPanel.setEnabled(true);
						ptAddress.setEnabled(true);
						ptPort.setEnabled(true);
						ptNetworkName.setEnabled(true);
						ptDeviceAddress.setEnabled(true);
						ptDeviceGateway.setEnabled(true);
					} else {
						ptPanel.setEnabled(false);
						ptAddress.setEnabled(false);
						ptPort.setEnabled(false);
						ptNetworkName.setEnabled(false);
						ptDeviceAddress.setEnabled(false);
						ptDeviceGateway.setEnabled(false);
					}
				}
			});
			
			horizontalGroup.addComponent(ptTroggle);
			verticalGroup.addGap(20);
			verticalGroup.addComponent(ptTroggle);
			
			horizontalGroup.addComponent(ptPanel);
			verticalGroup.addComponent(ptPanel);
		}
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lastReturnValue = CANCEL_OPTION;
				
				setVisible(false);
			}
		});
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkInput()) return;
				
				lastReturnValue = ACCEPT_OPTION;
				
				setVisible(false);
			}
		});
		
		horizontalGroup.addGap(0, 0, Short.MAX_VALUE);
		horizontalGroup.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
				.addComponent(cancel)
				.addComponent(ok));
		
		verticalGroup.addGap(10, 10, Short.MAX_VALUE);
		verticalGroup.addGroup(groupLayout.createParallelGroup()
				.addComponent(cancel)
				.addComponent(ok));
		
		groupLayout.setHorizontalGroup(horizontalGroup);
		groupLayout.setVerticalGroup(verticalGroup);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				lastReturnValue = CANCEL_OPTION;
			}
		});
		
		pack();
		
		setModal(true);
	}
	
	
	
	public void setRootAddress(InetAddress address) {
		rootAddress.setText(address.getHostAddress());
	}
	public void setMasterUser(SimpleCiscoUser user) {
		masterUser.setText(user.getUsername());
		masterPassword.setText(user.getPassword());
	}
	public void setMultithreading(boolean multithreading) {
		if (this.multithreading.isSelected() != multithreading) this.multithreading.doClick();
	}
	public void setThreadCount(int threadCount) {
		this.threadCount.setText("" + threadCount);
	}
	public void setPTUsed(boolean usePT) {
		if (ptTroggle.isSelected() != usePT) ptTroggle.doClick();
	}
	public void setPTAddress(InetAddress address) {
		ptAddress.setText(address.getHostAddress());
	}
	public void setPTPort(int port) {
		ptPort.setText("" + port);
	}
	public void setPTNetworkName(String name) {
		ptNetworkName.setText(name);
	}
	public void setPTDeviceAddress(InetAddress address) {
		ptDeviceAddress.setText(address.getHostAddress());
	}
	public void setPTDeviceGateway(InetAddress gateway) {
		ptDeviceGateway.setText(gateway.getHostAddress());
	}
	
	public InetAddress getRootAddress() {
		try {
			return InetAddress.getByName(rootAddress.getText());
		} catch (UnknownHostException e) {
			return null;
		}
	}
	public SimpleCiscoUser getMasterUser() {
		return new SimpleCiscoUser(masterUser.getText(), new String(masterPassword.getPassword()));
	}
	public boolean isMultithreading() {
		return multithreading.isSelected();
	}
	public int getThreadCount() {
		return Integer.parseInt(threadCount.getText());
	}
	public boolean isPTUsed() {
		return ptTroggle.isSelected();
	}
	public InetAddress getPTAddress() {
		try {
			return InetAddress.getByName(ptAddress.getText());
		} catch (UnknownHostException e) {
			return null;
		}
	}
	public int getPTPort() {
		return Integer.parseInt(ptPort.getText());
	}
	public String getPTNetworkName() {
		return ptNetworkName.getText();
	}
	public InetAddress getPTDeviceAddress() {
		try {
			return InetAddress.getByName(ptDeviceAddress.getText());
		} catch (UnknownHostException e) {
			return null;
		}
	}
	public InetAddress getPTDeviceGateway() {
		try {
			return InetAddress.getByName(ptDeviceGateway.getText());
		} catch (UnknownHostException e) {
			return null;
		}
	}
	
	
	public int showOptionPane(JFrame parent) {
		if (parent == null) {
			JFrameUtil.centerFrame(this);
		} else {
			JFrameUtil.centerFrame(this, parent);
		}
		
		setVisible(true);
		
		return lastReturnValue;
	}
	
	private boolean checkInput() {
		try {
			if (rootAddress.getText().isEmpty()) throw new IllegalArgumentException("Root address is empty!");
			InetAddress.getByName(rootAddress.getText());
			
			if (threadCount.getText().isEmpty()) throw new IllegalArgumentException("Thread count is empty!");
			int threadCount = Integer.parseInt(this.threadCount.getText());
			if (threadCount < 0) throw new IllegalArgumentException("Thread count is illegal!");
			
			if (ptTroggle.isSelected()) {
				if (ptAddress.getText().isEmpty()) throw new IllegalArgumentException("Packet Tracer address is empty!");
				InetAddress.getByName(ptAddress.getText());
				
				if (ptPort.getText().isEmpty()) throw new IllegalArgumentException("Packet Tracer Port is empty!");
				int port = Integer.parseInt(ptPort.getText());
				if ((port < 0) || (port > 65535)) throw new IllegalArgumentException("Packet Tracer Port number is illegal!");
				
				if (ptDeviceAddress.getText().isEmpty()) throw new IllegalArgumentException("Packet Tracer device address is empty!");
				InetAddress.getByName(ptDeviceAddress.getText());
				if (ptDeviceGateway.getText().isEmpty()) throw new IllegalArgumentException("Packet Tracer device gateway is empty!");
				InetAddress.getByName(ptDeviceGateway.getText());
			}
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(this, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
}