package at.netcrawler.ui.device;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import at.andiwand.library.cli.CommandLineInterface;
import at.andiwand.library.component.JFrameUtil;


/**
 * 
 * A simple terminal frame that is connected with a command line.
 * 
 * @author Andreas Stefl
 * 
 */
public class JSimpleTerminal extends JFrame {
	
	private static final long serialVersionUID = -78836768236278425L;
	
	
	/**
	 * The default title of the terminal.
	 */
	public static final String DEFAULT_TITLE = "Terminal";
	
	
	
	/**
	 * Creates a new terminal frame with the given command line. <br>
	 * Note: the frame is invisible by default.
	 * 
	 * @param commandLine the command line which should connected to this
	 * terminal.
	 */
	public JSimpleTerminal(CommandLineInterface commandLine) {
		this(DEFAULT_TITLE, commandLine);
	}
	
	/**
	 * Creates a new terminal frame with the given command line. <br>
	 * Note: the frame is invisible by default.
	 * 
	 * @param title the title of the terminal frame.
	 * @param commandLine the command line which should connected to this
	 * terminal.
	 */
	public JSimpleTerminal(String title, final CommandLineInterface commandLine) {
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
	}
	
	
	@Override
	public void setVisible(boolean b) {
		if (b) JFrameUtil.centerFrame(this);
		
		super.setVisible(b);
	}
	
	public static void main(String[] args) {
		JSimpleTerminal terminal = new JSimpleTerminal(commandLine);
		
		terminal.setVisible(true);
	}
}