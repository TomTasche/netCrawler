package at.netcrawler.cli.agent;

public class CiscoCommandLineAgentSettings extends CommandLineAgentSettings {
	
	private String logonUsername;
	private String logonPassword;
	
	private String enablePassword;
	
	public String getLogonUsername() {
		return logonUsername;
	}
	
	public String getLogonPassword() {
		return logonPassword;
	}
	
	public String getEnablePassword() {
		return enablePassword;
	}
	
	public void setLogonUsername(String logonUsername) {
		this.logonUsername = logonUsername;
	}
	
	public void setLogonPassword(String logonPassword) {
		this.logonPassword = logonPassword;
	}
	
	public void setEnablePassword(String enablePassword) {
		this.enablePassword = enablePassword;
	}
	
}