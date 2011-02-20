package at.andiwand.library.graphics;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import at.andiwand.library.util.cli.CommandLine;

import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.JCTermSwing;
import com.jcraft.jcterm.Term;


public class JSimpleTerminalPanel extends JCTermSwing {
	
	private static final long serialVersionUID = -3529309954826071748L;
	
	
	private CommandLine cli;
	
	private List<CloseListener> listeners;
	
	
	public JSimpleTerminalPanel(CommandLine cli) {
		this.cli = cli;
		
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
	
	
	public void addCloseListener(CloseListener listener) {
		listeners.add(listener);
	}
	
	public void removeCloseListener(CloseListener listener) {
		listeners.remove(listener);
	}
	
	
	private class SimpleConnection implements Connection {
		@Override
		public InputStream getInputStream() {
			try {
				return cli.getInputStream();
			} catch (IOException e) {}
			
			return null;
		}
		@Override
		public OutputStream getOutputStream() {
			try {
				return cli.getOutputStream();
			} catch (IOException e) {}
			
			return null;
		}
		
		@Override
		public void requestResize(Term term) {}
		
		@Override
		public void close() {
			try {
				cli.close();
			} catch (IOException e) {}
		}
	}
	
	
	public static interface CloseListener {
		public void closed();
	}
	
}