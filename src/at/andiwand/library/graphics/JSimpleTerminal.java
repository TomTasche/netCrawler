package at.andiwand.library.graphics;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import at.andiwand.library.util.JFrameUtil;
import at.andiwand.library.util.cli.CommandLine;



public class JSimpleTerminal extends JFrame {
	
	private static final long serialVersionUID = -78836768236278425L;
	
	public static final String DEFAULT_TITLE = "Terminal";
	
	
	public JSimpleTerminal(CommandLine commandLine) {
		this(DEFAULT_TITLE, commandLine);
	}
	public JSimpleTerminal(String title, final CommandLine commandLine) {
		super(title);
		setLayout(new BorderLayout());
		
		JSimpleTerminalPanel terminalPanel = new JSimpleTerminalPanel(commandLine);
		terminalPanel.addCloseListener(new JSimpleTerminalPanel.CloseListener() {
			public void closed() {
				JSimpleTerminal.this.dispose();
			}
		});
		add(new JScrollPane(terminalPanel));
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		pack();
		JFrameUtil.centerFrame(this);
	}
	
}