package at.netcrawler.ui.device;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import at.andiwand.library.cli.CommandLineInterface;

import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.JCTermSwing;
import com.jcraft.jcterm.Term;


/**
 * 
 * A simple terminal panel that is connected with a command line.
 * 
 * @author Andreas Stefl
 * 
 */
public class JSimpleTerminalPanel extends JCTermSwing {
	
	private static final long serialVersionUID = -3529309954826071748L;
	
	
	/**
	 * 
	 * A simple close listener for the terminal frame.
	 * 
	 * @author Andreas Stefl
	 * 
	 */
	public static interface CloseListener {
		/**
		 * Is called when the command line has been closed.
		 */
		public void closed();
	}
	
	
	
	private CommandLineInterface commandLine;
	
	private List<CloseListener> listeners;
	
	
	/**
	 * Creates a new terminal panel with the given command line.
	 * 
	 * @param commandLine the command line which should connected to this
	 * terminal.
	 */
	public JSimpleTerminalPanel(CommandLineInterface commandLine) {
		this.commandLine = commandLine;
		
		listeners = new ArrayList<CloseListener>();
		
		new Thread() {
			public void run() {
				JSimpleTerminalPanel.this.start(new SimpleConnection());
				
				for (CloseListener listener : listeners) {
					listener.closed();
				}
			}
		}.start();
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setSize(e.getComponent().getSize());
				repaint();
			}
		});
	}
	
	
	
	/**
	 * Adds a <code>CloseListener</code> to the terminal panel.
	 * 
	 * @param listener the <code>CloseListener</code> instance.
	 */
	public void addCloseListener(CloseListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes a <code>CloseListener</code> from the terminal panel.
	 * 
	 * @param listener the <code>CloseListener</code> instance.
	 */
	public void removeCloseListener(CloseListener listener) {
		listeners.remove(listener);
	}
	
	
	private class SimpleConnection implements Connection {
		public InputStream getInputStream() {
			try {
				return commandLine.getInputStream();
			} catch (IOException e) {}
			
			return null;
		}
		public OutputStream getOutputStream() {
			try {
				return commandLine.getOutputStream();
			} catch (IOException e) {}
			
			return null;
		}
		
		public void requestResize(Term term) {}
		
		public void close() {
			try {
				commandLine.close();
			} catch (IOException e) {}
		}
	}
	
}