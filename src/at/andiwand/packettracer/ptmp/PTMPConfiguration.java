package at.andiwand.packettracer.ptmp;

import java.util.Arrays;


public class PTMPConfiguration {
	
	public static final int ENCODING_TEXT	= 1;
	public static final int ENCODING_BINARY	= 2;
	
	public static final int ENCRYPTION_NONE	= 1;
	public static final int ENCRYPTION_XOR	= 2;
	
	public static final int COMPRESSION_NO				= 1;
	public static final int COMPRESSION_ZLIB_DEFAULT	= 2;
	
	public static final int AUTHENTICATION_CLEAR_TEXT	= 1;
	public static final int AUTHENTICATION_SIMPLE		= 2;
	public static final int AUTHENTICATION_MD5			= 4;
	
	public static final PTMPConfiguration DEFAULT =
		new PTMPConfiguration(ENCODING_TEXT, ENCRYPTION_NONE, COMPRESSION_NO, AUTHENTICATION_CLEAR_TEXT);
	
	
	private final int encoding;
	private final int encryption;
	private final int compression;
	private final int authentication;
	
	
	public PTMPConfiguration(int encoding, int encryption, int compression, int authentication) {
		this.encoding = encoding;
		this.encryption = encryption;
		this.compression = compression;
		this.authentication = authentication;
	}
	
	
	@Override
	public String toString() {
		return "encoding: " + encoding + "; " +
			"encryption: " + encryption + "; " +
			"compression: " + compression + "; " +
			"authentication: " + authentication + ";";
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		
		if (!(obj instanceof PTMPConfiguration)) return false;
		PTMPConfiguration configuration = (PTMPConfiguration) obj;
		
		return (encoding == configuration.encoding) &&
			(encryption == configuration.encryption) &&
			(compression == configuration.compression) &&
			(authentication == configuration.authentication);
	}
	@Override
	public int hashCode() {
		return Arrays.hashCode(new int[] {encoding, encryption, compression, authentication});
	}
	
	
	public int encoding() {
		return encoding;
	}
	public int encryption() {
		return encryption;
	}
	public int compression() {
		return compression;
	}
	public int authentication() {
		return authentication;
	}
	
}