package test;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;


public class TestJSch {
	
	public static void main(String[] arg) throws Throwable{
		JSch jsch = new JSch();
		
		String login = JOptionPane.showInputDialog("Enter username@hostname", System.getProperty("user.name") + "@localhost");
		String user = login.split("@")[0];
		String host = login.split("@")[1];
		
		Session session = jsch.getSession(user, host, 22);
		
		JPasswordField passwordField = new JPasswordField();
		int result = JOptionPane.showConfirmDialog(null, new Object[] {passwordField}, "your password", JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.CANCEL_OPTION) System.exit(0);
		
		UserInfo userInfo = new SimpleUserInfo(new String(passwordField.getPassword()));
		
		session.setUserInfo(userInfo);
		session.connect(30000);
		
		Channel channel = session.openChannel("shell");
		channel.setInputStream(System.in);
		channel.connect();
		channel.setOutputStream(System.out);
	}
	
	public static class SimpleUserInfo implements UserInfo {
		private String password;
		
		
		public SimpleUserInfo(String password) {
			this.password = password;
		}
		
		
		public String getPassword() {
			return password;
		}
		public String getPassphrase() {
			return null;
		}
		
		
		public boolean promptYesNo(String str){
			return true;
		}
		
		public boolean promptPassphrase(String message) {
			return true;
		}
		
		public boolean promptPassword(String message) {
			return true;
		}
		
		public void showMessage(String message) {}
	}
	
}