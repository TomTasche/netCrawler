package at.rennweg.htl.netcrawler.cli;


public class SimpleCiscoUser {
	
	private String username;
	private String password;
	
	
	public SimpleCiscoUser(String username, String password) {
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