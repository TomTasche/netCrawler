package at.netcrawler.assistant;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public enum Encryption {
	
	PLAIN("Plain", null, null) {
		public InputStream getCipherInputStream(InputStream inputStream,
				String password) {
			return inputStream;
		}
		
		public OutputStream getCipherOutputStream(OutputStream outputStream,
				String password) {
			return outputStream;
		}
	},
	DES("DES"), AES("AES");
	
	private static final String KEY_CHARSET = "utf-8";
	
	private static final String HASHING_ALGORITHM = "MD5";
	
	private static final Map<String, Encryption> NAME_MAP;
	
	static {
		Map<String, Encryption> nameMap = new HashMap<String, Encryption>();
		
		for (Encryption encryption : values()) {
			nameMap.put(encryption.name, encryption);
		}
		
		NAME_MAP = Collections.unmodifiableMap(nameMap);
	}
	
	public static Encryption getEncryptionByName(String name) {
		return NAME_MAP.get(name);
	}
	
	private final String name;
	private final String algorithm;
	private final String transformation;
	
	private Encryption(String algorithm) {
		this(algorithm, algorithm, algorithm);
	}
	
	private Encryption(String name, String algorithm) {
		this(name, algorithm, algorithm);
	}
	
	private Encryption(String name, String algorithm, String transformation) {
		this.name = name;
		this.algorithm = algorithm;
		this.transformation = transformation;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public String getAlgorithm() {
		return algorithm;
	}
	
	public String getTransformation() {
		return transformation;
	}
	
	public InputStream getCipherInputStream(InputStream inputStream,
			String password) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {
		Key key = generateKey(password);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return new CipherInputStream(inputStream, cipher);
	}
	
	public OutputStream getCipherOutputStream(OutputStream outputStream,
			String password) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException {
		Key key = generateKey(password);
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return new CipherOutputStream(outputStream, cipher);
	}
	
	private Key generateKey(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance(HASHING_ALGORITHM);
			byte[] passwordBytes = password.getBytes(KEY_CHARSET);
			byte[] passwordHash = digest.digest(passwordBytes);
			byte[] key;
			
			int hashLength = passwordHash.length;
			int maxKeyLength = Cipher.getMaxAllowedKeyLength(transformation) >> 3;
			
			if (hashLength != maxKeyLength) {
				key = new byte[maxKeyLength];
				int max = Math.max(hashLength, maxKeyLength);
				
				for (int i = 0; i < max; i++) {
					key[i % maxKeyLength] ^= passwordHash[i % hashLength];
				}
			} else {
				key = passwordHash;
			}
			
			return new SecretKeySpec(key, algorithm);
		} catch (Exception e) {
			return null;
		}
	}
	
}