package at.netcrawler.prototype;


public class EncryptionBag {
	
	public Encryption encryption;
	public String password;
	
	
	public EncryptionBag() {
		
	}
	
	public EncryptionBag(Encryption encryption, String password) {
		this.encryption = encryption;
		this.password = password;
	}
	
}