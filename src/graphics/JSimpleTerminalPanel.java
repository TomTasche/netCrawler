package graphics;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.InputStream;
import java.io.OutputStream;

import network.ssh.SimpleSSHClient;

import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.JCTermSwing;
import com.jcraft.jcterm.Term;


public class JSimpleTerminalPanel extends JCTermSwing {
	
	private static final long serialVersionUID = -3529309954826071748L;
	
	
	private SimpleSSHClient sshClient;
	
	
	public JSimpleTerminalPanel(SimpleSSHClient sshClient) {
		this.sshClient = sshClient;
		
		new Thread() {
			public void run() {
				JSimpleTerminalPanel.this.start(new SimpleConnection());
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
	
	
	private class SimpleConnection implements Connection {
		@Override
		public InputStream getInputStream() {
			return sshClient.getInputStream();
		}
		@Override
		public OutputStream getOutputStream() {
			return sshClient.getOutputStream();
		}
		
		@Override
		public void requestResize(Term term) {}
		
		@Override
		public void close() {
			sshClient.disconnect();
		}
	}
	
}