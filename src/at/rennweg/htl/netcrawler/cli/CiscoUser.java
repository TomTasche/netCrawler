package at.rennweg.htl.netcrawler.cli;


public class CiscoUser {
	
	private String username;
	private String password;
	
	
	public CiscoUser(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
}