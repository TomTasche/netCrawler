package at.netcrawler.assistant;


public class EncryptionBag {
	
	private Encryption encryption;
	private String password;
	
	
	public EncryptionBag() {
		
	}
	public EncryptionBag(Encryption encryption, String password) {
		this.encryption = encryption;
		this.password = password;
	}
	
	
	public Encryption getEncryption() {
		return encryption;
	}
	public String getPassword() {
		return password;
	}
	
	public void setEncryption(Encryption encryption) {
		this.encryption = encryption;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}